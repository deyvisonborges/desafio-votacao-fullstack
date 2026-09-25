#!/usr/bin/env bash
# Entry point: detects the OS, provisions Java/Docker, prepares .env and boots
# local dependencies. Windows: use scripts\setup.ps1 from PowerShell instead.

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/scripts/lib/common.sh"

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

OS="$(detect_os)"
log_info "Detected OS: $OS"

case "$OS" in
  macos) bash "$ROOT_DIR/scripts/setup-macos.sh" ;;
  linux|wsl) bash "$ROOT_DIR/scripts/setup-linux.sh" ;;
  windows)
    log_warn "Detected a Windows shell (Git Bash/MSYS). Prefer running scripts\\setup.ps1 from PowerShell instead."
    exit 1
    ;;
  *)
    log_error "Unsupported OS. Please install Java 21, Docker and Docker Compose manually."
    exit 1
    ;;
esac

if [ ! -f "$ROOT_DIR/.env" ]; then
  cp "$ROOT_DIR/.env.example" "$ROOT_DIR/.env"
  log_ok "Created .env from .env.example"
else
  log_info ".env already exists, leaving it untouched"
fi

chmod +x "$ROOT_DIR/mvnw" 2>/dev/null || true

log_info "Starting local dependencies (docker compose up -d)..."
docker compose up -d

echo "📦 Baixando dependências..."
./mvnw clean install -DskipTests

echo "🚀 Setup concluído!"