@echo off
if '%1=='## goto EVNSET
set myclasspath=.;..;../config;
for %%c in (..\lib\*.jar) do call %0 ## %%c
goto RUN

:EVNSET
set myclasspath=%myclasspath%;%2
goto END

:RUN
@echo on
java -cp %myclasspath% -Xms256m -Xmx256m -XX:MaxPermSize=256m  com.ipacs.als.server.tools.interfaces.jms.JMSMessageForwarder

:END