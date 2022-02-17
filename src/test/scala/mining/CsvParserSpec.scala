package mining

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

class CsvParserSpec extends AnyFlatSpec {

  "CsvParser" should "parse event from csv formatted line" in {
    val e = CsvParser.parseEvent("trace_1,Incident logging,2016/01/04 12:26:44.001,2016/01/04 12:35:44.000")
    e.traceId shouldBe "trace_1"
    e.activity shouldBe "Incident logging"
    e.start.toString shouldBe "2016-01-04T12:26:44.001Z[UTC]"
  }

}
