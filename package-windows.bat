@echo off
REM ===================================================================
REM  Gera o INSTALADOR .EXE do Sistema Farmacia para Windows.
REM
REM  Pre-requisitos (na maquina Windows):
REM    1. JDK 17 ou superior instalado e no PATH (fornece o "jpackage").
REM    2. WiX Toolset 3.x instalado (necessario para o jpackage criar .exe):
REM         https://github.com/wixtoolset/wix3/releases
REM
REM  Passos:
REM    1. Rode primeiro  build.bat   (para gerar dist\SistemaFarmacia.jar)
REM    2. Rode           package-windows.bat
REM
REM  O instalador sera gerado na pasta  dist-installer\
REM ===================================================================
setlocal
cd /d "%~dp0"

if not exist dist\SistemaFarmacia.jar (
    echo Arquivo dist\SistemaFarmacia.jar nao encontrado.
    echo Rode primeiro:  build.bat
    exit /b 1
)

if exist dist-installer rmdir /s /q dist-installer
mkdir dist-installer

echo >> Gerando instalador .exe com jpackage...
jpackage ^
  --type exe ^
  --name "SistemaFarmacia" ^
  --app-version 1.0 ^
  --vendor "Farmacia" ^
  --description "Sistema de gestao e PDV para farmacia" ^
  --input dist ^
  --main-jar SistemaFarmacia.jar ^
  --main-class com.farmacia.Main ^
  --dest dist-installer ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --win-shortcut-prompt

if errorlevel 1 (
    echo.
    echo ERRO ao gerar o instalador. Confira se o WiX Toolset esta instalado.
    exit /b 1
)

echo.
echo OK! Instalador gerado na pasta dist-installer\
dir /b dist-installer
endlocal
