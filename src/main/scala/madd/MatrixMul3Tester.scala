package madd

import chisel3._
import chisel3.iotesters.PeekPokeTester
import chisel3.util._

class MatrixMul3Tester(dut: MatrixMul3) extends PeekPokeTester(dut) {
  poke(dut.io.in.valid, true)

  // Two matrixs:
  // Matrix A (3 * 2):
  // 0 1
  // 2 3
  // 4 5
  // Matrix B (2 * 4):
  // 0 1 2 3
  // 4 5 6 7

  // var M = 3; var N = 2; var P = 4
  var matA = Array(0, 1, 2, 3, 4, 5)
  var matB = Array(0, 1, 2, 3, 4, 5, 6, 7)
  var matAns = Array(4, 5, 6, 7, 12, 17, 22, 27, 20, 29, 38, 47)

  for (i <- 0 until 3) {
    for (j <- 0 until 4) {
      for (k <- 0 until 2) {
        while (peek(dut.io.in.ready) == BigInt(0)) {
          step(1)
        }
        poke(dut.io.in.bits.a, matA(i * 2 + k))
        poke(dut.io.in.bits.b, matB(k * 4 + j))
      
        step(1)
      }
    }
  }

  poke(dut.io.in.valid, false)

  while (peek(dut.io.out.valid) == BigInt(0)) {
    step(1)
  }

  for (i <- 0 until 3 * 4) {
    expect(dut.io.out.bits(i), matAns(i))
  }
  
}

object MatrixMul3Tester extends App {
  chisel3.iotesters.Driver(() => new MatrixMul3(3, 2, 4)) { dut =>
    new MatrixMul3Tester(dut)
  }
}
