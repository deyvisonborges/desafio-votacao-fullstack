#!/usr/bin/env bash
# Common helpers shared by the setup scripts. Not meant to be executed directly.

set -euo pipefail

C_RESET='\033[0m'
C_GREEN='\033[0;32m'
C_YELLOW='\033[1;33m'
C_RED='\033[0;31m'
C_BLUE='\033[0;34m'

log_info()  { printf "${C_BLUE}[info]${C_RESET} %s\n" "$1"; }
log_ok()    { printf "${C_GREEN}[ ok ]${C_RESET} %s\n" "$1"; }
log_warn()  { printf "${C_YELLOW}[warn]${C_RESET} %s\n" "$1"; }
log_error() { printf "${C_RED}[fail]${C_RESET} %s\n" "$1"; }

command_exists() { command -v "$1" >/dev/null 2>&1; }

detect_os() {
  case "$(uname -s)" in
    Darwin) echo "macos" ;;
    Linux)
      if grep -qi microsoft /proc/version 2>/dev/null; then
        echo "wsl"
      else
        echo "linux"
      fi
      ;;
    MINGW*|MSYS*|CYGWIN*) echo "windows" ;;
    *) echo "unknown" ;;
  esac
}

# Appends a block to a shell rc file, guarded by markers so it's idempotent.
append_to_rc_once() {
  local rc_file="$1"
  local marker="$2"
  local content="$3"

  [ -f "$rc_file" ] || touch "$rc_file"

  if grep -qF "$marker" "$rc_file" 2>/dev/null; then
    log_info "Env block already present in $rc_file, skipping"
    return 0
  fi

  {
    echo ""
    echo "# >>> $marker >>>"
    echo "$content"
    echo "# <<< $marker <<<"
  } >> "$rc_file"
  log_ok "Updated $rc_file"
}

project_root() {
  cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd
}
