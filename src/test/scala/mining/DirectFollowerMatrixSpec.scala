package mining

import models.{Event, Trace}
import org.scalatest.LoneElement._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import java.time.{Instant, ZoneId}

class DirectFollowerMatrixSpec extends AnyFlatSpec {

  "DirectFollowerMatrix" should "extract the direct follower activity count" in {
    val t1 = trace("t1", Seq("a" -> 1, "b" -> 2, "c" -> 3, "d" -> 4))  // a -> b -> c -> d
    val t2 = trace("t2", Seq("b" -> 3, "c" -> 4, "d" -> 5))  // b -> c -> d
    val t3 = trace("t3", Seq("c" -> 5, "d" -> 6))  // c -> d
    val matrix = DirectFollowerMatrix(Seq(t1, t2, t3))

    // activity count should contain the aggregated count
    matrix.activityCount should contain theSameElementsAs Seq(
      ("a", "b") -> 1,
      ("b", "c") -> 2,
      ("c", "d") -> 3)
  }

  it should "merge traces with same start and end date" in {
    val t1 = trace("t1", Seq("a" -> 1, "b" -> 2, "c" -> 3, "d" -> 4))
    val t2 = trace("t2", Seq("b" -> 1, "c" -> 4))
    val matrix = DirectFollowerMatrix(Seq(t1, t2))

    // t1 and t2 belong to the same interval [1, 4]
    matrix.traces.values.loneElement shouldBe Seq(t1, t2)
  }

  it should "filter the direct followers by time range" in {
    val t1 = trace("t1", Seq("a" -> 1, "b" -> 2, "c" -> 3, "d" -> 4, "e" -> 5))
    val t2 = trace("t2", Seq("b" -> 3, "c" -> 4, "d" -> 5))
    val t3 = trace("t3", Seq("c" -> 4, "d" -> 5))
    val t4 = trace("t4", Seq("a" -> 1, "b" -> 2))
    val t5 = trace("t5", Seq("b" -> 5, "d" -> 6))

    // extract only traces within the interval [3, 5]
    val matrix = DirectFollowerMatrix(Seq(t1, t2, t3, t4, t5))
      .filter(dt(3), dt(5))

    // only t2 and t3 which are enclosed within [3, 5] should be aggregated
    matrix.activityCount should contain theSameElementsAs Seq(
      ("b", "c") -> 1,
      ("c", "d") -> 2)
  }

  val now = Instant.now().atZone(ZoneId.systemDefault())

  def dt(days: Int) = now plusDays days

  def trace(traceId: String, activities: Seq[(String, Int)]): Trace = {
    val events = activities.map { x =>
      Event(traceId, activity = x._1, start = dt(x._2))
    }
    Trace(traceId, events)
  }
}
