#!/usr/bin/env bash
# Sanity-checks that the local environment has everything needed to build/run the project.

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/common.sh"

fail=0

check() {
  local label="$1"; shift
  if "$@" >/dev/null 2>&1; then
    log_ok "$label"
  else
    log_error "$label"
    fail=1
  fi
}

check "java (21) available" bash -c 'java -version 2>&1 | grep -q "\"21"'
check "docker available" command_exists docker
check "docker compose available" docker compose version
check "mvnw executable" test -x "$(project_root)/mvnw"
check ".env file present" test -f "$(project_root)/.env"

if docker info >/dev/null 2>&1; then
  log_ok "docker daemon running"
else
  log_error "docker daemon not running"
  fail=1
fi

if [ "$fail" -eq 0 ]; then
  log_ok "Environment looks good."
else
  log_warn "Some checks failed. Run 'make setup' again or fix the items above."
fi

exit "$fail"
