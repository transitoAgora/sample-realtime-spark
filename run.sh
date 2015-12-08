sbt assembly
~/Downloads/spark-1.5.1-bin-hadoop2.6/bin/spark-submit \
--class spark.transitobh.TwitterRoutCheck \
--master local[4] \
/media/mateus/new2/git/RealTime/transitobh-scala-spark/target/scala-2.10/transitobh-spark-assembly-0.0.1.jar
