package madd

import chisel3._
import chisel3.util._

// interface(input, output)
// Vec: vector in Chisel
// SInt: signed integer, UInt: unsigned integer
// 32: 32 bits, W: width
class MatrixMul1IO(M: Int, N: Int, P: Int) extends Bundle {
  val a = Input(Vec(M * N, SInt(32.W)))
  val b = Input(Vec(N * P, SInt(32.W)))

  val out = Output(Vec(M * P, SInt(32.W)))

  override def cloneType =
    new MatrixMul1IO(M, N, P).asInstanceOf[this.type]
}
