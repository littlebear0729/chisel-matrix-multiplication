package madd

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselStage, ChiselGeneratorAnnotation}

class MatrixMul3(M: Int, N: Int, P: Int) extends Module with CurrentCycle {
  val io = IO(new MatrixMul3IO(M, N, P))

  io.in.ready := false.B

  io.out.bits := DontCare
  io.out.valid := false.B

  val regA = Reg(SInt(32.W))
  val regB = Reg(SInt(32.W))

  val regLoadEnabled = RegInit(true.B)
  val regComputeEnabled = RegInit(false.B)
  val regStoreEnabled = RegInit(false.B)

  val regOut = Reg(Vec(M * P, SInt(32.W)))

  val i = Counter(M)
  val j = Counter(P)
  val k = Counter(N)

  io.in.ready := regLoadEnabled

  when(io.in.fire()) {
    regA := io.in.bits.a
    regB := io.in.bits.b
    regComputeEnabled := true.B
  }.otherwise {
    regComputeEnabled := false.B
  }

  when(regComputeEnabled) {
    //regOut(i.value * N.U + j.value) := regA + regB

    var sum = 0.S(32.W)
    
    when(k.value === 0.U) {
      sum = regA * regB
      regOut(i.value * P.U + j.value) := sum
    }

    when(k.value =/= 0.U) {
      sum = regOut(i.value * P.U + j.value) + regA * regB
      regOut(i.value * P.U + j.value) := sum
    }

    when(k.inc()) {
      when(j.inc()) {
        when(i.inc()) {
          regLoadEnabled := false.B
          regStoreEnabled := true.B
        }
      }
    }
  }

  when(regStoreEnabled) {
    io.out.bits := regOut
    io.out.valid := true.B
  }

  chisel3.printf(
    p"[$currentCycle] io.in.fire(): ${io.in.fire()}, regA: $regA, regB: $regB, regLoadEnabled: $regLoadEnabled, regComputeEnabled: $regComputeEnabled, regStoreEnabled: $regStoreEnabled, regOut: $regOut, i: ${i.value}, j: ${j.value}, io.out.fire(): ${io.out
      .fire()}\n"
  )
}

object MatrixMul3 extends App {
  (new ChiselStage).execute(
    Array("-X", "verilog", "-td", "source/"),
    Seq(
      ChiselGeneratorAnnotation(() => new MatrixMul3(3, 2, 4))
    )
  )
}
