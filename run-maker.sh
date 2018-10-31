JAVA_OPT="-Djava.net.preferIPv4Stack=true -Xms1G -Xmx1G -server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+ExplicitGCInvokesConcurrent -Xloggc:/arch/logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10  -XX:GCLogFileSize=100M "

java $JAVA_OPT -jar ./msgmaker-0.0.1-SNAPSHOT.jar $*
