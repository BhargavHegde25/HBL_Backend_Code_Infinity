<#
.SYNOPSIS
    Shared helpers for getlist-diff.ps1 and getlist-timing.ps1. Dot-source it; do not run it directly.

.DESCRIPTION
    Signs a user in to Fabric (or reuses a claims token you supply) and calls
    POST /services/data/v1/Holdings/operations/DigitalArrangements/getList.

    Users come from a CSV file with a header row and these columns:
        label      a short name used for file names and output (no personal data), for example "retail1"
        token      optional: an existing X-Kony-Authorization claims token (use this when login needs MFA)
        username   optional: used with password when no token is given
        password   optional

    Tokens and passwords are never printed or written to disk by these scripts.
#>

Set-StrictMode -Version 2.0

# Windows PowerShell 5.1 defaults to older TLS versions.
[Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor [Net.SecurityProtocolType]::Tls12

$script:GetListPath = '/services/data/v1/Holdings/operations/DigitalArrangements/getList'

function Read-GetListUsers {
    param([Parameter(Mandatory = $true)][string] $UsersCsv)
    $users = @(Import-Csv -Path $UsersCsv)
    if ($users.Count -eq 0) { throw "No users in $UsersCsv" }
    foreach ($u in $users) {
        if (-not ($u.PSObject.Properties.Name -contains 'label') -or [string]::IsNullOrWhiteSpace($u.label)) {
            throw "Every row of $UsersCsv needs a 'label'"
        }
        if ($u.label -notmatch '^[A-Za-z0-9_-]+$') {
            throw "Label '$($u.label)' may only contain letters, digits, '-' and '_'"
        }
    }
    return $users
}

function Get-ClaimsToken {
    <#
    .SYNOPSIS
        Returns the user's claims token: the one in the CSV, or a new one from the Fabric identity login.
    #>
    param(
        [Parameter(Mandatory = $true)] $User,
        [string] $AuthUrl,
        [string] $IdentityProvider,
        [string] $AppKey,
        [string] $AppSecret
    )
    if (($User.PSObject.Properties.Name -contains 'token') -and -not [string]::IsNullOrWhiteSpace($User.token)) {
        return $User.token
    }
    if ([string]::IsNullOrWhiteSpace($AuthUrl) -or [string]::IsNullOrWhiteSpace($IdentityProvider)) {
        throw "User '$($User.label)' has no token: pass -AuthUrl and -IdentityProvider to sign in with username/password"
    }
    $headers = @{ 'Content-Type' = 'application/json' }
    if ($AppKey) { $headers['X-Kony-App-Key'] = $AppKey }
    if ($AppSecret) { $headers['X-Kony-App-Secret'] = $AppSecret }
    $body = @{ userid = $User.username; password = $User.password } | ConvertTo-Json -Compress
    $uri = $AuthUrl.TrimEnd('/') + '/login?provider=' + [Uri]::EscapeDataString($IdentityProvider)
    try {
        $login = Invoke-RestMethod -Method Post -Uri $uri -Headers $headers -Body $body
    } catch {
        throw "Login failed for user '$($User.label)': $($_.Exception.Message)"
    }
    if ($null -eq $login.claims_token -or [string]::IsNullOrWhiteSpace($login.claims_token.value)) {
        throw "Login for user '$($User.label)' returned no claims token (MFA? then put a token in the CSV)"
    }
    return $login.claims_token.value
}

function Invoke-GetList {
    <#
    .SYNOPSIS
        Calls getList once and returns @{ Millis; Status; Body }. Never throws on an HTTP error status.
    #>
    param(
        [Parameter(Mandatory = $true)][string] $BaseUrl,
        [Parameter(Mandatory = $true)][string] $Token,
        [string] $RequestBody = '{}',
        [string] $AppKey
    )
    $headers = @{ 'X-Kony-Authorization' = $Token; 'Content-Type' = 'application/json' }
    if ($AppKey) { $headers['X-Kony-App-Key'] = $AppKey }
    $uri = $BaseUrl.TrimEnd('/') + $script:GetListPath
    $watch = [Diagnostics.Stopwatch]::StartNew()
    try {
        $response = Invoke-WebRequest -Method Post -Uri $uri -Headers $headers -Body $RequestBody -UseBasicParsing
        $watch.Stop()
        return @{ Millis = $watch.Elapsed.TotalMilliseconds; Status = [int] $response.StatusCode; Body = $response.Content }
    } catch {
        # HTTP error status: WebException in Windows PowerShell 5.1, HttpResponseException in PowerShell 7.
        $watch.Stop()
        $status = 0
        $content = ''
        $errorResponse = $_.Exception.Response
        if ($errorResponse) {
            $status = [int] $errorResponse.StatusCode
            if ($_.ErrorDetails -and $_.ErrorDetails.Message) {
                $content = $_.ErrorDetails.Message
            } elseif ($errorResponse -is [System.Net.WebResponse]) {
                $reader = New-Object IO.StreamReader($errorResponse.GetResponseStream())
                $content = $reader.ReadToEnd()
            }
        } else {
            throw
        }
        return @{ Millis = $watch.Elapsed.TotalMilliseconds; Status = $status; Body = $content }
    }
}
