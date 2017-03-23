package com.example

import akka.actor.{Actor, ActorLogging, Props}
import breeze.linalg.{DenseMatrix, DenseVector}
import probability_monad.Distribution
import spire.math.{Complex, Interval}

class QubitRegister(val size: Int) extends Actor with ActorLogging {
  import QubitRegister._

  var qubits = DenseVector.fill(size, Complex(1/spire.math.sqrt(size), 0.0))

  def receive = {
    case FlipQubit(i) => qubits = pauliFlipGate * qubits
    case Observe =>
      val p = Distribution.uniform.sample(1).head
      qubits.toScalaVector().zipWithIndex.find {
        case (z, i) =>
          val lower = if (i > 0) qubits.toScalaVector.take(i-1).foldLeft(0.0)((acc, z_i) => acc + z_i.abs) else 0.0
          Interval.openUpper(lower, lower + z.abs).contains(p)
        case _ => false
      }.head
  }
}

object QubitRegister {

  val identity = DenseMatrix.eye(256)
  val pauliFlipGate = DenseMatrix(identity(255 to 0))

  val props = Props[QubitRegister]
  case class FlipQubit(bit: Int)
  case object Observe

}