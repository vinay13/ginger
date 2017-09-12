name := """ginger-service"""

val reactiveMongoVer = "0.11.14"
lazy val root = (project in file(".")).enablePlugins(PlayScala)

val projectVersion = "1.0.0-SNAPSHOT"

version := s"${projectVersion}"



scalaVersion := "2.11.8"
routesGenerator := InjectedRoutesGenerator
resolvers += Resolver.mavenLocal
resolvers += "Nexus" at "https://dev.mobigraph.co:2158/nexus/content/groups/m2-repo-group/"
resolvers += "Atlassian" at "https://maven.atlassian.com/repository/public"

credentials += Credentials("Nexus Repo", "https://dev.mobigraph.co:2158/nexus/content/groups/m2-repo-group/", "ruser", "n3x^$c)ncorde!")



libraryDependencies ++= Seq(
    filters, cache, ws,
    "com.typesafe.akka" %% "akka-remote" % "2.4.14",
    "org.im4java" % "im4java" % "1.4.0",
    /*"com.mobigraph.rendering" % "protobuf-messages" % "1.6.0-SNAPSHOT",*/


    //"com.mobigraph.rendering" % "xPresso-entities" %  "1.11.0-SNAPSHOT",

    "org.springframework" % "spring-core" % "4.3.3.RELEASE",
    "org.springframework" % "spring-context" % "4.3.3.RELEASE",
    "org.springframework.data" % "spring-data-mongodb" % "1.9.3.RELEASE",
    "org.springframework" % "spring-web" % "4.3.3.RELEASE",
    "org.springframework" % "spring-context-support" % "4.3.3.RELEASE",

    "org.mongodb" % "mongo-java-driver" % "3.2.2",
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client
    "com.google.oauth-client" % "google-oauth-client" % "1.22.0",

    "org.bouncycastle" % "bcprov-jdk15on" % "1.51",
    
    "com.amazonaws" % "aws-java-sdk-s3" % "1.11.73",
    "com.nimbusds" % "nimbus-jose-jwt" % "4.33",
    "org.eclipse.jetty.http2" % "http2-client" % "9.4.0.v20161208",
    "org.eclipse.jetty.http2" % "http2-http-client-transport" % "9.4.0.v20161208",
    "org.eclipse.jetty" % "jetty-alpn-client" % "9.4.0.v20161208",
 /*   "org.eclipse.jetty.alpn" % "alpn-api" % "1.1.3.v20160715",
    "org.mortbay.jetty.alpn" % "alpn-boot" % "8.1.10.v20161026",
    "io.kamon" %% "kamon-core" % "0.6.5",
    "io.kamon" %% "kamon-jmx" % "0.6.3",
    "io.kamon" %% "kamon-influxdb" % "0.6.3",
    "io.kamon" %% "kamon-system-metrics" % "0.6.5",*/
    "org.mindrot" % "jbcrypt" % "0.4",
    "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVer,
    "com.google.code.gson" % "gson" % "2.8.0",
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
    "commons-lang" % "commons-lang" % "2.6",
    "commons-io" % "commons-io" % "2.4",
    "commons-beanutils" % "commons-beanutils" % "1.9.2",
    // https://mvnrepository.com/artifact/javax.mail/javax.mail-api
    "javax.mail" % "javax.mail-api" % "1.5.5",
    // https://mvnrepository.com/artifact/javax/javaee-api
    "javax" % "javaee-api" % "7.0",

    // https://mvnrepository.com/artifact/org.quartz-scheduler/quartz
    "org.quartz-scheduler" % "quartz" % "2.3.0",





    "com.restfb" % "restfb" % "1.41.0"


    // https://mvnrepository.com/artifact/edu.uci.ics/crawler4j
    // https://mvnrepository.com/artifact/com.mohiva/play-silhouette_2.11



    // https://mvnrepository.com/artifact/com.sleepycat/je





)

