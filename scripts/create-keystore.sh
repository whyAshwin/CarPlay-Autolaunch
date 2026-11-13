#!/usr/bin/env bash
set -euo pipefail

KEYSTORE_FILE="release-keystore.jks"
KEY_ALIAS="upload-key"
DNAME="CN=Android Release, O=FieldOps Engineering, C=US"

echo "Generating PKCS12 Android release keystore: $KEYSTORE_FILE"
keytool -genkeypair -v \
  -keystore "$KEYSTORE_FILE" \
  -storetype PKCS12 \
  -keyalg RSA \
  -keysize 2048 \
  -validity 9125 \
  -alias "$KEY_ALIAS" \
  -dname "$DNAME"

echo "Done. Keep $KEYSTORE_FILE and its passwords out of version control."
