<#
.SYNOPSIS
    Puts the getList performance classes into copies of the jars already deployed on Fabric, without rebuilding
    or redeploying whole jars.

.DESCRIPTION
    1. Compile the changed modules locally, so their target/classes folders hold the new classes
       (see "Compile first" below).
    2. Download the currently deployed jars from Fabric into one folder (-JarsDir). Keep their file names.
    3. Run this script. For every jar it:
         - copies the jar to -OutDir (the downloaded original is never changed);
         - removes the old copy of each changed class, together with all its inner classes (Foo$1.class,
           Foo$Bar.class...), because a recompiled class can number its inner classes differently;
         - adds the new class and all its inner classes from the module's target/classes;
         - for a whole package (the new com.hbl... packages), replaces every class directly in that package.
    4. Upload the jars from -OutDir to Fabric and publish.

    -Scope GetList (default) patches only the three jars getList needs. -Scope All also patches the jars with the
    cache invalidation hooks (online-banking user/contract/permission services, HBL services, Spotlight). Use All
    before switching HBL_GETLIST_CACHE_ENABLED on anywhere but a performance test.

    Compile first (JDK 11, the same build as the deployed jars), from Code_HBL/dbplocalservices-main:
        mvn -pl DBPCommonUtilityServices,com.temenos.infinity.t24irisintegration,ArrangementsAPI-Services -am compile
    and for -Scope All also:
        mvn -pl eum-productservices,DBPProductServices,DBPNonProductServices,UserManagementAPI-Services -am compile
        (Code_HBL/hblservices-main)          mvn compile
        (Code_HBL/adminconsoleservices-main) mvn -pl AdminConsoleServices -am compile

.EXAMPLE
    .\patch-deployed-jars.ps1 -JarsDir C:\fabric-jars -OutDir C:\fabric-jars-patched
.EXAMPLE
    .\patch-deployed-jars.ps1 -JarsDir C:\fabric-jars -OutDir C:\fabric-jars-patched -Scope All -WhatIf
#>
[CmdletBinding(SupportsShouldProcess = $true)]
param(
    [Parameter(Mandatory = $true)][string] $JarsDir,
    [Parameter(Mandatory = $true)][string] $OutDir,
    [ValidateSet('GetList', 'All')][string] $Scope = 'GetList',
    [string] $RepoRoot = (Split-Path -Parent $PSScriptRoot)
)

Set-StrictMode -Version 2.0
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$dbp = 'Code_HBL/dbplocalservices-main'

# Every jar, the module whose target/classes holds its new classes, and what to replace. An entry ending in '/'
# is a whole package (its classes only, not sub-packages); any other entry is one class plus its inner classes.
$plan = @(
    @{ Jar = 'arrangementsapi-services'; Module = "$dbp/ArrangementsAPI-Services"; Scope = 'GetList'; Entries = @(
        'com/hbl/infinity/accounts/perf/',
        'com/temenos/infinity/api/arrangements/javaservice/GetAccountsOperation',
        'com/temenos/infinity/api/arrangements/postprocessors/GetAccountsPostLoginObjectServicePostProcessor') },
    @{ Jar = 'com.temenos.infinity.t24irisintegration'; Module = "$dbp/com.temenos.infinity.t24irisintegration"; Scope = 'GetList'; Entries = @(
        'com/infinity/dbx/temenos/accounts/getAccountsFromT24PreProcessor',
        'com/infinity/dbx/temenos/accounts/getAccountsFromT24PostProcessor') },
    @{ Jar = 'dbp-commonutilityservices'; Module = "$dbp/DBPCommonUtilityServices"; Scope = 'GetList'; Entries = @(
        'com/hbl/infinity/accounts/perf/invalidation/') },

    @{ Jar = 'arrangementsapi-services'; Module = "$dbp/ArrangementsAPI-Services"; Scope = 'All'; Entries = @(
        'com/temenos/infinity/api/arrangements/javaservice/UpdateCoreCustomerFavoriteStatus',
        'com/temenos/infinity/api/arrangements/javaservice/UpdateUserAccountSettingsOperation') },
    @{ Jar = 'dbp-productservices'; Module = "$dbp/DBPProductServices"; Scope = 'All'; Entries = @(
        'com/kony/dbputilities/accountservices/UpdateFavouriteStatus',
        'com/kony/dbputilities/customersecurityservices/AssociateBusinessUserActionLimitsAndGroup',
        'com/temenos/dbx/product/contract/javaservice/ContractCreateOperation',
        'com/temenos/dbx/product/contract/javaservice/ContractEnrollOperation',
        'com/temenos/dbx/product/contract/javaservice/ContractUpdateByStatusOperation',
        'com/temenos/dbx/product/contract/javaservice/ContractUpdateOperation',
        'com/temenos/dbx/product/limitsandpermissions/javaservices/UpdateCustomerRoleLimitsAndPermissionsOperation',
        'com/temenos/dbx/product/limitsandpermissions/javaservices/UpdateServiceDefinitionLimitsAndPermissionsOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/ApplyCustomRoleOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/AssignInfinityUserToPrimaryRetailContract',
        'com/temenos/dbx/product/usermanagement/javaservice/CreateBusinessContractOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/CreateInfinityUserWithContractOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/CreateRetailContractOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/CustomRoleApplyToUsersOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/CustomRoleCreateOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/CustomRoleDeleteOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/CustomRoleUpdateOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/EditInfinityUserOperation',
        'com/temenos/dbx/product/usermanagement/javaservice/InfinityUserStatusUpdateOperation') },
    @{ Jar = 'eum-productservices'; Module = "$dbp/eum-productservices"; Scope = 'All'; Entries = @(
        'com/temenos/dbx/eum/product/contract/javaservice/ContractCreateOperation',
        'com/temenos/dbx/eum/product/contract/javaservice/ContractEnrollOperation',
        'com/temenos/dbx/eum/product/contract/javaservice/ContractUpdateByStatusOperation',
        'com/temenos/dbx/eum/product/contract/javaservice/ContractUpdateOperation',
        'com/temenos/dbx/eum/product/limitsandpermissions/javaservices/UpdateCustomerRoleLimitsAndPermissionsOperation',
        'com/temenos/dbx/eum/product/limitsandpermissions/javaservices/UpdateServiceDefinitionLimitsAndPermissionsOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/AddNewFeaturestoContractOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/ApplyCustomRoleOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/AssignInfinityUserToPrimaryRetailContract',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CreateBusinessContractOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CreateInfinityUserWithContractOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CreateRetailContractOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CustomRoleApplyToUsersOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CustomRoleCreateOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CustomRoleDeleteOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/CustomRoleUpdateOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/EditInfinityUserOperation',
        'com/temenos/dbx/eum/product/usermanagement/javaservice/InfinityUserStatusUpdateOperation') },
    @{ Jar = 'dbp-nonproductservices'; Module = "$dbp/DBPNonProductServices"; Scope = 'All'; Entries = @(
        'com/kony/dbputilities/accountservices/NewAccountOpening',
        'com/kony/dbputilities/accountservices/UpdateUserAccountSettings') },
    @{ Jar = 'usermanagementapi-services'; Module = "$dbp/UserManagementAPI-Services"; Scope = 'All'; Entries = @(
        'com/temenos/infinity/api/usermanagement/javaservice/UpdateUserAccountSettingsOperation') },
    @{ Jar = 'HBLServices'; Module = 'Code_HBL/hblservices-main'; Scope = 'All'; Entries = @(
        'com/bct/javaservices/CrossBorderConsent',
        'com/bct/javaservices/ResetCustomerDefaultAccount',
        'com/bct/javaservices/UpdateAccountNickName') },
    @{ Jar = 'AdminConsoleServices'; Module = 'Code_HBL/adminconsoleservices-main/AdminConsoleServices'; Scope = 'All'; Entries = @(
        'com/hbl/adminconsole/getlistcache/',
        'com/kony/adminconsole/service/business/BusinessBankingCustomerServiceLimitManageService',
        'com/kony/adminconsole/service/business/UpgradeRolesAndPermissionsService',
        'com/kony/adminconsole/service/business/UpgradeUserService',
        'com/kony/adminconsole/service/businesstype/BusinessTypeDeleteService',
        'com/kony/adminconsole/service/campaignmanagement/CustomerGroupMappingService',
        'com/kony/adminconsole/service/campaignmanagement/CustomerGroupUnMappingService',
        'com/kony/adminconsole/service/customermanagement/CustomerUpdateActions',
        'com/kony/adminconsole/service/customermanagement/CustomerUpdateGroups',
        'com/kony/adminconsole/service/customermanagement/MemberCreate',
        'com/kony/adminconsole/service/featuresandactions/FeatureManageService',
        'com/kony/adminconsole/service/group/GroupCreateService',
        'com/kony/adminconsole/service/group/GroupEditService') }
)

function Find-Jar([string] $name) {
    # Deployed jars may carry a version suffix, for example arrangementsapi-services-0.0.1-SNAPSHOT.jar.
    $candidates = @(Get-ChildItem -Path $JarsDir -Filter '*.jar' | Where-Object {
            $_.BaseName -eq $name -or $_.BaseName -like "$name-[0-9]*" })
    if ($candidates.Count -gt 1) { throw "More than one jar in $JarsDir matches '$name': $($candidates.Name -join ', ')" }
    if ($candidates.Count -eq 1) { return $candidates[0] }
    return $null
}

function Get-ClassFiles([string] $classesDir, [string] $entry) {
    # Returns @{ Name = 'com/x/Foo.class'; Path = full path } for the class and its inner classes, or every class
    # of a package when the entry ends with '/'.
    if ($entry.EndsWith('/')) {
        $dir = Join-Path $classesDir $entry
        if (-not (Test-Path $dir)) { return @() }
        return @(Get-ChildItem -Path $dir -Filter '*.class' -File | ForEach-Object { @{ Name = $entry + $_.Name; Path = $_.FullName } })
    }
    $dir = Join-Path $classesDir (Split-Path $entry -Parent)
    $simple = Split-Path $entry -Leaf
    $folder = (Split-Path $entry -Parent).Replace('\', '/') + '/'
    if (-not (Test-Path $dir)) { return @() }
    return @(Get-ChildItem -Path $dir -File | Where-Object { $_.Name -eq "$simple.class" -or $_.Name -like "$simple`$*.class" } |
            ForEach-Object { @{ Name = $folder + $_.Name; Path = $_.FullName } })
}

function Test-OldEntry([string] $entryName, [string] $spec) {
    if ($spec.EndsWith('/')) {
        return $entryName.StartsWith($spec) -and $entryName.EndsWith('.class') -and -not $entryName.Substring($spec.Length).Contains('/')
    }
    return $entryName -eq "$spec.class" -or ($entryName.StartsWith("$spec`$") -and $entryName.EndsWith('.class'))
}

function Get-ClassMajorVersion([string] $path) {
    $bytes = [IO.File]::ReadAllBytes($path)
    return ($bytes[6] * 256) + $bytes[7]
}

# Group the plan by jar, keeping only the requested scope.
$selected = $plan | Where-Object { $Scope -eq 'All' -or $_.Scope -eq 'GetList' }
$byJar = [ordered]@{}
foreach ($step in $selected) {
    if (-not $byJar.Contains($step.Jar)) { $byJar[$step.Jar] = @{ Module = $step.Module; Entries = @() } }
    $byJar[$step.Jar].Entries += $step.Entries
}

# Check everything before writing anything.
$work = @()
$problems = @()
foreach ($jarName in $byJar.Keys) {
    $item = $byJar[$jarName]
    $jar = Find-Jar $jarName
    if ($null -eq $jar) { $problems += "Jar '$jarName' (.jar) not found in $JarsDir"; continue }
    $classesDir = Join-Path (Join-Path $RepoRoot $item.Module) 'target/classes'
    if (-not (Test-Path $classesDir)) { $problems += "$classesDir does not exist: compile module $($item.Module) first"; continue }
    $files = @()
    foreach ($entry in $item.Entries) {
        $found = @(Get-ClassFiles $classesDir $entry)
        if ($found.Count -eq 0) { $problems += "No compiled class for '$entry' in $classesDir"; continue }
        foreach ($f in $found) {
            $major = Get-ClassMajorVersion $f.Path
            if ($major -gt 55) { $problems += "$($f.Name) was compiled for Java $($major - 44); the deployed code targets Java 11: build with JDK 11 or -Dmaven.compiler.release=11" }
        }
        $files += $found
    }
    $work += @{ Jar = $jar; Entries = $item.Entries; Files = $files }
}
if ($problems.Count -gt 0) {
    $problems | ForEach-Object { Write-Host "ERROR: $_" -ForegroundColor Red }
    throw "Nothing was written. Fix the errors above and run again."
}

New-Item -ItemType Directory -Force -Path $OutDir | Out-Null
foreach ($w in $work) {
    $target = Join-Path $OutDir $w.Jar.Name
    if (-not $PSCmdlet.ShouldProcess($target, "replace $($w.Files.Count) class file(s)")) {
        foreach ($f in $w.Files) { "  would add $($f.Name)" }
        continue
    }
    Copy-Item -Path $w.Jar.FullName -Destination $target -Force
    $zip = [IO.Compression.ZipFile]::Open($target, [IO.Compression.ZipArchiveMode]::Update)
    try {
        if (@($zip.Entries | Where-Object { $_.FullName -match '^META-INF/[^/]+\.(SF|RSA|DSA|EC)$' }).Count -gt 0) {
            Write-Warning "$($w.Jar.Name) is signed; a patched jar fails signature checks if Fabric verifies them"
        }
        $removed = 0
        foreach ($old in @($zip.Entries)) {
            foreach ($spec in $w.Entries) {
                if (Test-OldEntry $old.FullName $spec) { $old.Delete(); $removed++; break }
            }
        }
        foreach ($f in $w.Files) {
            [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, $f.Path, $f.Name) | Out-Null
        }
        '{0}: removed {1} old class file(s), added {2}' -f $w.Jar.Name, $removed, $w.Files.Count
        foreach ($f in $w.Files) { "    + $($f.Name)" }
    } finally {
        $zip.Dispose()
    }
}
''
"Patched jars are in $OutDir. The downloaded originals in $JarsDir are unchanged: keep them for rollback."
