#!/usr/bin/env bash
#
# Compila e executa os testes de unidade (JUnit 5).
# Baixa o JUnit Console Standalone na primeira execucao (requer internet).
#
set -euo pipefail
cd "$(dirname "$0")"

JUNIT_VERSION="1.11.3"
JUNIT_JAR="build/junit-platform-console-standalone-${JUNIT_VERSION}.jar"
JUNIT_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar"

mkdir -p build/classes build/test-classes

if [ ! -f "$JUNIT_JAR" ]; then
  echo ">> Baixando JUnit..."
  curl -sSL -o "$JUNIT_JAR" "$JUNIT_URL"
fi

echo ">> Compilando codigo principal..."
find src/main/java -name '*.java' > build/sources.txt
javac -encoding UTF-8 -d build/classes @build/sources.txt

echo ">> Compilando testes..."
find src/test/java -name '*.java' > build/test-sources.txt
javac -encoding UTF-8 -cp "build/classes:${JUNIT_JAR}" -d build/test-classes @build/test-sources.txt

echo ">> Executando testes..."
java -jar "$JUNIT_JAR" execute \
  -cp "build/classes:build/test-classes" \
  --scan-classpath --details=tree
