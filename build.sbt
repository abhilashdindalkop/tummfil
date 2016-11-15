name := """ecommerce-backend"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.github.fge" % "json-schema-validator" % "2.2.6",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.amazonaws" % "aws-java-sdk" % "1.10.35",
  "com.mashape.unirest" % "unirest-java" % "1.4.5",
  "org.mongodb" % "mongo-java-driver" % "3.3.0",
  "org.mongodb.morphia" % "morphia" % "1.2.1",
  filters
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
