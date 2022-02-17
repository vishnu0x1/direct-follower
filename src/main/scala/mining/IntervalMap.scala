package mining

import models.Types.Implicits._
import models.Types._

import scala.collection.SortedMap
import scala.collection.immutable.TreeMap
import scala.math.Ordering

/** A data structure backed by an immutable `TreeMap` where the key is an `Interval` */
private [mining]
case class IntervalMap[T](nodes: SortedMap[Interval, T]) {

  /** Create a projection of this map enclosed within the interval `start` (inclusive)
   * and `end` (exclusive) */
  def enclosedWithin(start: DateTime, end: DateTime): IntervalMap[T] = {
    val enclosed = nodes.range(Interval(start, start), Interval(end, end))
    IntervalMap(enclosed)
  }

  /** Values of this map */
  def values: Seq[T] = nodes.values.toSeq

}

object IntervalMap {
  implicit def intervalOrdering: Ordering[Interval] =
    Ordering.Tuple2[DateTime, DateTime].on(x => (x.start, x.end))

  def apply[T](elems: Seq[(Interval, T)]): IntervalMap[T] =
    new IntervalMap(TreeMap(elems: _*))
}

/** Interval stores an upper and lower bound of time */
case class Interval(start: DateTime, end: DateTime)
