@echo off
rem
rem ClassVisualizer start script.
rem
set DIR=%~dp0
set APP_PATH=%DIR%/clsvis.jar

rem
rem Choose java - use JAVA_HOME, if set.
rem
if "%JAVA_HOME%" == "" (
	set JAVA=java
) else (
	set JAVA=%JAVA_HOME%/bin/java
)

rem
rem Choose Look and Feel.
rem
rem No explicit settings - java default (Ocean)
rem set LAF=
rem Windows default
rem set LAF=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
rem Windows classic
rem set LAF=com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
rem modern one - Nimbus
set LAF=javax.swing.plaf.nimbus.NimbusLookAndFeel

rem
rem Final settings and run.
rem
rem Look and Feel
set OPTS=
if not "%LAF%" == "" (
	set OPTS=-Dswing.defaultlaf=%LAF%
)
rem Font settings
set OPTS=%OPTS% -Dawt.useSystemAAFontSettings=on
rem Memory
set OPTS=%OPTS% -Xms16m -Xmx1g -XX:+UseSerialGC
rem Logging
set OPTS=%OPTS% -Djava.util.logging.config.file=logging.properties

rem
rem Run.
rem
set CMD="%JAVA%" %OPTS% -jar "%APP_PATH%" %*
echo %CMD%
%CMD%
