package pirate

trait UnixParsers extends HyphenatedParsers {
  lazy val MultiArgSchema: Schema = rep(ArgSchema) map {
    ParserHelper(_).andWithRepetition
  }

  lazy val ArgSchema: Schema = (OptionSchema(MultiArgSchema) | HyphenStartedSchema)

  case class ParserHelper(parsed: List[Parser[Arguments]], unparsed: List[Parser[Arguments]]) {
    def andWithRepetition: Parser[Arguments] =
      unparsed -> parsed match {
        case (Nil, Nil) => success(Arguments.empty)
        case (Nil, list) => rep(list reduce (_ | _)) map {arg =>
          arg reduceOption (_ + _) getOrElse Arguments.empty
        }
        case (_, Nil) => parseUnparsed
        case (_, _) => parseUnparsed | parseParsed
      }

    private[this] def parseParsed =
      (parsed reduce (_ | _)) ~ andWithRepetition map {case f ~ s => f + s}

    private[this] def parseUnparsed = unparsed map {parser  =>
      parser ~ useParser(parser).andWithRepetition map {case f ~ s => f + s}
    } reduce (_ | _)

    private[this] def disableOption(parser: Parser[Arguments]): Parser[Arguments] =
      Parser { input =>
        parser(input) match {
          case s @ Success(argument, _) => if (argument.isEmpty) Failure("no empties", input) else s
          case noSuccess => noSuccess
        }
      }

    private[this] def useParser(parser: Parser[Arguments]) =
      copy(parsed = disableOption(parser) :: parsed,
        unparsed = unparsed filterNot (_ == parser))
  }

  object ParserHelper {
    def apply(unparsed: List[Parser[Arguments]]): ParserHelper = ParserHelper(Nil, unparsed)
  }
}