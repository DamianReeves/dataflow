package com.damianreeves.dataflow.agents.internal

import com.damianreeves.dataflow.agents.AgentContext
import monix.reactive.Observer

class AgentContextImpl[-A](observer: Observer[A]) extends AgentContext[A] {
  override def send(message: A): Unit = observer.onNext(message)
}
