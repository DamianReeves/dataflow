package com.damianreeves.dataflow.agents

trait HasScheduler {
  implicit val scheduler = monix.execution.Scheduler.Implicits.global

}
