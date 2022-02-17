package models

import models.Types._

/** Represents a transition from one activity to another activity
 * along with its total count of the occurrences in a process */
case class Transition(from: Activity, to: Activity, count: Count)

object Transition {
  /** Aggregate the count of transitions by their `from` and `to` activity */
  def aggregate(ts: Seq[Transition]): Seq[Transition] = {
    ts.groupBy(e => e.from -> e.to)
      .mapValues(t => t.head.copy(count = t.map(_.count).sum))
      .values.toSeq
  }
}