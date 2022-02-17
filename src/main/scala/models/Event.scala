package models

import Types._

/** Represents an activity logged in a process */
case class Event(traceId: TraceId, activity: Activity, start: DateTime)