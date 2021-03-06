name := "pirate"

version := "0.1.2"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.9.2", "2.9.3", "2.10.4", "2.11.2")

organization := "com.mosesn"

libraryDependencies <++= scalaVersion {
  case version if version startsWith "2.9." =>
    Seq("org.scalatest" %% "scalatest" % "1.9.2" % "test")
  case version if version startsWith "2.10." =>
    Seq("org.scalatest" %% "scalatest" % "2.2.1" % "test")
  case version if version startsWith "2.11." =>
    Seq(
      "org.scalatest" %% "scalatest" % "2.2.1" % "test",
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2"
    )
}

libraryDependencies += "junit" % "junit" % "4.11" % "test"

scalacOptions += "-deprecation"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

publishArtifact in Test := false

pomExtra := (
  <url>http://github.com/mosesn/pirate</url>
  <licenses>
    <license>
      <name>MIT License</name>
      <url>http://opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:mosesn/pirate.git</url>
    <connection>scm:git:git@github.com:mosesn/pirate.git</connection>
  </scm>
  <developers>
    <developer>
      <id>mosesn</id>
      <name>Moses Nakamura</name>
      <url>http://github.com/mosesn</url>
    </developer>
  </developers>)
