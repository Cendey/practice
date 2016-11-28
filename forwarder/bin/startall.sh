MY_CLASSPATH=".:..:../config"
export MY_CLASSPATH
for jar in `ls ../lib/*.jar`
do MY_CLASSPATH=$MY_CLASSPATH:$jar
done
export MY_CLASSPATH

nohup java -Dsvr=JOBSCHEDULER -cp $MY_CLASSPATH -Xms256m -Xmx256m -XX:MaxPermSize=256m com.ipacs.als.gui.plugins.tools.setup.jobschedule.serverjobschedule2.poll.ServerJobStartAllForUnix &