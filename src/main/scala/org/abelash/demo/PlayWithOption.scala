package org.abelash.demo


import cats.instances.option._
import cats.syntax.apply._

object PlayWithOption extends App {
  case class cc(a: String, b: String)
  val  a = null
  val  b = "b"
  val  testoption = (Option(a), Option(b)).mapN(cc(_, _))
  println(testoption)
  val  test = Option(cc(a, b))
  println(test)
  val  testequivaventcats = if (( a == null) || (b == null) ) None else Option(cc(a, b))
  println(testequivaventcats)
}
