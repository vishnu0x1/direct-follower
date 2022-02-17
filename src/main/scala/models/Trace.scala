package models

import models.Transition.aggregate
import models.Types.Implicits._
import models.Types._

/** Consists of multiple events that share the same case ID (`traceId`) */
case class Trace private(traceId: TraceId, events: Seq[Event]) {

  // traceId should match for all events
  assert(events.forall(_.traceId == traceId))

  /** The start time of this trace */
  val start: DateTime = events.head.start

  /** The end time of this trace */
  val end: DateTime = events.last.start

  /** The transitions of all the events in this trace */
  lazy val transitions: Seq[Transition] =
    aggregate {
      for ((e1, e2) <- events zip events.tail
           if e1.start isBefore e2.start)
        yield Transition(e1.activity, e2.activity, 1)
    }

}

object Trace {
  def apply(traceId: TraceId, events: Seq[Event]): Trace =
    new Trace(traceId, events.sortBy(_.start))
}

