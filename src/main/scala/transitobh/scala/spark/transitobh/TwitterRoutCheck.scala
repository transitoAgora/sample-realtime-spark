/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package spark.transitobh

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import spark.transitobh.model._
import spark.transitobh._;
import spark.transitobh.util._;
import java.net.URI;

import org.apache.spark.Logging
import org.apache.log4j.{Level, Logger}


/**
 * Calculates popular hashtags (topics) over sliding 10 and 60 second windows from a Twitter
 * stream. The stream is instantiated with credentials and optionally filters supplied by the
 * command line arguments.
 *
 * Run this on your local machine as
 *
 */
 object TwitterRoutCheck {
  def main(args: Array[String]) {
    /*if (args.length < 4) {
      System.err.println("Usage: TwitterPopularTags <consumer key> <consumer secret> " +
        "<access token> <access token secret> [<filters>]")
      System.exit(1)
      }*/

      setStreamingLogLevels()

    //val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
    val filters = "mtvstars" :: Nil

    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generat OAuth credentials
    System.setProperty("twitter4j.oauth.consumerKey", "...")
    System.setProperty("twitter4j.oauth.consumerSecret", "...")
    System.setProperty("twitter4j.oauth.accessToken", "...")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "...")
    val routes = Route("Gaga", 1, "Gaga") :: Route("Selena", 2, "Selena") :: Nil
    val sparkConf = new SparkConf().setAppName("TwitterRoutCheck")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    val stream = TwitterUtils.createStream(ssc, None, filters)
    val routesStreaming = stream.flatMap(status => Trafic.route(status.getText, routes ).toString ::Nil )

    val topCounts60 = routesStreaming.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(60))
    .map{case (topic, count) => (count, topic)}
    .transform(_.sortByKey(false))

    /*val topCounts10 = routesStreaming.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(10))
                     .map{case (topic, count) => (count, topic)}
                     .transform(_.sortByKey(false))
                     */


    // Print popular hashtags
    topCounts60.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
      topList.foreach{case (count, tag) => {
        println("%s (%s tweets)".format(tag, count))
        KafkaProducer("transito", "localhost:9092").send("{ \"tag\" :\"%s\", \"count\" : %s }".format(tag, count));
          //val webSocket = WebsocketClientEndpointJetty("ws://localhost:8091")
          //webSocket.send("{ \"tag\" :\"%s\", \"count\" : %s }".format(tag, count))
          //webSocket.close
        }
      }
      
      })

    /*topCounts10.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
      topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
      })*/

      ssc.start()
      ssc.awaitTermination()
    }

    def setStreamingLogLevels() {
      val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
      if (!log4jInitialized) {
      // We first log something to initialize Spark's default logging, then we override the
      // logging level.
      /*
      logInfo("Setting log level to [WARN] for streaming example." +
        " To override add a custom log4j.properties to the classpath.")
        */
      Logger.getRootLogger.setLevel(Level.WARN)
    }
  }
}
// scalastyle:on println
