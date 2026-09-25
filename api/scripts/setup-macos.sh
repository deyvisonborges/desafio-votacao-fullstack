#!/usr/bin/env bash
# macOS-specific provisioning. Invoked by setup.sh — do not run directly.

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/common.sh"

install_homebrew() {
  if command_exists brew; then
    log_ok "Homebrew already installed"
    return
  fi
  log_info "Installing Homebrew..."
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

  if [ -d /opt/homebrew/bin ]; then
    eval "$(/opt/homebrew/bin/brew shellenv)"
  elif [ -d /usr/local/bin ]; then
    eval "$(/usr/local/bin/brew shellenv)"
  fi
}

install_java() {
  if command_exists java && java -version 2>&1 | grep -q '"21'; then
    log_ok "Java 21 already installed"
    return
  fi
  log_info "Installing Temurin 21 (OpenJDK)..."
  brew install --cask temurin@21 || brew install openjdk@21
}

install_docker() {
  if command_exists docker; then
    log_ok "Docker already installed"
    return
  fi
  log_info "Installing Docker Desktop..."
  brew install --cask docker
  log_warn "Open Docker.app once from Launchpad to finish setup, then re-run this script."
}

configure_shell_env() {
  local java_home_snippet
  java_home_snippet=$(cat <<'EOF'
export JAVA_HOME="$(/usr/libexec/java_home -v 21 2>/dev/null || echo "$JAVA_HOME")"
export PATH="$JAVA_HOME/bin:$PATH"
export MAVEN_OPTS="-Xmx1536m"
if [ -f "PROJECT_ROOT/.env" ]; then
  set -a; source "PROJECT_ROOT/.env"; set +a
fi
EOF
)
  java_home_snippet="${java_home_snippet//PROJECT_ROOT/$(project_root)}"

  local rc_file="$HOME/.zprofile"
  [ "$SHELL" = "/bin/bash" ] && rc_file="$HOME/.bash_profile"

  append_to_rc_once "$rc_file" "dbserver-voting-api env" "$java_home_snippet"
}

main() {
  install_homebrew
  install_java
  install_docker
  configure_shell_env
  log_ok "macOS setup finished. Open a new terminal (or 'source ~/.zprofile') to load the env."
}

main "$@"
