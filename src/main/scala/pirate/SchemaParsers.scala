package pirate

import scala.util.parsing.combinator.JavaTokenParsers

trait SchemaParsers extends JavaTokenParsers {
  type Schema = Parser[Parser[Arguments]]

  lazy val OptionSchema: Schema => Schema = schema =>
    ("[" ~> schema <~ "]") map (opt(_) map (_ getOrElse Arguments.empty))

  lazy val NamedStringParser: (String => Parser[Arguments]) = name =>
    """[^\s]+""".r map { string => Arguments.named(name -> string) }
}