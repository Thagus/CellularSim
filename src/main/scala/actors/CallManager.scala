package actors

import akka.actor.{Actor, ActorRef, Props}

import scala.collection.immutable.HashMap

/**
  * Created by Thagus on 15/11/16.
  */
case class PlaceCall(cellID: Integer, userID: Integer)

class CallManager extends Actor{

  /*override def main(args : Array[String]): Unit = {
    val system = ActorSystem("System")

    for(a <- 1 to 10){
      val actor = system.actorOf(Props(new CellTowerActor(a)))
      for(b <- 1 to 10){
        actor ! MakeCall
      }
    }
  }*/

  val cellRegistry = new HashMap[Integer, ActorRef]

  override def receive = {
    case PlaceCall(cellID, userID) => {
      //Find the actor for the cell, or create it if none exists
      var cellTowerActor = cellRegistry.getOrElse(cellID, null)

      if(cellTowerActor == null){
        cellTowerActor = context.actorOf(Props(new CellTowerActor(cellID)))
        cellRegistry + (cellID -> cellTowerActor)
      }

      //Send the message of MakeCall to the tower actor with the user id
      cellTowerActor ! MakeCall(userID)
    }
  }
}
