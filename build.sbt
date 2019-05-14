name := """smsc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  javaCore,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "commons-codec" % "commons-codec" % "1.10",
  "commons-io" % "commons-io" % "2.2",
  "commons-collections" % "commons-collections" % "3.2.1",
  "org.apache.commons" % "commons-csv" % "1.5",
  "com.cloudhopper" % "ch-smpp" % "5.0.9",
  "com.cloudhopper" % "ch-commons-util" % "6.0.2",
  "com.cloudhopper" % "ch-commons-charset" % "3.0.2",
  "it.sauronsoftware.cron4j" % "cron4j" % "2.2.5",
  "io.netty" % "netty" % "3.9.6.Final",
  "org.slf4j" % "slf4j-api" % "1.7.13"
)
