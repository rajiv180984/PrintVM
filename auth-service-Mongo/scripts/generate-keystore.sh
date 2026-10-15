#!/usr/bin/env bash
# Generates a self-signed PKCS12 keystore for local HTTPS (localhost / 127.0.0.1).
# Usage: ./scripts/generate-keystore.sh [password]
# For production use a CA-issued certificate, e.g.:
#   openssl pkcs12 -export -in cert.pem -inkey key.pem -name auth-service -out keystore.p12
set -euo pipefail

PASSWORD="${1:-changeit}"
DIR="$(cd "$(dirname "$0")/.." && pwd)"
KEYSTORE="$DIR/src/main/resources/keystore.p12"
CERT="$DIR/scripts/auth-service.crt"

rm -f "$KEYSTORE" "$CERT"

keytool -genkeypair \
  -alias auth-service \
  -keyalg RSA -keysize 2048 \
  -storetype PKCS12 \
  -keystore "$KEYSTORE" \
  -storepass "$PASSWORD" \
  -validity 825 \
  -dname "CN=localhost, OU=Dev, O=Auth Service, L=Local, C=IN" \
  -ext "SAN=dns:localhost,ip:127.0.0.1"

# Public certificate, e.g. to trust in Postman / the OS / curl --cacert
keytool -exportcert -rfc -alias auth-service -keystore "$KEYSTORE" -storepass "$PASSWORD" -file "$CERT"

echo "Keystore : $KEYSTORE"
echo "Cert     : $CERT"
