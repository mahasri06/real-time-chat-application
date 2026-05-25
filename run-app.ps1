$mavenBin = "C:\Users\admin\Tools\apache-maven-3.9.16\bin"

if (!(Test-Path "$mavenBin\mvn.cmd")) {
    Write-Host "Maven was not found at $mavenBin"
    Write-Host "Expected file: $mavenBin\mvn.cmd"
    exit 1
}

$env:Path = "$mavenBin;$env:Path"

Write-Host "Using Maven from: $mavenBin"
Write-Host "Starting app on: http://localhost:8081"
mvn spring-boot:run
