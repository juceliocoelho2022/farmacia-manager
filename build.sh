#!/usr/bin/env bash
#
# Compila o Sistema Farmacia e gera o JAR executavel em dist/SistemaFarmacia.jar
# Uso (Linux / macOS):  ./build.sh
#
set -euo pipefail
cd "$(dirname "$0")"

echo ">> Limpando saidas anteriores..."
rm -rf build/classes dist
mkdir -p build/classes dist

echo ">> Compilando codigo-fonte..."
find src/main/java -name '*.java' > build/sources.txt
javac -encoding UTF-8 -d build/classes @build/sources.txt

echo ">> Gerando JAR executavel..."
jar --create \
    --file dist/SistemaFarmacia.jar \
    --main-class com.farmacia.Main \
    -C build/classes .

echo ""
echo "OK! Gerado: dist/SistemaFarmacia.jar"
echo "Para executar:  java -jar dist/SistemaFarmacia.jar"
