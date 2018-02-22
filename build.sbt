name := "dataflow"
description := "A project focused on providing dataflow functionality"
val scalacheckVersion = "1.13.5"
val scalaTestVersion = "2.2.6"

val commonSettings = Seq(
  organization := "damianreeves.com",
  //scalaVersion := "2.12.4",
   scalaVersion := "2.11.11",
  crossScalaVersions := Seq("2.11.11", "2.12.4"),
  resolvers ++= Seq(
    "bintray/non" at "http://dl.bintray.com/non/maven",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  scalacOptions ++= Seq("-target:jvm-1.8", "-deprecation", "-feature", "-unchecked", "-Ypartial-unification"),
  libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % scalacheckVersion % Test,
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  )
)

lazy val root: Project = project.in(file(".")).settings(
  commonSettings
).aggregate(
  dataflowCore,
  dataflowMonix,
  dataflowAkka,
  dataflowExperiments
)

lazy val dataflowCore = (project in file("dataflow-core")).settings(
  moduleName := "dataflow-core",
  commonSettings
)

lazy val dataflowMonix = (project in file("dataflow-monix")).settings(
  moduleName := "dataflow-monix",
  commonSettings
).dependsOn(
  dataflowCore
)

lazy val dataflowAkka = (project in file("dataflow-akka")).settings(
  moduleName := "dataflow-akka",
  commonSettings
).dependsOn(
  dataflowCore
)

lazy val dataflowExperiments = (project in file("dataflow-experiments")).settings(
  moduleName := "dataflow-experiments",
  commonSettings
).dependsOn(
  dataflowCore
)
