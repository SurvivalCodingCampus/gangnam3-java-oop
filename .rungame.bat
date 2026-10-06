@echo off
set "JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d C:\java\gangnam3-java-oop
call gradlew.bat -I .run-init.gradle run
