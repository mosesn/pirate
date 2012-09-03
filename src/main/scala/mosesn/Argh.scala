package mosesn

import scala.util.parsing.input.CharSequenceReader
import scala.util.parsing.combinator.JavaTokenParsers

object Argh extends JavaTokenParsers {

  val OptFlagParser: String => Parser[Elem] = (flags: String) =>
    acceptIf(flags.contains(_))((err: Char) => "%c is not a valid flag.".format(err))

  val OptFlagsParser: String => Parser[List[Char]] = (flags: String) => rep1(OptFlagParser(flags))

  val OptionsParser: String => Parser[Arguments] = (flags: String) =>
    "-" ~> OptFlagsParser(flags) map (list => Arguments.flags(list.toSet))

  val FlagsParser: Parser[String] = """[A-Za-z]+""".r

  def OptionMultiArgSchema: Parser[Parser[Arguments]] =
    ("[" ~> MultiArgSchema <~ "]") map (opt(_) map (_ getOrElse Arguments.empty))

  lazy val IntParser: Char => Parser[Arguments] =
    char => "-" ~> char.toString ~> wholeNumber map { elt =>
      Arguments.int(char -> elt.toInt)
    }

  lazy val DoubParser: Char => Parser[Arguments] =
    char => "-" ~> char.toString ~> floatingPointNumber map { elt =>
      Arguments.double(char -> elt.toDouble)
    }

  lazy val NamedStringParser: (String => Parser[Arguments]) =
    (name: String) => """[^\s]+""".r map { string => Arguments.named(name -> string) }

  lazy val StringSchema: Parser[Parser[Arguments]] = """\w+""".r map { key =>
    NamedStringParser(key)
  }

  lazy val OptStringSchema: Parser[Parser[Arguments]] =
    "[" ~> MultiStringSchema <~ "]" map {
      opt(_) map (_.getOrElse(Arguments.empty))
    }

  lazy val FlexibleStringSchema: Parser[Parser[Arguments]] = OptStringSchema | StringSchema

  lazy val MultiStringSchema: Parser[Parser[Arguments]] = rep(FlexibleStringSchema) map { list =>
    list reduceOption { (first, second) =>
      first ~ second map {
        case f ~ s => f + s
      }
    } getOrElse success(Arguments.empty)
  }

  lazy val LetterParser = acceptIf(_.isLetter)((err: Char) =>
    "%c should have been a letter".format(err))

  lazy val IntegerParser: Parser[Parser[Arguments]] = LetterParser <~ "int" map (IntParser)

  lazy val StringParser: Parser[Parser[Arguments]] = LetterParser <~ "string" map {arg =>
    "-" ~> arg.toString ~> NamedStringParser(arg.toString)
  }

  lazy val DoubleParser: Parser[Parser[Arguments]] = LetterParser <~ "double" map (DoubParser)

  lazy val NumberParser = DoubleParser | IntegerParser

  lazy val AllFlagsSchema: Parser[Parser[Arguments]] = FlagsParser map (OptionsParser)

  lazy val HyphenStartedSchema: Parser[Parser[Arguments]] = "-" ~> (NumberParser | AllFlagsSchema)

  val ArgSchema: Parser[Parser[Arguments]] = (OptionMultiArgSchema | HyphenStartedSchema)

  def MultiArgSchema: Parser[Parser[Arguments]] = handleMultiArgsFancy(rep(ArgSchema))

  def handleMultiArgsFancy(top: Parser[List[Parser[Arguments]]]): Parser[Parser[Arguments]] =
    top map {ParserHelper(Nil, _, Arguments.empty).andWithRepetition}

  lazy val FlagsAndStringsSchema: Parser[Parser[Arguments]] =
    MultiArgSchema ~ MultiStringSchema map {
      case args ~ flags => args ~ flags map {case f ~ s => f + s}
    }

  lazy val PhraseFASSchema: Parser[Parser[Arguments]] = phrase(FlagsAndStringsSchema)

  def apply(helpText: String)(flags: Array[String]): Arguments =
    FlagsAndStringsSchema(new CharSequenceReader(helpText)) match {
      case Success(parser, _) => parser(new CharSequenceReader(flags mkString " ")) match {
        case Success(results, _) => results
        case failure: NoSuccess => sys.error(failure.msg)
      }
      case failure: NoSuccess => sys.error(failure.msg)
    }

  case class ParserHelper(parsed: List[Parser[Arguments]],
    unparsed: List[Parser[Arguments]],
    default: Arguments) {

    def andWithRepetition: Parser[Arguments] =
      unparsed -> parsed match {
        case (Nil, Nil) => success(default)
        case (Nil, list) => rep(list reduce (_ | _)) map {arg =>
          arg reduceOption (_ + _) getOrElse default
        }
        case (_, Nil) => parseUnparsed
        case (_, _) => parseUnparsed | parseParsed
      }

    private[this] def parseParsed =
      (parsed reduce (_ | _)) ~ andWithRepetition map {case f ~ s => f + s}

    private[this] def parseUnparsed =
      unparsed map {parser =>
        parser ~ useParser(parser).andWithRepetition map {case f ~ s => f + s}
      } reduce (_ | _)

    private[this] def disableOption(parser: Parser[Arguments]): Parser[Arguments] = Parser { input =>
      parser(input) match {
        case s @ Success(argument, _) => if (argument.isEmpty) Failure("no empties", input) else s
        case noSuccess => noSuccess
      }
    }

    private[this] def useParser(parser: Parser[Arguments]) = copy(parsed = disableOption(parser) :: parsed,
      unparsed = unparsed filterNot (_ == parser))
  }

}

case class Arguments(flags: Set[Char], intMap: Map[Char, Int], doubleMap: Map[Char, Double], strings: Map[String, String]) {
  def addFlag(flag: Char): Arguments = copy(flags = flags + flag)

  def +(args: Arguments): Arguments = Arguments(this.flags ++ args.flags, this.intMap ++ args.intMap, this.doubleMap ++ args.doubleMap, this.strings ++ args.strings)

  def isEmpty = this == Arguments.empty
}

object Arguments {
  lazy val empty = Arguments(Set.empty[Char], Map.empty[Char, Int], Map.empty[Char, Double], Map.empty[String, String])

  def flags(chars: Set[Char]) = Arguments.empty.copy(flags = chars)

  def named(pair: Pair[String, String]) = Arguments.empty.copy(strings = Map(pair))

  def int(pair: Pair[Char, Int]) = Arguments.empty.copy(intMap = Map(pair))

  def double(pair: Pair[Char, Double]) = Arguments.empty.copy(doubleMap = Map(pair))
}