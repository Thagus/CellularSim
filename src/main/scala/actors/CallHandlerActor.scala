package actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef}

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Thagus on 15/11/16.
  */
case class ProcessCall(userID: Integer, receivingUserID: Integer, receivingCell: ActorRef)

class CallHandlerActor extends Actor{
  def receive = {
    case ProcessCall(userID, receivingUserID, receivingCell) => {
      val r = scala.util.Random
      context.system.scheduler.scheduleOnce(Duration.create(r.nextInt(7)+1, TimeUnit.SECONDS), sender, EndCall(userID, receivingUserID, receivingCell))
    }
    case _ => println("Error: message not recognized")
  }
}
