package com.damianreeves.dataflow.agents
import com.damianreeves.dataflow.agents.internal.AgentContextImpl
import monix.reactive.Observer

trait AgentContext[-A] {
  def send(message:A):Unit
}

trait UntypedAgentContext extends AgentContext[Any]

object AgentContext {
  def apply[A](observer:Observer[A]):AgentContext[A] =
    new AgentContextImpl[A](observer)
}

