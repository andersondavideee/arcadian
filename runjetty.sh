export MAVEN_OPTS=$ARCADIAN_OPTS"-Xmx256m -XX:MaxPermSize=128m -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=4000,server=y,suspend=n -Dlog4j.configuration=file:///home/franz/git-workspace/log4j.properties"
mvn clean jetty:run -Dmaven.test.skip=true
