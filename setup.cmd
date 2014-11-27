@rem setup.cmd for OS/2
@echo off
cd om 
echo compiling object model...
javac *.java
cd ..\lib
set CLASSPATH=..\..;%CLASSPATH%
echo compiling metaclass library...
javac *.java
cd ..\test
echo compiling tests...
javac *.java
cd ..\ex
echo compiling exercises...
javac *.java

cd ..\test
echo executing tests...
set allokay="true"

java XTest
if errorlevel 1 set allokay="false"

java MROTest
if errorlevel 1 set allokay="false"

java EMTest
if errorlevel 1 set allokay="false"

java CTSTest
if errorlevel 1 set allokay="false"

java CoopTest
if errorlevel 1 set allokay="false"

java STracedTest
if errorlevel 1 set allokay="false"

java BATest
if errorlevel 1 set allokay="false"

java BATest1
if errorlevel 1 set allokay="false"

java BATest2
if errorlevel 1 set allokay="false"

java BATest3
if errorlevel 1 set allokay="false"

java ProxyTest
if errorlevel 1 set allokay="false"

java ICTest
if errorlevel 1 set allokay="false"

java SITest
if errorlevel 1 set allokay="false"

java ABTest
if errorlevel 1 set allokay="false"

java FITest
if errorlevel 1 set allokay="false"

java NONITest
if errorlevel 1 set allokay="false"

java PMCTest
if errorlevel 1 set allokay="false"

cd ..
if %allokay% == "false" goto exit
@echo *************************
@echo ******** Success ********
@echo *************************
:exit
