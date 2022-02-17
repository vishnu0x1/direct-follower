package mining

import models._

import java.time.ZonedDateTime
import scala.io.Source

/**
 * Extracts the direct follower matrix from a log.
 * Parses each line of the log file as an `Event` and builds the `Trace`s from the events.
 * Then counts all direct follower relations and prints them to the command line.
 */
object DirectFollowerExtraction extends App {
  // Get the log file from the resources
  private val logFile =
    Source.fromURL(getClass.getResource("/IncidentExample.csv"))

  // parse events from the log file
  val events = logFile.getLines()
    .toSeq.tail
    .map(CsvParser.parseEvent)

  // create the traces from the events
  val traces = events.groupBy(_.traceId)
    .map((Trace.apply _).tupled)
    .toSeq

  // create the direct followers from the traces
  val directFollowers = DirectFollowerMatrix(traces)
  directFollowers.prettyPrint()

  // filter the direct followers
  val start = ZonedDateTime.parse("2016-01-04T12:00:00Z")
  val end = ZonedDateTime.parse("2016-01-06T07:00:00Z")
  directFollowers.filter(start, end).prettyPrint()
}


