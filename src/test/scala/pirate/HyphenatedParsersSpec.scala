package com.mosesn.pirate

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import scala.util.parsing.input.CharSequenceReader
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HyphenatedParsersSpec extends FunSpec with ShouldMatchers {
  describe("HyphenatedParsers") {
    describe("HyphenStartedSchema") {
      it("Should take a flag and return a nice Arguments of just that flag.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a flag and a few of the same argument, and understand it.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several flags and their arguments, and understand it.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-abcdef")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an int flag and parse it.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take a double flag and parse it.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take a double flag and parse an interesting number.") {
        assert(Pirate.HyphenStartedSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
    }
  }
}
