@rem $Revision: 39 $
@rem $Date: 2007-01-07 14:18:30 +0200 (Su, 07 Tam 2007) $
@rem $Author: paul.totterman $
@rem $Id: build-native.cmd 39 2007-01-07 12:18:30Z paul.totterman $

@rem @echo off
SETLOCAL

@echo Starting build at %TIME%, %DATE%

@rem %~dp0 is expanded pathname of the current script under NT
@set DEFAULT_BUILD_HOME=%~dp0
@rem get Parent Directory
@for /f %%i in ("%DEFAULT_BUILD_HOME%..") do @set DEFAULT_BUILD_HOME=%%~fi

@if exist %JAVA_HOME%/include/win32 goto java_found
@echo JAVA_HOME Not Found
:java_found

%JAVA_HOME%\bin\javah   -d intelbth -classpath build com.intel.bluetooth.BluetoothPeer
@if errorlevel 1 goto errormark

@set p="%ProgramFiles%\Microsoft Visual Studio 8\VC\bin"
@if exist %p%\VCVARS32.BAT goto vs_found

@echo Visual Studio Not Found
@goto :errormark

:vs_found
@echo Found Visual Studio %p%
@set PATH=%p%;%PATH%

@rem PATH=%DEFAULT_BUILD_HOME%\bin;%PATH%

@echo [%p%\VCVARS32.BAT]
call %p%\VCVARS32.BAT
gmake.exe -fmakefile %1 default
@if errorlevel 1 goto errormark
@echo [Build OK]
@goto endmark
:errormark
	ENDLOCAL
	echo Error in build
	pause
:endmark
ENDLOCAL