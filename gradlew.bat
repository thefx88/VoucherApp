@echo off

:: ##########################################################################
::
::  Gradle startup script for Windows
::
:: ##########################################################################

set DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the

echo location of your Java installation.
goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the

echo location of your Java installation.
goto fail

:execute
set CMD_LINE_ARGS=
:concat
if "%1"=="" goto doneConcat
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto concat
:doneConcat

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%
goto end

:fail
set EXIT_CODE=1
if not "%OS%"=="Windows_NT" goto exit
set EXIT_CODE=%ERRORLEVEL%
:exit
exit %EXIT_CODE%

:end
