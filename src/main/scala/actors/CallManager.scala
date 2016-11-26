package actors

import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable

/**
  * Created by Thagus on 15/11/16.
  */
case class PlaceCall(cellID: Integer, userID: Integer)

class CallManager extends Actor{
  @volatile
  var cellRegistry = new mutable.HashMap[Integer, ActorRef]

  override def receive = {
    case PlaceCall(cellID, userID) => {
      //Find the actor for the cell, or create it if none exists
      val senderCell = cellRegistry.getOrElseUpdate(cellID, context.actorOf(Props(new CellTowerActor(cellID))))

      //Send the message of MakeCall to the tower actor with the user id
      senderCell ! MakeCall(userID)
    }
  }
}
