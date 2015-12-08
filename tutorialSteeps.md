# installSpark
## Download and install spark-1.5.1-bin-hadoop2.6
https://www.apache.org/dist/spark/spark-1.5.1/spark-1.5.1-bin-hadoop2.6.tgz
download and unzip

# startSpark
cd spark-1.5.1-bin-hadoop2.6
cd 	./sbin/start-all.sh
# installKafka
## Download and install kafka_2.10-0.8.2.2
https://www.apache.org/dyn/closer.cgi?path=/kafka/0.8.2.2/kafka_2.10-0.8.2.2.tgz
## Download and unzip
# statrKafka
./bin/zookeeper-server-start.sh config/zookeeper.properties
./bin/kafka-server-start.sh config/server.properties

createTheTopic transito
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic transito
submitTheApp to test
cd transitobh-scala-spark
./run.sh

cd transitobh-scala-spark
./run_clutes.sh