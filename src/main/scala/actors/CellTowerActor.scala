package actors

import akka.actor.{Actor, ActorRef, Props}
import model.{Constants, Variables}
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Thagus on 15/11/16.
  */
case class MakeCall(userID: Integer, receivingUserID: Integer, receivingCell: ActorRef)
case class ReceiveCall(userID: Integer)
case class EndCall(userID: Integer, receivingUserID: Integer, receivingCell: ActorRef)
case class EndReceivingCall(userID: Integer)

class CellTowerActor (id: Integer) extends Actor{
  @volatile
  private var totalCalls = 0
  implicit val timeout = Timeout(10 seconds)

  def receive = {
    case MakeCall(userID, receivingUserID, receivingCell) => {
      //Check if we can make a call
      if(Constants.CELL_CALL_LIMIT>=totalCalls+1) {
        //Reserve the call
        totalCalls += 1

        val future: Future[Int] = ask(receivingCell, ReceiveCall(receivingUserID)).mapTo[Int]
        future.onSuccess {
          case 1 => {
            //Receiving cell is ready to receive
            println("Starting call for " + userID + " call on cell " + id + ". Total calls now: " + totalCalls)

            Variables.successfullyStartedCalls += 1
            //Make the call
            context.actorOf(Props[CallHandlerActor]) ! ProcessCall(userID, receivingUserID, receivingCell)
          }
          case 0 => {
            //Release the reservation
            totalCalls -= 1
            println("Call blocked for user " + userID + " in cell " + id + ". Tried to call " + receivingUserID)
          }
        }
      }
      else {
        //Call blocked on sender
        Variables.senderCellBlockedCalls += 1
        println("Sender call blocked for user " + userID + " in cell " + id + ". Tried to call " + receivingUserID)
      }
    }
    case ReceiveCall(userID) => {
      if(Constants.CELL_CALL_LIMIT>=totalCalls+1) {
        totalCalls += 1
        println("Receiving call for " + userID + " on cell " + id + ". Total calls now: " + totalCalls)
        sender ! 1
      }
      else{
        Variables.receiverCellBlockedCalls += 1 //Call is blocked
        println("Received call blocked for user " + userID + " in cell " + id + ". Calls: " + totalCalls)
        sender ! 0
      }
    }
    case EndCall(userID, receivingUserID, receivingCell) => {
      totalCalls -= 1
      Variables.successfullyEndedCalls += 1
      receivingCell ! EndReceivingCall(receivingUserID)
      println("Ending call of user " + userID + " call on cell " + id + ". Total calls now: " + totalCalls)
    }
    case EndReceivingCall(receivingUserID) => {
      totalCalls -= 1
      println("Ending received call of user" + receivingUserID + " call on cell " + id + ". Total calls now: " + totalCalls)
    }
    case _ => println("Error: message not recognized")
  }
}
