name := "weather"
 
version := "1.0" 
      
lazy val `weather` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
libraryDependencies ++= Seq( jdbc ,
  ehcache ,
  ws ,
  specs2 % Test ,
  guice,
  cacheApi ,
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.5",  //play xml
  "org.scalaj" %% "scalaj-http" % "2.3.0",     // 爬蟲
  "org.jsoup" % "jsoup" % "1.11.2",             //play json
  "com.typesafe.play" %% "play-json" % "2.6.7" )// play json)

libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.0",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.258",
  "ai.x" %% "play-json-extensions" % "0.10.0"
)


      