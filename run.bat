@echo off
setlocal

REM Di chuyen vao thu muc chua file bat
cd /d "%~dp0"

REM Tao thu muc build neu chua co
if not exist build\classes mkdir build\classes

echo [1/2] Compiling source files...
REM Dung UTF-8 de doc duoc comment tieng Viet co dau
javac -encoding UTF-8 -cp "lib/*" -d build\classes App.java command\*.java db\*.java model\*.java ui\*.java
if errorlevel 1 (
  echo.
  echo Compile failed.
  exit /b 1
)

echo [2/2] Running application...
java -cp "build\classes;lib/*" App

endlocal