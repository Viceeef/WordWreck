@echo off
cd /d "%~dp0demo"
call mvn clean javafx:run
if errorlevel 1 pause
