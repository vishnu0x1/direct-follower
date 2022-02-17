package models

import java.time.ZonedDateTime

object Types {
  type TraceId = String
  type Activity = String
  type Count = Int
  type DateTime = ZonedDateTime

  object Implicits {
    implicit def zonedDateTimeOrdering: Ordering[ZonedDateTime] =
      Ordering.fromLessThan(_ isBefore _)
  }
}
