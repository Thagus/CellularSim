package actors

import java.util.concurrent.TimeUnit

import akka.actor.Actor

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Thagus on 15/11/16.
  */
case class ProcessCall(userID: Integer)

class CallHandlerActor extends Actor{
  def receive = {
    case ProcessCall(userID) => {
      val r = scala.util.Random

      context.system.scheduler.scheduleOnce(Duration.create(r.nextInt(7), TimeUnit.SECONDS), sender, EndCall(userID))
    }
    case _ => println("Error: message not recognized")
  }
}
