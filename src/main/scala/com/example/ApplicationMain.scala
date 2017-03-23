package com.example

import akka.actor.ActorSystem

object ApplicationMain extends App {
  val system = ActorSystem("qubits")
  val qubitRegister = system.actorOf(QubitRegister.props, "qubitRegister")
  // This example app will ping pong 3 times and thereafter terminate the ActorSystem -
  // see counter logic in PingActor
  system.awaitTermination()
}