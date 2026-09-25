#!/usr/bin/env bash
# Removes one or more Maven dependencies (by artifactId) from pom.xml.
#
# Usage: scripts/remove-dependency.sh "spring-boot-starter-validation"

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/common.sh"

ROOT_DIR="$(project_root)"
POM_FILE="$ROOT_DIR/pom.xml"

if [ "$#" -eq 0 ]; then
  log_error "No dependency provided. Usage: make remove-deps deps=\"artifactId\""
  exit 1
fi

for artifact in "$@"; do
  if ! grep -q "<artifactId>${artifact}</artifactId>" "$POM_FILE"; then
    log_warn "Not found, skipping: $artifact"
    continue
  fi

  awk -v artifact="$artifact" '
    BEGIN { skip = 0 }
    /<dependency>/ { block = $0 "\n"; in_dep = 1; found = 0; next }
    in_dep {
      block = block $0 "\n"
      if ($0 ~ "<artifactId>" artifact "</artifactId>") found = 1
      if ($0 ~ /<\/dependency>/) {
        in_dep = 0
        if (!found) printf "%s", block
        next
      }
      next
    }
    { print }
  ' "$POM_FILE" > "$POM_FILE.tmp" && mv "$POM_FILE.tmp" "$POM_FILE"

  log_ok "Removed: $artifact"
done
