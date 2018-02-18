#!/bin/bash

JAVA_HEAP_SIZE=6G
JAVA_VM_OPTIONS="-server -Xms${JAVA_HEAP_SIZE} -Xmx${JAVA_HEAP_SIZE} -XX:MaxDirectMemorySize=1024M -XX:NewSize=1G "\
"-XX:MaxNewSize=1G -XX:+UseParNewGC -XX:MaxTenuringThreshold=2 "\
"-XX:SurvivorRatio=8 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=32768";

if type -p java > /dev/null; then
   JAVA="/Library/Java/JavaVirtualMachines/jdk1.8.0_162.jdk/Contents/Home";

   "${JAVA}/bin/java" ${JAVA_VM_OPTIONS} -classpath "build" main.Application;
else
    echo "ERROR: Could not locate a 'java' executable" >&2;
    echo "Make sure a Java version is correctly installed and visible inside \$PATH variable." >&2;
    exit 1;
fi

exit 0;
