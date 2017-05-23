rem 주석적기 
set  classpath=.
set  classpath=%classpath%;bin
set  classpath=%classpath%;lib/commons-dbcp-1.3.jar
set  classpath=%classpath%;lib/commons-logging-1.1.1.jar
set  classpath=%classpath%;lib/cxf-api-2.7.18.jar
set  classpath=%classpath%;lib/kxml2-2.3.0.jar
set  classpath=%classpath%;lib/log4j-1.2.5.jar
set  classpath=%classpath%;lib/log4sql.jar
set  classpath=%classpath%;lib/ojdbc14.jar


java -cp %classpath% KopisApiBatchThread

:java -cp %classpath% HelloWorld



