package mining

import org.scalatest.LoneElement._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import java.time.{Instant, ZoneId}

class IntervalMapSpec extends AnyFlatSpec {

  "IntervalMap" should "not coalesce overlapping intervals" in {
    val tree = IntervalMap(Seq(
      interval(3, 4) -> "i1",
      interval(3, 5) -> "i2",
      interval(3, 6) -> "i3",
      interval(4, 4) -> "i4",
      interval(4, 4) -> "i5",
      interval(4, 5) -> "i6"))

    // overlapping intervals will not be coalesced, but exact duplicates will be overwritten
    tree.nodes.size shouldBe 5
  }

  it should "find enclosing intervals" in {
    val tree = IntervalMap(Seq(
      interval(3, 7) -> "i1",
      interval(30, 70) -> "o1"))

    def enclosedWithin(start: Int, end: Int) =
      tree.enclosedWithin(epoch plusDays start, epoch plusDays end).values

    enclosedWithin(4, 5) shouldBe empty  // [3, 7] or [30, 70] is not within [4, 5]
    enclosedWithin(1, 2) shouldBe empty
    enclosedWithin(8, 9) shouldBe empty

    enclosedWithin(2, 8).loneElement shouldBe ("i1")  // [3, 7] -> "i1" is within [2, 8]
    enclosedWithin(30, 70).loneElement shouldBe ("o1")  // [30, 70] -> "o1"
  }

  val epoch = Instant.EPOCH atZone ZoneId.of("UTC")

  def interval(start: Int, end: Int) =
    Interval(epoch plusDays start, epoch plusDays end)
}
