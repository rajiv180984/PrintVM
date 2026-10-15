# Generates a self-signed PKCS12 keystore for local HTTPS (localhost / 127.0.0.1).
# Usage: powershell -ExecutionPolicy Bypass -File scripts\generate-keystore.ps1 [-Password changeit]
param([string]$Password = "changeit")

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$keystore = Join-Path $root "src\main\resources\keystore.p12"
$cert = Join-Path $root "scripts\auth-service.crt"

Remove-Item -ErrorAction SilentlyContinue $keystore, $cert

keytool -genkeypair `
  -alias auth-service `
  -keyalg RSA -keysize 2048 `
  -storetype PKCS12 `
  -keystore $keystore `
  -storepass $Password `
  -validity 825 `
  -dname "CN=localhost, OU=Dev, O=Auth Service, L=Local, C=IN" `
  -ext "SAN=dns:localhost,ip:127.0.0.1"
if ($LASTEXITCODE -ne 0) { throw "keytool -genkeypair failed" }

keytool -exportcert -rfc -alias auth-service -keystore $keystore -storepass $Password -file $cert
if ($LASTEXITCODE -ne 0) { throw "keytool -exportcert failed" }

Write-Host "Keystore : $keystore"
Write-Host "Cert     : $cert"
