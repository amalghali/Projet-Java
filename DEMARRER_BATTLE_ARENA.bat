@echo off
echo ==========================================
echo    PREPARATION DE LA MISSION WARZONE...
echo ==========================================
cd /d "%~dp0"

REM Tentative de lancement avec le classpath complet
java -cp "target/classes;%USERPROFILE%\.m2\repository\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-controls\22.0.2\javafx-controls-22.0.2-win.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-graphics\22.0.2\javafx-graphics-22.0.2-win.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-base\22.0.2\javafx-base-22.0.2-win.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-fxml\22.0.2\javafx-fxml-22.0.2-win.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-media\22.0.2\javafx-media-22.0.2-win.jar" main.AppLauncher

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERREUR] Le lancement a echoue. 
    echo Assurez-vous d'avoir compile le projet dans IntelliJ (Build > Build Project) 
    echo avant de lancer ce script.
    pause
)
