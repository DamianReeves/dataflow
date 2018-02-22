package com.damianreeves.dataflow.agents

import monix.eval.Task

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn
import scala.util.{Failure, Success}

object AgentUsage extends scala.App {
  import monix.execution.Scheduler.Implicits.{global => scheduler}

  sealed trait CounterMessage
  final case object IncrementCounter extends CounterMessage
  final case object DecrementCounter extends CounterMessage

  val agentRef = Agent[CounterMessage,Long](0)(state => {
    case IncrementCounter => Task {state + 1}
    case DecrementCounter => Task {state -1 }
  }).run


  scheduler.execute(new Runnable {
    override def run(): Unit = {
      var continue = true
      do {
        println("Press I to increment, D to decrement, or Q to quit!")
        val input = StdIn.readLine()
        continue = input match {
          case "I"|"i" =>
            agentRef.tell(IncrementCounter); true
          case "D"|"d" => agentRef.tell(DecrementCounter); true
          case "Q"|"q" => false
          case _ => true
        }
      }while(continue)
      agentRef.shutdown()
    }
  })

  val shutdownHandle = agentRef.whenShutdown.andThen{
    case Success(value) => println(s"Final counter value: $value")
    case Failure(ex) => println(s"Error encountered: $ex")
  }

  Await.ready(shutdownHandle, Duration.Inf)
}
