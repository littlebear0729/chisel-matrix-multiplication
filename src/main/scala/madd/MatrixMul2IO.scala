package madd

import chisel3._
import chisel3.util._

// DecoupledIO: bits, valid, ready
// in.bits.a
// in.bits.b
// in.bits.valid: Input
// in.bits.ready: Output

// Flipped: reverse

class MatrixMul2IO(M: Int, N: Int, P: Int) extends Bundle {
  val in = Flipped(DecoupledIO(new Bundle {
    val a = SInt(32.W)
    val b = SInt(32.W)
  }))

  val out = ValidIO(Vec(M * P, SInt(32.W)))

  override def cloneType =
    new MatrixMul2IO(M, N, P).asInstanceOf[this.type]
}