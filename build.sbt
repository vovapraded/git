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
libraryDependencies += "org.apache.commons" % "commons-vfs2" % "2.9.0"
// https://mvnrepository.com/artifact/com.github.mwiede/jsch
libraryDependencies += "com.github.mwiede" % "jsch" % "0.2.21"
// https://mvnrepository.com/artifact/org.apache.sshd/sshd-sftp
libraryDependencies += "org.apache.sshd" % "sshd-sftp" % "2.14.0"

// https://mvnrepository.com/artifact/commons-logging/commons-logging
libraryDependencies += "commons-logging" % "commons-logging" % "1.3.4"
// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.17.0"

// JSch (библиотека для работы с SFTP, требуется для VFS)
libraryDependencies += "com.jcraft" % "jsch" % "0.1.55"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.4.11", // Реализация SLF4J
  "org.slf4j" % "slf4j-api" % "2.0.9"             // SLF4J API
)
libraryDependencies ++= Seq(
  "org.springframework.integration" % "spring-integration-core" % "6.1.2",
  "org.springframework.integration" % "spring-integration-file" % "6.1.2",
  "org.springframework.integration" % "spring-integration-sftp" % "6.1.2",
  "org.springframework.integration" % "spring-integration-http" % "6.1.2",
  "org.springframework" % "spring-core" % "6.0.12",
  "org.springframework" % "spring-context" % "6.0.12" // Добавляем Spring Context
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}