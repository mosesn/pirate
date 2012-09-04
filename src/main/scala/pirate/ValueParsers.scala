package pirate

trait ValueParsers extends SchemaParsers{
  type FlagValueSchema = Parser[Char] => Schema

  lazy val ValueParser: Schema = NumberParser(LetterParser) | ValueStringSchema(LetterParser)

  lazy val NumberParser: FlagValueSchema = parser => DoubleSchema(parser) | IntegerSchema(parser)

  lazy val DoubleSchema: FlagValueSchema = parser => parser <~ literal("double") map { char =>
    LiteralParser(char, floatingPointNumber map { elt =>
     Arguments.double(char -> elt.toDouble)
    })
  }

  lazy val IntegerSchema: FlagValueSchema =  parser => parser <~ "int" map { char =>
    LiteralParser(char, wholeNumber map { elt =>
     Arguments.int(char -> elt.toInt)
    })
  }

  lazy val ValueStringSchema: FlagValueSchema = parser => parser <~ literal("string") map { char =>
    LiteralParser(char, NamedStringParser(char.toString))
  }

  lazy val LiteralParser: (Char, Parser[Arguments]) => Parser[Arguments] = (char, parser) =>
    "-" ~> char.toString ~> parser

  lazy val LetterParser = regex("""[A-Za-z]""".r) map (_.charAt(0))
}