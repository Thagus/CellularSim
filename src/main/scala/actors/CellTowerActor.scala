package actors

import akka.actor.{Actor, Props}

/**
  * Created by Thagus on 15/11/16.
  */
case class MakeCall()
case class EndCall()

class CellTowerActor (id: Integer) extends Actor{
  private var totalCalls = 0
  def receive = {
    case MakeCall => {
      context.actorOf(Props[CallHandlerActor]) ! ProcessCall
      totalCalls += 1
      println("Starting one, now: ",totalCalls," call on cell ",id)
    }
    case EndCall => {
      totalCalls -= 1
      println("Ending one, now: ",totalCalls," call on cell ",id)
    }
    case _ => println("Error: message not recognized")
  }
}
