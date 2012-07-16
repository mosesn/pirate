package mosesn

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.CharSequenceReader

object Argh extends Parsers {
  
  type Elem = Char

  val emptyCharSequenceReader = new CharSequenceReader("")
  
  val HyphenParser = new Parser[Unit] {
    def apply(input: Input): ParseResult[Unit] = input match {
      case finished: Input if finished.atEnd => Failure("No hyphen found.", input.rest)
      case unfinished: Input => unfinished.first match {
        case whiteSpace: Char if whiteSpace.isWhitespace => this(unfinished.rest)
        case '-' => Success(Unit, unfinished.rest)
        case _ => Failure("Invalid character, no hyphen found.", unfinished.rest)
      }
    }
  }
  
//  val CoolHyphenParser = this.acceptIf(x: Char => (x.isWhitespace || (x == '-')))
  
  val OptionsParser: String => Parser[Arguments] = (flags: String) => opt((HyphenParser ~ OptFlagsParser(flags)) map {
    case result => result._2
  }) map {
    case Some(arguments) => arguments
    case None => Arguments(Set.empty[Char])
  }

  val OptFlagsParser: String => Parser[Arguments] = (flags: String) => new Parser[Arguments] {
    def apply(input: Input): ParseResult[Arguments] = input match {
      case finished: Input if finished.atEnd => Success(Arguments(Set.empty[Char]), emptyCharSequenceReader)
      case contained: Input if flags.contains(contained.first) => {
        this(input.rest) match {
          case Success(results, rest) => Success(results.addFlag(input.first), rest)
          case failure: NoSuccess => failure
        }
      }
      case invalid => Failure("Not a valid character: %c.".format(invalid), input.rest)
    }
  }
  
  val FlagsParser = new Parser[String] {
    def apply(input: Input): ParseResult[String] = input match {
      case finished: Input if finished.atEnd =>
        Failure("Did not end with ]", emptyCharSequenceReader)
      case unfinished: Input => {
        val first = unfinished.first
        val rest = unfinished.rest
        first match {
          case letter: Char if letter.isLetter => this(rest) match {
            case Success(result, rest) => Success(result + first, rest)
            case failure: NoSuccess => failure
          }
          case letter: Char if letter.isWhitespace => this(rest)
          case ']' => Success("", rest)
          case _ => Failure("Expected letter or whitepsace.", rest)
        }
      }
    }
  }
  
  val BracketStartedParser = new Parser[Parser[Arguments]] {
    def apply(input: Input): ParseResult[Parser[Arguments]] = input match {
      case finished: Input if finished.atEnd => 
        Failure("Did not end with ], expected -", emptyCharSequenceReader)
      case unfinished: Input => {
        val first = unfinished.first
        val rest = unfinished.rest
        first match {
          case '-' => FlagsParser(rest) match {
            case Success(results, rest) => Success(OptionsParser(results), rest)
            case failure: NoSuccess => failure
          }
          case char: Char if char.isWhitespace => this(rest)
          case other => Failure("Expected -, found %c".format(other), rest)
        }
      }
      case _ => throw new Exception("Input is not of type Input.")
    }
  }
  
  val HelpTextParser = new Parser[Parser[Arguments]] {
    def apply(input: Input): ParseResult[Parser[Arguments]] = input match {
      case finished: Input if (finished.atEnd) => Success((new Parser[Arguments] {
        def apply(input: Input): ParseResult[Arguments] = 
          Success(Arguments(Set.empty[Char]), emptyCharSequenceReader)
      }), emptyCharSequenceReader)
      case unfinished: Input => {
        val first = unfinished.first
        val rest = unfinished.rest
        first match {
          case '[' => BracketStartedParser(unfinished.rest)
          case char: Char if char.isWhitespace => this(rest)
          case _ => Failure("Did not start with [", rest)
        }
      }
      case _ => throw new Exception("Input is not of type Input.")
    }
  }

  def apply(helpText: String)(flags: String): Arguments = HelpTextParser(new CharSequenceReader(helpText)) match {
    case Success(parser: Parser[Arguments], _) => parser(new CharSequenceReader(flags)) match {
      case Success(results, _) => results
      case failure: NoSuccess => scala.sys.error(failure.msg)
    }
    case failure: NoSuccess => scala.sys.error(failure.msg)
  }
}

case class Arguments(flags: Set[Char]) {
  def addFlag(flag: Char): Arguments = this.copy(flags = flags + flag)
}