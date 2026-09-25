#!/usr/bin/env bash
# Adds one or more Maven dependencies to pom.xml (before </dependencies>).
#
# Usage:
#   scripts/add-dependency.sh "org.springframework.boot:spring-boot-starter-validation"
#   scripts/add-dependency.sh "groupId:artifactId:version" "another:dep:1.0"
#
# Set scope with --scope=test|provided|runtime (default: no scope element, i.e. compile).

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/common.sh"

ROOT_DIR="$(project_root)"
POM_FILE="$ROOT_DIR/pom.xml"
SCOPE=""

args=()
for arg in "$@"; do
  case "$arg" in
    --scope=*) SCOPE="${arg#--scope=}" ;;
    *) args+=("$arg") ;;
  esac
done

if [ "${#args[@]}" -eq 0 ]; then
  log_error "No dependency provided. Usage: make add-deps deps=\"groupId:artifactId[:version]\""
  exit 1
fi

for dep in "${args[@]}"; do
  IFS=':' read -r group artifact version <<< "$dep"

  if [ -z "$group" ] || [ -z "$artifact" ]; then
    log_error "Invalid dependency format: $dep (expected groupId:artifactId[:version])"
    continue
  fi

  if grep -q "<artifactId>${artifact}</artifactId>" "$POM_FILE"; then
    log_warn "Already present, skipping: $dep"
    continue
  fi

  block="        <dependency>\n            <groupId>${group}</groupId>\n            <artifactId>${artifact}</artifactId>"
  [ -n "$version" ] && block="${block}\n            <version>${version}</version>"
  [ -n "$SCOPE" ] && block="${block}\n            <scope>${SCOPE}</scope>"
  block="${block}\n        </dependency>"

  awk -v block="$block" '
    /<\/dependencies>/ && !inserted { printf "%s\n", block; inserted = 1 }
    { print }
  ' "$POM_FILE" > "$POM_FILE.tmp" && mv "$POM_FILE.tmp" "$POM_FILE"

  log_ok "Added: $dep${SCOPE:+ (scope: $SCOPE)}"
done

log_info "Validating pom.xml..."
(cd "$ROOT_DIR" && ./mvnw -q validate) || log_warn "Run './mvnw clean install' to verify the new dependency resolves."
