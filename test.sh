#!/usr/bin/env bash

set -e

ORBSTACK_SOCKET="$HOME/.orbstack/run/docker.sock"

if [ -z "$DOCKER_HOST" ] && [ -S "$ORBSTACK_SOCKET" ]; then
    export DOCKER_HOST="unix://$ORBSTACK_SOCKET"
    echo "🐳 Usando OrbStack em $DOCKER_HOST"
fi

echo "🧪 Rodando testes..."
./mvnw clean test