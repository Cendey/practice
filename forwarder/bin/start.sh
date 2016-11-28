MY_CLASSPATH=".:..:../config"
export MY_CLASSPATH
for jar in `ls ../lib/*.jar`
do MY_CLASSPATH=$MY_CLASSPATH:$jar
done
export MY_CLASSPATH
JOBSCHEDULE=`cat ../config/alsclient.properties |grep "java.naming.provider.url"|head -n 1|cut -f 2 -d "="`
export JOBSCHEDULE
nohup java -Dsvr=$JOBSCHEDULE -cp $MY_CLASSPATH -Xms256m -Xmx256m -XX:MaxPermSize=256m com.ipacs.als.gui.plugins.tools.setup.jobschedule.serverjobschedule2.poll.ServerJobConsole &
