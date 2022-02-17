package models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import java.time.ZonedDateTime

class TraceSpec extends AnyFlatSpec {

  "Trace" should "sort its events by start time" in {
    val e1 = Event("t1", "a1", ZonedDateTime.now())
    val e2 = Event("t1", "a2", ZonedDateTime.now() minusDays 1)
    val e3 = Event("t1", "a2", ZonedDateTime.now() plusDays 1)

    val trace = Trace("t1", Seq(e1, e2, e3))
    trace.events shouldBe Seq(e2, e1, e3)
  }

  it should "compute transitions from events" in {
    // a1 -> a2 -> a3 -> a1 -> a2
    val e1 = Event("t1", "a1", ZonedDateTime.now())
    val e2 = Event("t1", "a2", ZonedDateTime.now() plusDays 1)
    val e3 = Event("t1", "a3", ZonedDateTime.now() plusDays 2)
    val e4 = Event("t1", "a1", ZonedDateTime.now() plusDays 3)
    val e5 = Event("t1", "a2", ZonedDateTime.now() plusDays 4)
    val trace = Trace("t1", Seq(e1, e2, e3, e4, e5))

    trace.transitions should contain theSameElementsAs Seq(
      Transition("a1", "a2", 2),
      Transition("a2", "a3", 1),
      Transition("a3", "a1", 1))
  }

}
