package mining

import models.Transition.aggregate
import models.Types._
import models._

/** Holds the direct followers of the activities in a list of traces.
 *
 * The traces are internally sorted by their start time and end time
 * allowing them to be searchable by a given time range. The transitions
 * of a trace will be computed only once lazily and shared across all
 * instances created by `filter`. The activity count from traces will
 * be computed once for each new instance. This class is immutable. */
case class DirectFollowerMatrix private(
    activities: Seq[Activity],
    traces:     IntervalMap[Seq[Trace]]) {

  // extract the direct follower activity count from the transitions
  private[mining]
  lazy val activityCount = {
    val ts = traces.values.flatten.flatMap(_.transitions)
    aggregate(ts)
      .map(t => (t.from, t.to) -> t.count)
      .toMap withDefaultValue 0
  }

  /** Filter the logs by a given time range */
  def filter(start: DateTime, end: DateTime): DirectFollowerMatrix = {
    copy(traces = traces.enclosedWithin(start, end))
  }

  /** Print the direct followers activity matrix to console
   *
   * The value in the matrix corresponding to a row `a` and column `b`
   * represents the count of the occurrence of a follower relation
   * from activity `a` to activity `b`
   *
   * @note Current implementation truncates to a 20 by 20 activity matrix */
  def prettyPrint(): Unit = {
    //// print legend
    val legend = activities zip "abcdefghijklmnopqrst"
    val legendStr = legend.map(x => s"${x._2} => ${x._1}").mkString("\n")
    println(
      s"""
         |Legend:
         |${legendStr}
         |""".stripMargin)

    //// print only column header
    print(" ")
    for ((_, l) <- legend)
      print(f"${l}%10c")

    //// print rows with row header
    for ((a1, l1) <- legend) {
      print(f"\n${l1}%c")
      for ((a2, _) <- legend) {
        val c = activityCount(a1, a2)
        print(f"${c}%10d")
      }
    }
    println()
  }
}

object DirectFollowerMatrix {
  def apply(ts: Seq[Trace]): DirectFollowerMatrix = {
    val activities = ts.flatMap(_.events.map(_.activity)).distinct
    val traces = IntervalMap(ts.groupBy(t => Interval(t.start, t.end)).toSeq)
    new DirectFollowerMatrix(activities, traces)
  }
}
