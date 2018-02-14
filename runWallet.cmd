set JAVA_HOME=%cd%\jre
echo %JAVA_HOME%
set PATH=%PATH%;%JAVA_HOME%\bin
echo %PATH%
java -jar hardware.wallet-0.0.1-POC.jar