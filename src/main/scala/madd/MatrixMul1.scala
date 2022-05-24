package madd

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselStage, ChiselGeneratorAnnotation}

class MatrixMul1(M: Int, N: Int, P: Int) extends Module {
  val io = IO(new MatrixMul1IO(M, N, P))

  io.out := DontCare

  for (i <- 0 until M) {
    for (j <- 0 until P) {
      var sum = 0.S(32.W)

      for (k <- 0 until N) {
        sum = sum + io.a(i * N + k) * io.b(k * P + j)
      }

      io.out(i * P + j) := sum
    }
  }

}

object MatrixMul1 extends App {
  (new ChiselStage).execute(
    Array("-X", "verilog", "-td", "source/"),
    Seq(
      ChiselGeneratorAnnotation(() => new MatrixMul1(8, 8, 8))
    )
  )
}
