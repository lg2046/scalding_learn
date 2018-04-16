organization := "com.jd"

name := "scalding_learn"

version := "1.0"

resolvers += "Local Maven Repository" at "http://conjars.org/repo"

libraryDependencies ++= Seq(
  "cascading" % "cascading-core" % "2.7.1",
  "cascading" % "cascading-local" % "2.7.1",
  "cascading" % "cascading-hadoop2-mr1" % "2.7.1",

  "com.twitter" %% "scalding-core" % "0.17.4" exclude("cascading", "cascading-local") exclude("cascading", "cascading-hadoop"),
  //"com.twitter" %%  "scalding-json"       % "0.17.4" exclude( "cascading", "cascading-local" ) exclude( "cascading", "cascading-hadoop" )
  //"com.twitter" %%  "scalding-avro"       % "0.17.4" exclude( "cascading", "cascading-local" ) exclude( "cascading", "cascading-hadoop" )

  "org.apache.hadoop" % "hadoop-common" % "2.7.5" % "provided",
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.5" % "provided"
)

offline := true

assemblyExcludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  val excludes = Set(
    "jsp-api-2.1.jar",
    "jsp-2.1.jar",
    "janino-2.7.5.jar", // Janino includes a broken signature, and is not needed anyway
    "commons-beanutils-core-1.8.0.jar" // Clash with each other and with commons-collections
  )
  cp filter { jar => excludes(jar.data.getName) }
}
