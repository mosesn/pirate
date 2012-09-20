package com.mosesn.pirate

trait HyphenatedParsers extends ValueParsers with FlagParsers {
  lazy val HyphenStartedSchema: Schema = "-" ~> (ValueParser | FlagSchema)
}
