package com.mosesn.pirate

case class Arguments(flags: Set[Char],
  intMap: Map[Char, Int],
  doubleMap: Map[Char, Double],
  strings: Map[String, String]) {

  def addFlag(flag: Char): Arguments = copy(flags = flags + flag)

  def +(args: Arguments): Arguments =
    Arguments(this.flags ++ args.flags,
      this.intMap ++ args.intMap,
      this.doubleMap ++ args.doubleMap,
      this.strings ++ args.strings)

  def isEmpty = this == Arguments.empty

}

object Arguments {
  lazy val empty = 
    Arguments(Set.empty[Char],
      Map.empty[Char, Int],
      Map.empty[Char, Double],
      Map.empty[String, String])

  def flags(chars: Set[Char]) = Arguments.empty.copy(flags = chars)

  def named(pair: (String, String)) = Arguments.empty.copy(strings = Map(pair))

  def int(pair: (Char, Int)) = Arguments.empty.copy(intMap = Map(pair))

  def double(pair: (Char, Double)) = Arguments.empty.copy(doubleMap = Map(pair))
}
