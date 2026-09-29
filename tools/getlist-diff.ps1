<#
.SYNOPSIS
    Checks that the getList cache changes nothing: captures getList responses with the cache off and on, then
    compares them field by field.

.DESCRIPTION
    Step 1  With HBL_GETLIST_CACHE_ENABLED=false on the server:
                .\getlist-diff.ps1 -Mode Capture -Label off -BaseUrl https://qa.example/ -UsersCsv users.csv -OutDir out
    Step 2  Set HBL_GETLIST_CACHE_ENABLED=true (no restart needed), then:
                .\getlist-diff.ps1 -Mode Capture -Label on -BaseUrl https://qa.example/ -UsersCsv users.csv -OutDir out
            Capture calls getList twice per user by default: call1 is the cold call (reads the database and fills
            the cache), call2 the warm one (served from the cache).
    Step 3  .\getlist-diff.ps1 -Mode Compare -OutDir out -Left off -Right on

    Compare checks every Right call of each user against the user's Left call1. Accounts are matched by account
    id, so their order does not matter; every other field must be equal. Balances come live from T24 on every
    call, so if they move between captures, pass -IgnoreFields availableBalance,currentBalance (and so on).
    The exit code is 0 when there is no difference and 1 otherwise.

    The captured files contain customer account data. Keep -OutDir private and delete it after the check.
    See getlist-common.ps1 for the users CSV format.

.EXAMPLE
    .\getlist-diff.ps1 -Mode Compare -OutDir out -Left off -Right on -IgnoreFields availableBalance,currentBalance
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)][ValidateSet('Capture', 'Compare')][string] $Mode,
    [Parameter(Mandatory = $true)][string] $OutDir,
    # Capture
    [string] $Label,
    [string] $BaseUrl,
    [string] $UsersCsv,
    [int] $Calls = 2,
    [string] $RequestBody = '{}',
    [string] $AuthUrl,
    [string] $IdentityProvider,
    [string] $AppKey,
    [string] $AppSecret,
    # Compare
    [string] $Left = 'off',
    [string] $Right = 'on',
    [string[]] $IgnoreFields = @()
)

. (Join-Path $PSScriptRoot 'getlist-common.ps1')

function Invoke-Capture {
    if (-not $Label -or -not $BaseUrl -or -not $UsersCsv) { throw 'Capture needs -Label, -BaseUrl and -UsersCsv' }
    if ($Label -notmatch '^[A-Za-z0-9_-]+$') { throw "Label may only contain letters, digits, '-' and '_'" }
    $dir = Join-Path $OutDir $Label
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
    foreach ($user in (Read-GetListUsers -UsersCsv $UsersCsv)) {
        $token = Get-ClaimsToken -User $user -AuthUrl $AuthUrl -IdentityProvider $IdentityProvider -AppKey $AppKey -AppSecret $AppSecret
        for ($i = 1; $i -le $Calls; $i++) {
            $r = Invoke-GetList -BaseUrl $BaseUrl -Token $token -RequestBody $RequestBody -AppKey $AppKey
            $file = Join-Path $dir ('{0}-call{1}.json' -f $user.label, $i)
            [IO.File]::WriteAllText($file, [string] $r.Body, (New-Object Text.UTF8Encoding($false)))
            '{0,-20} call{1}  HTTP {2}  {3,8:N0} ms' -f $user.label, $i, $r.Status, $r.Millis
        }
    }
    "Saved to $dir"
}

function Get-AccountId($account) {
    foreach ($name in 'Account_id', 'account_id', 'accountID', 'accountId') {
        $p = $account.PSObject.Properties[$name]
        if ($p -and $p.Value) { return [string] $p.Value }
    }
    return $null
}

function Compare-Node($leftNode, $rightNode, [string] $path, [System.Collections.Generic.List[string]] $out) {
    if ($null -eq $leftNode -and $null -eq $rightNode) { return }
    if ($null -eq $leftNode -or $null -eq $rightNode) {
        $out.Add("${path}: $(Format-Value $leftNode) -> $(Format-Value $rightNode)"); return
    }
    if ($leftNode -is [Management.Automation.PSCustomObject] -and $rightNode -is [Management.Automation.PSCustomObject]) {
        $names = @($leftNode.PSObject.Properties.Name) + @($rightNode.PSObject.Properties.Name) | Sort-Object -Unique
        foreach ($n in $names) {
            if ($IgnoreFields -contains $n) { continue }
            $childPath = if ($path) { "$path.$n" } else { $n }
            if ($n -eq 'Accounts') {
                Compare-Accounts $leftNode.$n $rightNode.$n $childPath $out
            } else {
                $l = $leftNode.PSObject.Properties[$n]; $r = $rightNode.PSObject.Properties[$n]
                if ($null -eq $l) { $out.Add("${childPath}: (missing) -> $(Format-Value $r.Value)"); continue }
                if ($null -eq $r) { $out.Add("${childPath}: $(Format-Value $l.Value) -> (missing)"); continue }
                Compare-Node $l.Value $r.Value $childPath $out
            }
        }
        return
    }
    if ($leftNode -is [Array] -and $rightNode -is [Array]) {
        if ($leftNode.Count -ne $rightNode.Count) { $out.Add("${path}: $($leftNode.Count) items -> $($rightNode.Count) items"); return }
        for ($i = 0; $i -lt $leftNode.Count; $i++) { Compare-Node $leftNode[$i] $rightNode[$i] "$path[$i]" $out }
        return
    }
    if ([string] $leftNode -cne [string] $rightNode -or $leftNode.GetType() -ne $rightNode.GetType()) {
        $out.Add("${path}: $(Format-Value $leftNode) -> $(Format-Value $rightNode)")
    }
}

function Compare-Accounts($leftAccounts, $rightAccounts, [string] $path, [System.Collections.Generic.List[string]] $out) {
    $l = @{}; $r = @{}
    foreach ($a in @($leftAccounts)) { $l[(Get-AccountId $a)] = $a }
    foreach ($a in @($rightAccounts)) { $r[(Get-AccountId $a)] = $a }
    if (@($leftAccounts).Count -ne $l.Count -or @($rightAccounts).Count -ne $r.Count) {
        $out.Add("${path}: accounts without a unique id; compared by position")
        Compare-Node @($leftAccounts) @($rightAccounts) $path $out
        return
    }
    foreach ($id in (@($l.Keys) + @($r.Keys) | Sort-Object -Unique)) {
        if (-not $r.ContainsKey($id)) { $out.Add("$path[$id]: present -> (missing)"); continue }
        if (-not $l.ContainsKey($id)) { $out.Add("$path[$id]: (missing) -> present"); continue }
        Compare-Node $l[$id] $r[$id] "$path[$id]" $out
    }
}

function Format-Value($value) {
    if ($null -eq $value) { return 'null' }
    $text = if ($value -is [string]) { '"' + $value + '"' } else { ($value | ConvertTo-Json -Compress -Depth 20) }
    if ($text.Length -gt 120) { $text = $text.Substring(0, 117) + '...' }
    return $text
}

function Invoke-Compare {
    $leftDir = Join-Path $OutDir $Left
    $rightDir = Join-Path $OutDir $Right
    $totalDiffs = 0; $compared = 0
    foreach ($baseline in (Get-ChildItem -Path $leftDir -Filter '*-call1.json' | Sort-Object Name)) {
        $user = $baseline.Name -replace '-call1\.json$', ''
        $leftJson = Get-Content -Raw -Encoding UTF8 $baseline.FullName | ConvertFrom-Json
        $rightFiles = @(Get-ChildItem -Path $rightDir -Filter "$user-call*.json" | Sort-Object Name)
        if ($rightFiles.Count -eq 0) { "{0,-20} no '{1}' capture" -f $user, $Right; $totalDiffs++; continue }
        foreach ($rightFile in $rightFiles) {
            $diffs = New-Object 'System.Collections.Generic.List[string]'
            Compare-Node $leftJson (Get-Content -Raw -Encoding UTF8 $rightFile.FullName | ConvertFrom-Json) '' $diffs
            $compared++
            $call = $rightFile.Name -replace '^.*-(call\d+)\.json$', '$1'
            if ($diffs.Count -eq 0) {
                '{0,-20} {1}/call1 vs {2}/{3}: identical' -f $user, $Left, $Right, $call
            } else {
                '{0,-20} {1}/call1 vs {2}/{3}: {4} difference(s)' -f $user, $Left, $Right, $call, $diffs.Count
                $diffs | ForEach-Object { "    $_" }
                $totalDiffs += $diffs.Count
            }
        }
    }
    ''
    "Compared $compared response(s); $totalDiffs difference(s)."
    if ($totalDiffs -gt 0) { exit 1 }
}

if ($Mode -eq 'Capture') { Invoke-Capture } else { Invoke-Compare }
