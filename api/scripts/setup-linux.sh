#!/usr/bin/env bash
# Linux-specific provisioning (Debian/Ubuntu, Fedora/RHEL, Arch). Invoked by setup.sh.

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/common.sh"

PKG_MANAGER=""
if command_exists apt-get; then PKG_MANAGER="apt"
elif command_exists dnf; then PKG_MANAGER="dnf"
elif command_exists pacman; then PKG_MANAGER="pacman"
else
  log_error "No supported package manager found (apt/dnf/pacman). Install Java 21 and Docker manually."
  exit 1
fi

install_java() {
  if command_exists java && java -version 2>&1 | grep -q '"21'; then
    log_ok "Java 21 already installed"
    return
  fi
  log_info "Installing OpenJDK 21..."
  case "$PKG_MANAGER" in
    apt)    sudo apt-get update -y && sudo apt-get install -y openjdk-21-jdk ;;
    dnf)    sudo dnf install -y java-21-openjdk java-21-openjdk-devel ;;
    pacman) sudo pacman -Sy --noconfirm jdk21-openjdk ;;
  esac
}

install_docker() {
  if command_exists docker; then
    log_ok "Docker already installed"
  else
    log_info "Installing Docker Engine..."
    curl -fsSL https://get.docker.com | sh
  fi

  if ! docker compose version >/dev/null 2>&1; then
    log_warn "docker compose plugin not found. Install 'docker-compose-plugin' via your package manager."
  fi

  if ! groups "$USER" | grep -q docker; then
    log_info "Adding $USER to the docker group (log out/in required to take effect)..."
    sudo usermod -aG docker "$USER" || true
  fi
}

configure_shell_env() {
  local snippet
  snippet=$(cat <<'EOF'
export JAVA_HOME="${JAVA_HOME:-$(dirname $(dirname $(readlink -f $(which java))))}"
export PATH="$JAVA_HOME/bin:$PATH"
export MAVEN_OPTS="-Xmx1536m"
if [ -f "PROJECT_ROOT/.env" ]; then
  set -a; source "PROJECT_ROOT/.env"; set +a
fi
EOF
)
  snippet="${snippet//PROJECT_ROOT/$(project_root)}"

  local rc_file="$HOME/.bashrc"
  [ -n "${ZSH_VERSION:-}" ] && rc_file="$HOME/.zshrc"

  append_to_rc_once "$rc_file" "dbserver-voting-api env" "$snippet"
}

main() {
  install_java
  install_docker
  configure_shell_env
  log_ok "Linux setup finished. Open a new terminal (or 'source ~/.bashrc'/'~/.zshrc') to load the env."
}

main "$@"
