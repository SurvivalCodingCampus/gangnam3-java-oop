@echo off
setlocal
cd /d "%~dp0"

if not defined JAVA_HOME call :detectjava

if not defined JAVA_HOME (
    echo [ERROR] Java not found. Set JAVA_HOME and try again.
    pause
    exit /b 1
)

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] Invalid JAVA_HOME: %JAVA_HOME%
    pause
    exit /b 1
)

echo [INFO] JAVA_HOME=%JAVA_HOME%
call gradlew.bat run %*

if errorlevel 1 (
    echo.
    echo [ERROR] Game failed to start. Check the log above.
    pause
    exit /b 1
)
endlocal
exit /b 0

:detectjava
for %%D in (
    "%ProgramFiles%\JetBrains\IntelliJ IDEA 2026.2.3\jbr"
    "%ProgramFiles%\JetBrains\IntelliJ IDEA 2025.1\jbr"
    "%ProgramFiles%\Java\jdk-21"
    "%ProgramFiles%\Java\jdk-17"
    "%ProgramFiles%\Eclipse Adoptium\jdk-21"
) do (
    if exist "%%~D\bin\java.exe" (
        set "JAVA_HOME=%%~D"
        exit /b 0
    )
)

for /f "delims=" %%J in ('where java 2^>nul') do (
    if exist "%%J" (
        for %%P in ("%%J") do set "JAVA_HOME=%%~dpP.."
        exit /b 0
    )
)
exit /b 0
