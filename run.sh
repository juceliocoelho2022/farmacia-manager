#!/usr/bin/env bash
# Executa o Sistema Farmacia (requer que build.sh ja tenha sido rodado).
set -euo pipefail
cd "$(dirname "$0")"

if [ ! -f dist/SistemaFarmacia.jar ]; then
  echo "dist/SistemaFarmacia.jar nao encontrado. Rode ./build.sh primeiro."
  exit 1
fi

java -jar dist/SistemaFarmacia.jar
