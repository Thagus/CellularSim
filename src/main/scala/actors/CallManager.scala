package actors

import akka.actor.{ActorSystem, Props}

/**
  * Created by Thagus on 15/11/16.
  */
object CallManager extends App{

  override def main(args : Array[String]): Unit = {
    val system = ActorSystem("System")

    for(a <- 1 to 10){
      val actor = system.actorOf(Props(new CellTowerActor(a)))
      for(b <- 1 to 10){
        actor ! MakeCall
      }
    }
  }
}
