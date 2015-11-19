jboss-cli.sh --connect --command="undeploy attendee-2.0.war"
jboss-cli.sh --connect --command="undeploy event-2.0.war"
jboss-cli.sh --connect --command="undeploy organizer-2.0.war"
jboss-cli.sh --connect --command=":reload"
