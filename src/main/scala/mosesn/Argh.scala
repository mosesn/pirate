package mosesn

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.CharSequenceReader

object Argh extends Parsers {
  
  type Elem = Char

  val emptyCharSequenceReader = new CharSequenceReader("")
  
  val HyphenParser: Parser[Elem] = elem('-')
 
  val WhitespaceParser = rep(acceptIf(_.isWhitespace)((err: Char) => "%c is not whitespaces.".format(err)))
    
  val WhitespaceHyphenParser = WhitespaceParser ~ HyphenParser
    
  val OptionsParser: String => Parser[Arguments] = (flags: String) => opt((WhitespaceHyphenParser ~ 
      OptFlagsParser(flags)) map (_._2)) map (_.getOrElse(Arguments(Set.empty[Char])))
  
  val OptFlagParser: String => Parser[Elem] = (flags: String) => 
    acceptIf(flags.contains(_))((err: Char) => "%c is not a valid flag.")

  val OptFlagsParser: String => Parser[Arguments] = (flags: String) => 
    rep1(OptFlagParser(flags)) map { elems: List[Char] => Arguments(elems.toSet) }

  val FlagsParser: Parser[String] = rep1(LetterParser) map (_.mkString)
    
  val LetterParser = acceptIf(_.isLetter)((err: Char) => "%c should have been a letter.".format(err))
  
  val AllFlagsParser = new Parser[Parser[Arguments]] {
    def apply(input: Input): ParseResult[Parser[Arguments]] = FlagsParser(input) match {
      case Success(results, rest) => Success(OptionsParser(results), rest)
      case failure: NoSuccess => failure
    }
  }
  
  val MeatParser: Parser[Parser[Arguments]] = (WhitespaceHyphenParser ~ AllFlagsParser ~ WhitespaceParser) map (_._1._2)
  
  val BeginBracketParser = elem('[')
  
  val EndBracketParser = elem(']')

  val HelpTextParser: Parser[Parser[Arguments]] = phrase(opt((WhitespaceParser ~ BeginBracketParser ~
      MeatParser ~ EndBracketParser ~ WhitespaceParser)) map { _ match {
      case Some(parser) => parser._1._1._2
      case None => OptionsParser("")
    }
  })
  
  def apply(helpText: String)(flags: String): Arguments = HelpTextParser(new CharSequenceReader(helpText)) match {
    case Success(parser, _) => parser(new CharSequenceReader(flags)) match {
      case Success(results, _) => results
      case failure: NoSuccess => scala.sys.error(failure.msg)
    }
    case failure: NoSuccess => scala.sys.error(failure.msg)
  }
}

case class Arguments(flags: Set[Char]) {
  def addFlag(flag: Char): Arguments = this.copy(flags = flags + flag)
}

object Arguments {
  lazy val empty = Arguments(Set.empty[Char])
}