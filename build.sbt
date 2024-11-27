ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "github",
    Compile / packageBin / mainClass := Some("AppRunner") // Укажите ваш главный класс здесь
  )

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.3",
  "io.circe" %% "circe-generic" % "0.14.3",
  "io.circe" %% "circe-parser" % "0.14.3",

)
// https://mvnrepository.com/artifact/junit/junit
// https://mvnrepository.com/artifact/junit/junit
//libraryDependencies += "junit" % "junit" % "4.13.2" % Test
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.11.3" % Test
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-engine" % "5.11.3" % Test

// https://mvnrepository.com/artifact/com.github.scopt/scopt

