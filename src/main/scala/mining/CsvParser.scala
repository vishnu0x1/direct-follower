package mining

import models.Event

import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import scala.util.{Failure, Success, Try}

/** Parser to parse the contents of a log file */
object CsvParser {
  private val dateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")   // Example: 2016/01/04 12:09:44.000

  private def splitLine(line: String) = Try {
    line.trim.split(",") match {
      case Array(t, a, s, _*) => (t, a, s)
    }
  }

  private def parseDateTime(dt: String) = Try {
    LocalDateTime.parse(dt, dateTimeFormatter)
      .atZone(ZoneId.of("UTC"))
  }

  /** Parse an event from a line in csv format */
  def parseEvent(line: String): Event = {
    val event = for {
      (traceId, activity, s) <- splitLine(line)
      start <- parseDateTime(s)
    } yield Event(traceId, activity, start)

    event match {
      case Success(x) => x
      case Failure(e) => throw new IllegalArgumentException(
        s"Unable to parse csv, line: [$line]", e)
    }
  }
}
