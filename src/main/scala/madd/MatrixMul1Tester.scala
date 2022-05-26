package madd

import chisel3._
import chisel3.iotesters.PeekPokeTester
import chisel3.util._

// poke: input data to module
// peek: get module output data
// expect: compare data

class MatrixMul1Tester(dut: MatrixMul1) extends PeekPokeTester(dut) {
  for (i <- 0 until 3 * 2) {
    poke(dut.io.a(i), i)
  }

  for (i <- 0 until 2 * 4) {
    poke(dut.io.b(i), i)
  }

  expect(dut.io.out(0), 4)
  expect(dut.io.out(1), 5)
  expect(dut.io.out(2), 6)
  expect(dut.io.out(3), 7)
  expect(dut.io.out(4), 12)
  expect(dut.io.out(5), 17)
  expect(dut.io.out(6), 22)
  expect(dut.io.out(7), 27)
  expect(dut.io.out(8), 20)
  expect(dut.io.out(9), 29)
  expect(dut.io.out(10), 38)
  expect(dut.io.out(11), 47)

}

object MatrixMul1Tester extends App {
  chisel3.iotesters.Driver(() => new MatrixMul1(3, 2, 4)) { dut =>
    new MatrixMul1Tester(dut)
  }
}
