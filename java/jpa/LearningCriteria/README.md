How to setup environment on WildFly 8 to run this application:

Step #1: Copy a configuration file at <wildfly directory>/standalone/configuration/standalone.xml 
         to a different file: <wildfly directory>/standalone/configuration/standalone-example.xml
Step #2: Start Wildfly using the configuration file set on step #1
         <wildfly directory>/bin/standalone.sh -c standalone-example.xml
Step #3: (Assuming the Module for a Database, in my particular case, MySQL is added as a module and named: MySQL), then run the command on bin directory:
         bin/datasource-setup.sh
