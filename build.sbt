name := "Direct Follower Matrix"

version := "0.2"

scalaVersion := "2.12.8"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"

Test / parallelExecution := false