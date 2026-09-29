<#
.SYNOPSIS
    Times Holdings/DigitalArrangements/getList: N calls per user, then min, median, p90 and max.

.DESCRIPTION
    Run it once with HBL_GETLIST_CACHE_ENABLED=false and once with it true, and compare the two summaries.
    The first call of each user is reported separately: with the cache on it is the cold call that reads the
    database and fills the cache; the statistics cover the calls after it.

    Set HBL_GETLIST_TIMING_LOG=true on the server at the same time to see, in the server log, how each request
    splits into stages ("snapshotLookup", "snapshotStore", "newAccountProcessing", ...).

    See getlist-common.ps1 for the users CSV format. Tokens and passwords are never printed.

.EXAMPLE
    .\getlist-timing.ps1 -BaseUrl https://qa.example/ -UsersCsv users.csv -Runs 20
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)][string] $BaseUrl,
    [Parameter(Mandatory = $true)][string] $UsersCsv,
    [ValidateRange(2, 1000)][int] $Runs = 20,
    [int] $PauseMs = 200,
    [string] $RequestBody = '{}',
    [string] $AuthUrl,
    [string] $IdentityProvider,
    [string] $AppKey,
    [string] $AppSecret
)

. (Join-Path $PSScriptRoot 'getlist-common.ps1')

function Get-Percentile([double[]] $sorted, [double] $p) {
    # Nearest-rank percentile on an ascending array.
    $rank = [Math]::Ceiling($p / 100.0 * $sorted.Count)
    return $sorted[[Math]::Max(0, $rank - 1)]
}

$failures = 0
foreach ($user in (Read-GetListUsers -UsersCsv $UsersCsv)) {
    $token = Get-ClaimsToken -User $user -AuthUrl $AuthUrl -IdentityProvider $IdentityProvider -AppKey $AppKey -AppSecret $AppSecret
    $first = $null
    $times = New-Object 'System.Collections.Generic.List[double]'
    for ($i = 1; $i -le $Runs; $i++) {
        $r = Invoke-GetList -BaseUrl $BaseUrl -Token $token -RequestBody $RequestBody -AppKey $AppKey
        if ($r.Status -ne 200) { $failures++; Write-Warning ("{0}: call {1} returned HTTP {2}" -f $user.label, $i, $r.Status) }
        if ($i -eq 1) { $first = $r.Millis } else { $times.Add($r.Millis) }
        Start-Sleep -Milliseconds $PauseMs
    }
    $sorted = [double[]] ($times | Sort-Object)
    '{0,-20} first {1,7:N0} ms | next {2} calls: min {3,6:N0}  median {4,6:N0}  p90 {5,6:N0}  max {6,6:N0} ms' -f `
        $user.label, $first, $sorted.Count, $sorted[0], (Get-Percentile $sorted 50), (Get-Percentile $sorted 90), $sorted[-1]
}
if ($failures -gt 0) { Write-Warning "$failures call(s) did not return HTTP 200"; exit 1 }
