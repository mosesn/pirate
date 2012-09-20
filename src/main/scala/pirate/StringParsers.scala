package com.mosesn.pirate

trait StringParsers extends SchemaParsers {
  lazy val MultiStringSchema: Schema = rep(StringSchema) map {
    _ reduceOption { 
      (first, second) => first ~ second map { case f ~ s => f + s }
    } getOrElse success(Arguments.empty)
  }

  lazy val StringSchema: Schema = OptionSchema(MultiStringSchema) | RawStringSchema

  lazy val RawStringSchema: Schema = ident map NamedStringParser
}
