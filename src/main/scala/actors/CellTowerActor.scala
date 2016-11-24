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
      println("Starting call for ", userID," call on cell ",id, ". Total calls now: ",totalCalls)
    }
    case EndCall(userID) => {
      totalCalls -= 1
      println("Ending call of user", userID," call on cell ",id, ". Total calls now: ",totalCalls)
    }
    case _ => println("Error: message not recognized")
  }
}
