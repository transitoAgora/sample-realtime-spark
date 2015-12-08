import AssemblyKeys._ // put this at the top of the file,leave the next line blank

assemblySettings

name := "transitobh-spark"

version := "0.0.1"

scalaVersion := "2.10.3"
resolvers ++= Seq(
  "twitter4j" at "http://twitter4j.org/maven2",
  "maven2_search" at "http://repo.maven.apache.org/maven2",
  "typesafe repo"      at "http://repo.typesafe.com/typesafe/releases/",
  "bintrayRepo" at "https://dl.bintray.com/actor/maven"
 )
// additional libraries
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.3.1" % "provided",
  "org.apache.spark" %% "spark-streaming" % "1.3.1",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.3.1",
  "org.twitter4j" % "twitter4j-core" % "3.0.0-SNAPSHOT",
  "org.twitter4j" % "twitter4j-stream" % "3.0.0-SNAPSHOT",
  "org.apache.kafka" % "kafka_2.10" % "0.8.2.1"

)

  // Drop these jars
    excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
      
      val excludes = Set(
        "junit-4.5.jar", // We shouldn't need JUnit
        "jsp-api-2.1-6.1.14.jar",
        "jsp-2.1-6.1.14.jar",
        "javax.websocket-api-1.1.jar",
        "jasper-compiler-5.5.12.jar",
        "minlog-1.2.jar", // Otherwise causes conflicts with Kyro (which bundles it)
        "janino-2.5.16.jar", // Janino includes a broken signature, and is not needed anyway
        "commons-beanutils-core-1.8.0.jar", // Clash with each other and with commons-collections
        "commons-beanutils-1.7.0.jar",      // "
        "hadoop-core-0.20.2.jar", // Provided by Amazon EMR. Delete this line if you're not on EMR
        "hadoop-tools-0.20.2.jar",
        "guava-14.0.1.jar", // conflict spark-network-common_2.10-1.3.0.jar
        "jcl-over-slf4j-1.7.10.jar", //conflict commons-logging-1.1.3.jar
        "hadoop-yarn-api-2.2.0.jar"
      )
      /*
      val excludes = Set(
        "twitter4j-core-3.0.0-SNAPSHOT.jar",
        "twitter4j-stream-3.0.0-SNAPSHOT.jar",
        "tyrus-standalone-client-1.8.3.jar"
      )
      */
      cp filter { jar => excludes(jar.data.getName) }
    }

    mergeStrategy in assembly <<= (mergeStrategy in assembly) {
      (old) => {
        case x if x.contains("UnusedStubClass.class") => MergeStrategy.first
        case x if x.endsWith("project.clj") => MergeStrategy.discard // Leiningen build files
        case x if x.startsWith("META-INF") => MergeStrategy.discard // More bumf
        case x if x.endsWith(".html") => MergeStrategy.discard
        case x => MergeStrategy.first
      }
    }
