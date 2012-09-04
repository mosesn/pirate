package pirate

import scala.util.parsing.input.CharSequenceReader

object Pirate extends SchemaParsers with StringParsers with UnixParsers {
  lazy val CombinedSchema: Schema = MultiArgSchema ~ MultiStringSchema map {
    case args ~ flags => args ~ flags map {case f ~ s => f + s}
  }

  lazy val FullSchema: Schema = phrase(CombinedSchema)

  def apply(helpText: String)(flags: Array[String]): Arguments =
    FullSchema(new CharSequenceReader(helpText)) match {
      case Success(parser, _) => parser(new CharSequenceReader(flags mkString " ")) match {
        case Success(results, _) => results
        case failure: NoSuccess => sys.error(failure.msg)
      }
      case failure: NoSuccess => sys.error(failure.msg)
    }
}
