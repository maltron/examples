if [ $# -eq 0 ]; then
   echo "Plase supply WildFly's directory"
   exit 1
fi
cd  ${1}
mkdir modules/system/layers/base/org/apache/activemq/main
cd modules/system/layers/base/org/apache/activemq/main
wget http://repo1.maven.org/maven2/org/apache/activemq/activemq-rar/5.12.1/activemq-rar-5.12.1.rar
unzip activemq-rar-5.12.1.rar
cd ${1}
mkdir -p modules/com/mysql/main/
cd modules/com/mysql/main/
wget http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.37.tar.gz
tar zxvf mysql-connector-java-5.1.37.tar.gz
mv mysql-connector-java-5.1.37/mysql-connector-java-5.1.37-bin.jar mysql.jar
rm -rf mysql-connector-java-5.1.37
rm mysql-connector-java-5.1.37.tar.gz
export JBOSS_HOME=${1}
${1}/bin/standalone.sh

