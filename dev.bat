@echo off
setlocal

if "%1"=="" goto help
if "%1"=="help" goto help

if /I "%1"=="s" goto run
if /I "%1"=="run" goto run
if /I "%1"=="c" goto clean
if /I "%1"=="i" goto install
if /I "%1"=="t" goto test
if /I "%1"=="p" goto package
if /I "%1"=="d" goto dependency
if /I "%1"=="v" goto version
if /I "%1"=="u" goto update
if /I "%1"=="tree" goto tree
if /I "%1"=="verify" goto verify
if /I "%1"=="compile" goto compile
if /I "%1"=="cleanrun" goto cleanrun
if /I "%1"=="help" goto help

echo Invalid command.
goto help


:run
echo Starting Spring Boot...
call mvnw spring-boot:run
goto end


:clean
echo Cleaning project...
call mvnw clean
goto end


:compile
echo Compiling project...
call mvnw compile
goto end


:test
echo Running tests...
call mvnw test
goto end


:package
echo Packaging project...
call mvnw package
goto end


:install
echo Installing into local Maven repository...
call mvnw install
goto end


:verify
echo Verifying project...
call mvnw verify
goto end


:dependency
echo Downloading dependencies...
call mvnw dependency:resolve
goto end


:tree
echo Showing dependency tree...
call mvnw dependency:tree
goto end


:update
echo Updating dependencies...
call mvnw clean install -U
goto end


:version
call mvnw -version
goto end


:cleanrun
echo Cleaning and Running...
call mvnw clean spring-boot:run
goto end


:help
echo.
echo ===========================================
echo        Spring Boot Helper Commands
echo ===========================================
echo.
echo run.bat s           - Run Spring Boot
echo run.bat run         - Run Spring Boot
echo run.bat clean       - Clean project
echo run.bat compile     - Compile source
echo run.bat test        - Run tests
echo run.bat package     - Create JAR
echo run.bat install     - Install to local Maven
echo run.bat verify      - Verify project
echo run.bat dependency  - Download dependencies
echo run.bat tree        - Dependency tree
echo run.bat update      - Force dependency update
echo run.bat version     - Maven version
echo run.bat cleanrun    - Clean and Run
echo run.bat help        - Show this menu
echo.

:end
endlocal