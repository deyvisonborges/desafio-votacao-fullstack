#!/usr/bin/env bash

set -e

REQUIRED_JAVA_VERSION=21
ORBSTACK_SOCKET="$HOME/.orbstack/run/docker.sock"

echo "🔎 Verificando Java..."

if ! command -v java &> /dev/null
then
    echo "☕ Java não encontrado. Instalando via SDKMAN..."

    if ! command -v sdk &> /dev/null
    then
        curl -s "https://get.sdkman.io" | bash
        source "$HOME/.sdkman/bin/sdkman-init.sh"
    fi

    sdk install java ${REQUIRED_JAVA_VERSION}-tem
fi

echo "🐳 Verificando Docker..."

if [ -z "$DOCKER_HOST" ]; then
    if [ -S "$ORBSTACK_SOCKET" ]; then
        export DOCKER_HOST="unix://$ORBSTACK_SOCKET"
        echo "✅ DOCKER_HOST configurado para OrbStack: $DOCKER_HOST"
    else
        echo "ℹ️ DOCKER_HOST não definido. Usando padrão do sistema."
    fi
else
    echo "✅ DOCKER_HOST já definido: $DOCKER_HOST"
fi

echo "📦 Baixando dependências..."
./mvnw clean install -DskipTests

echo "🚀 Setup concluído!"