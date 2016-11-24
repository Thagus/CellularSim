package actors

import akka.actor.{Actor, Props}

/**
  * Created by Thagus on 15/11/16.
  */
case class MakeCall(userID: Integer)
case class EndCall(userID: Integer)

class CellTowerActor (id: Integer) extends Actor{
  private var totalCalls = 0
  def receive = {
    case MakeCall(userID) => {
      context.actorOf(Props[CallHandlerActor]) ! ProcessCall(userID)
      totalCalls += 1
      println("Starting one, now: ",totalCalls," call on cell ",id)
    }
    case EndCall(userID) => {
      totalCalls -= 1
      println("Ending one, now: ",totalCalls," call on cell ",id)
    }
    case _ => println("Error: message not recognized")
  }
}
