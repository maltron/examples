jboss-cli.sh --connect --command="undeploy attendee-2.0.war"
jboss-cli.sh --connect --command="undeploy event-2.0.war"
jboss-cli.sh --connect --command="undeploy organizer-2.0.war"
jboss-cli.sh --connect --command=":reload"
cd ./commons
mvn clean package install
cd ../attendee
mvn clean package install wildfly:deploy
jboss-cli.sh --connect --command=":reload"
cd ../event
mvn clean package install wildfly:deploy
jboss-cli.sh --connect --command=":reload"
cd ../organizer
mvn clean package install wildfly:deploy
jboss-cli.sh --connect --command=":reload"
