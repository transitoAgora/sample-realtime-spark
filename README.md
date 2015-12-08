# installSpark
## Download and install spark-1.5.1-bin-hadoop2.6
https://www.apache.org/dist/spark/spark-1.5.1/spark-1.5.1-bin-hadoop2.6.tgz
download and unzip

# startSpark
cd spark-1.5.1-bin-hadoop2.6
cd  ./sbin/start-all.sh
# installKafka
## Download and install kafka_2.10-0.8.2.2
https://www.apache.org/dyn/closer.cgi?path=/kafka/0.8.2.2/kafka_2.10-0.8.2.2.tgz
## Download and unzip
### Start Zookeeper (if it is not running)
```bash
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

### Start Kafka
```bash
./bin/kafka-server-start.sh config/server.properties
```

### Create the topic 'transito'

```bash
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic transito
```

### Submit the app to test

```bash
cd transitobh-scala-spark
./run.sh
```


### Now you can run that as a cluster

```bash
cd transitobh-scala-spark
./run_clutes.sh
```
