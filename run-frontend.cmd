@echo off
setlocal
cd /d "%~dp0frontend"

rem ---- Locate a Node.js runtime WITHOUT relying on PATH or npm ----
set "NODE_EXE="
set "NPM_CLI="
for /d %%D in ("%USERPROFILE%\.workbuddy\binaries\node\versions\*") do (
  if exist "%%D\node.exe" set "NODE_EXE=%%D\node.exe"
  if exist "%%D\node_modules\npm\bin\npm-cli.js" set "NPM_CLI=%%D\node_modules\npm\bin\npm-cli.js"
)
if not defined NODE_EXE (
  where node >nul 2>nul
  if not errorlevel 1 set "NODE_EXE=node"
)
if not defined NODE_EXE (
  echo [ERROR] No Node.js runtime found on this machine.
  echo         Install Node.js LTS from https://nodejs.org/ , then run:
  echo             npm install
  echo             npm run dev
  pause
  exit /b 1
)
echo Node runtime: %NODE_EXE%

rem ---- Install dependencies only if they are missing ----
if not exist "node_modules\vite\bin\vite.js" (
  if not defined NPM_CLI (
    echo [ERROR] node_modules is missing and npm CLI was not found.
    echo         Install Node.js LTS from https://nodejs.org/ first.
    pause
    exit /b 1
  )
  echo Installing frontend dependencies ...
  "%NODE_EXE%" "%NPM_CLI%" install --no-audit --no-fund
  if errorlevel 1 (
    echo [ERROR] npm install failed.
    pause
    exit /b 1
  )
)

echo.
echo   Frontend : http://localhost:5173
echo   Backend  : must be running on :8080 (this version has no mock mode)
echo   Login    : demo / demo123
echo   Press Ctrl+C to stop.
echo.
"%NODE_EXE%" "node_modules\vite\bin\vite.js" --host 127.0.0.1
pause
