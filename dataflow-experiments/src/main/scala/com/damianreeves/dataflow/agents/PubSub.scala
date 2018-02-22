package com.damianreeves.dataflow.agents

import monix.execution.{Ack, Cancelable}
import monix.reactive.{Observable, Observer, Pipe}

import scala.concurrent.Future
import scala.reflect.ClassTag

trait PubSub { self:HasScheduler =>
  private val (pubEnd: Observer[Any],subEnd: Observable[Any]) = Pipe.publish[Any].multicast
  def subscribe[A:ClassTag](receive:A => Future[Ack]): Cancelable = {
    subEnd.collect{ case msg:A => msg}.subscribe(receive)
  }
  def publish[A](message:A):Unit = {
    pubEnd.onNext(message)
  }
}
