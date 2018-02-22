package com.damianreeves.dataflow.agents

import monix.reactive.Observer

import scala.concurrent.Future

private[agents]
final class ObservableAgentRef[M,S](producer:Observer[M], val whenShutdown:Future[S]) extends AgentRef[M,S] {
  override def tell(message: M): Unit = producer.onNext(message)
  override def shutdown(): Future[S] = {
    producer.onComplete()
    whenShutdown
  }
}
