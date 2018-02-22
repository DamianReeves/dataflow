package com.damianreeves.dataflow.agents

import monix.reactive.{Consumer, Observable, Observer, Pipe}

trait AgentLike[M, S] extends HasScheduler {
  def run:AgentRef[M,S] = {
    val (input: Observer[M],mailbox: Observable[M]) = createInputChannel
    val agentConsumer = createConsumer
    val whenShutdown = mailbox.consumeWith(agentConsumer).runAsync(scheduler)
    AgentRef(input, whenShutdown)
  }

  def createConsumer:Consumer[M,S]
  protected def createInputChannel:(Observer[M],Observable[M]) = Pipe.publish[M].multicast
}






