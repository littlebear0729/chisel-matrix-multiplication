package madd

import chisel3._
import chisel3.iotesters.PeekPokeTester
import chisel3.util._
import scala.language.experimental

// poke: input data to module
// peek: get module output data
// expect: compare data

class MatrixMul1Tester(dut: MatrixMul1) extends PeekPokeTester(dut) {

  var matA = Array(0, 1, 2, 3, 4, 5)
  var matB = Array(0, 1, 2, 3, 4, 5, 6, 7)
  var matAns = Array(4, 5, 6, 7, 12, 17, 22, 27, 20, 29, 38, 47)

  for (i <- 0 until 3 * 2) {
    poke(dut.io.a(i), matA(i))
  }

  for (i <- 0 until 2 * 4) {
    poke(dut.io.b(i), matB(i))
  }

  for (i <- 0 until 3 * 4) {
    expect(dut.io.out(i), matAns(i))
  }

}

object MatrixMul1Tester extends App {
  chisel3.iotesters.Driver(() => new MatrixMul1(3, 2, 4)) { dut =>
    new MatrixMul1Tester(dut)
  }
}
