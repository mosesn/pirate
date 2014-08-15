package com.mosesn.pirate

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import scala.util.parsing.input.CharSequenceReader

@RunWith(classOf[JUnitRunner])
class SchemaParsersSpec extends FunSpec {
  describe("SchemaParsers") {
    describe("NamedStringParser") {
      it("Should be able to take a single named string and parse it, with a value.") {
        assert(Pirate.NamedStringParser("key")(new CharSequenceReader("value")).get ===
          Arguments.empty.copy(strings = Map("key" -> "value")))
      }
    }
  }
}
