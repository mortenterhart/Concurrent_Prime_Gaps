@echo off

REM  Enable command line extensions (IF DEFINED statement)
SETLOCAL ENABLEEXTENSIONS

REM  If JAVA environment variable not defined, search for the
REM  java executable
IF NOT DEFINED JAVA (
    FOR /f %%v IN ('WHERE java') DO (
        SET JAVA=%%v
    )
)

"%JAVA%\bin\java" -server -Xms4G -Xmx4G -XX:MaxDirectMemorySize=1024M ^
-XX:NewSize=1G -XX:MaxNewSize=1G -XX:+UseParNewGC -XX:MaxTenuringThreshold=2 ^
-XX:SurvivorRatio=8 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=32768 ^
-classpath "build" main.Application
PAUSE
