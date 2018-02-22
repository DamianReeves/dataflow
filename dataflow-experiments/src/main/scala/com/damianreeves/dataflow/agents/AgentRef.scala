package com.damianreeves.dataflow.agents

import monix.reactive.Observer

import scala.concurrent.Future

/**An [[AgentRef]] is a handle to an agent */
trait AgentRef[-M,+S] {
  def tell(message:M)
  def shutdown():Future[S]
  def whenShutdown:Future[S]
}

object AgentRef {
  def apply[M,S](producer:Observer[M], shutdownFuture:Future[S]):AgentRef[M,S] =
    new ObservableAgentRef[M,S](producer,shutdownFuture)
}