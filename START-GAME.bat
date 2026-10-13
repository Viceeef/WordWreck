@echo off
setlocal EnableDelayedExpansion

rem Compile and run the same JavaFX game from the source in demo.
rem The bundled JavaFX SDK needs JDK 24 or newer. Maven is not required.
set "PROJECT=%~dp0demo"
set "FX_LIB=%~dp0javafx-sdk-26.0.2\lib"
set "OUTPUT=%PROJECT%\target\run-classes"
set "SOURCE_LIST=%PROJECT%\target\run-sources.txt"

where javac >nul 2>&1
if errorlevel 1 (
    echo Java Development Kit not found. Install JDK 24 or newer and add it to PATH.
    pause
    exit /b 1
)
if not exist "%FX_LIB%\javafx.controls.jar" (
    echo The bundled JavaFX SDK is missing from this folder.
    pause
    exit /b 1
)

if not exist "%OUTPUT%" mkdir "%OUTPUT%"
if errorlevel 1 goto build_failed

rem A response file lets javac compile every source file without a long command line.
(for /R "%PROJECT%\src\main\java" %%F in (*.java) do (
    set "JAVA_SOURCE=%%~fF"
    echo "!JAVA_SOURCE:\=/!"
)) > "%SOURCE_LIST%"
javac --module-path "%FX_LIB%" --add-modules javafx.controls,javafx.fxml,javafx.media -d "%OUTPUT%" @"%SOURCE_LIST%"
if errorlevel 1 goto build_failed

rem Put FXML, CSS, the font, word list, and sounds beside the compiled classes.
xcopy "%PROJECT%\src\main\resources\*" "%OUTPUT%\" /E /I /Y /Q >nul
if errorlevel 1 goto build_failed

java --enable-native-access=javafx.graphics,javafx.media --module-path "%FX_LIB%;%OUTPUT%" -m com.example/com.example.App
if errorlevel 1 (
    echo The game could not start. Check the error above.
    pause
    exit /b 1
)
exit /b 0

:build_failed
echo The game could not compile or copy its resources. Check the error above.
pause
exit /b 1
