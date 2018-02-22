package com.damianreeves.dataflow.agents

import monix.eval.{Callback, Task}
import monix.execution.{Ack, Cancelable, Scheduler}
import monix.reactive.{Consumer, Observable, Observer, Pipe}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.io.StdIn
import scala.reflect.ClassTag
import scala.util.{Failure, Success}



/** An [[Agent]] is designed to act similar to F#'s MailboxProcessor or as a simple Actor.
  * The agent is stateful by default. */
trait Agent[M, S] extends AgentLike[M,S] {
  protected[agents] def prestart:S
  protected[agents] def receive(state:S):PartialFunction[M,Task[S]]
  protected def aroundReceive(state:S,message:M):Task[S] = receive(state)(message)

  def createConsumer:Consumer[M,S] = Consumer.foldLeftAsync(prestart)((state,msg:M)=>aroundReceive(state,msg))
}

object Agent {
  def apply[M,S](initialState:S)(evolve:S => PartialFunction[M,Task[S]]): Agent[M,S] = new Agent[M,S] {
    override def prestart: S = initialState
    override def receive(state: S): PartialFunction[M, Task[S]] = evolve(state)
  }

  def run[M,S](initialState:S)(evolve:S => PartialFunction[M,Task[S]]): AgentRef[M,S] = {
    val agent = Agent(initialState)(evolve)
    agent.run
  }
}

