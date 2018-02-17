@echo off

SET JAVA_DIRECT_EXECUTABLE_PATH=false

SET JAVA_VM_OPTIONS=-server -Xms6G -Xmx4G -XX:MaxDirectMemorySize=1024M -XX:NewSize=1G ^
-XX:MaxNewSize=1G -XX:+UseParNewGC -XX:MaxTenuringThreshold=2 ^
-XX:SurvivorRatio=8 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=32768

REM  Enable command line extensions (IF DEFINED statement)
SETLOCAL ENABLEEXTENSIONS

REM  If JAVA environment variable not defined, search for the
REM  java executable
IF NOT DEFINED JAVA (
    FOR /f %%v IN ('WHERE java') DO (
        SET JAVA=%%v
    )

    IF "%JAVA%" == "" (
       ECHO ERROR: Could not locate 'java' executable.
       ECHO Make sure a Java version is correctly installed and visible inside PATH variable.
       EXIT /B 1
    )
    SET JAVA_DIRECT_EXECUTABLE_PATH=true
)

IF "%JAVA_DIRECT_EXECUTABLE_PATH%" == "true" (
    ECHO Found java executable "%JAVA%"
    ECHO.
    ECHO Executing "%JAVA%" %JAVA_VM_OPTIONS% -classpath "build" main.Application
    "%JAVA%" %JAVA_VM_OPTIONS% -classpath "build" main.Application
) ELSE (
    ECHO Found JAVA environment variable "%JAVA%"
    ECHO.
    ECHO Executing "%JAVA%\bin\java" %JAVA_VM_OPTIONS% -classpath "build" main.Application 
    "%JAVA%\bin\java" %JAVA_VM_OPTIONS% -classpath "build" main.Application
)

PAUSE
EXIT /B 0
