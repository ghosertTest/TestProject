echo off

setlocal
echo ----------------------
echo Build the project
echo ----------------------

set JAVA_HOME=C:\Progra~1\Java\j2re1.4.2_04

if "%JAVA_HOME%" == "" goto error

set LOCALCLASSPATH=.;.\lib\swt.jar;.\bin\

echo %LOCALCLASSPATH%

%JAVA_HOME%\bin\javaw.exe -Djava.library.path="lib" -cp %LOCALCLASSPATH% loadtrend.swt.mobile.MobileApplication

goto end

:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end

endlocal