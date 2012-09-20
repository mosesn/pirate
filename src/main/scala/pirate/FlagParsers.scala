package com.mosesn.pirate

trait FlagParsers extends SchemaParsers {
  lazy val FlagSchema: Schema = """[A-Za-z]+""".r map (OptionsParser)

  lazy val OptionsParser: String => Parser[Arguments] = (flags: String) =>
    "-" ~> rep1(OptionParser(flags)) map (list => Arguments.flags(list.toSet))

  lazy val OptionParser: String => Parser[Elem] = (flags: String) =>
    acceptIf(flags.contains(_))((err: Char) => "%c is not a valid flag.".format(err))
}
