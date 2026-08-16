@echo off
REM ===================================================================
REM  Compila o Sistema Farmacia e gera dist\SistemaFarmacia.jar
REM  Uso (Windows):  build.bat
REM  Requer o JDK 17 ou superior instalado e no PATH.
REM ===================================================================
setlocal
cd /d "%~dp0"

echo >> Limpando saidas anteriores...
if exist build\classes rmdir /s /q build\classes
if exist dist rmdir /s /q dist
mkdir build\classes
mkdir dist

echo >> Compilando codigo-fonte...
dir /s /b src\main\java\*.java > build\sources.txt
javac -encoding UTF-8 -d build\classes @build\sources.txt
if errorlevel 1 goto erro

echo >> Gerando JAR executavel...
jar --create --file dist\SistemaFarmacia.jar --main-class com.farmacia.Main -C build\classes .
if errorlevel 1 goto erro

echo.
echo OK! Gerado: dist\SistemaFarmacia.jar
echo Para executar:  java -jar dist\SistemaFarmacia.jar
goto fim

:erro
echo.
echo ERRO na compilacao. Verifique se o JDK esta instalado (java -version).
exit /b 1

:fim
endlocal
