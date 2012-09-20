package com.mosesn.pirate

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import scala.util.parsing.input.CharSequenceReader
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FlagParsersSpec extends FunSpec with ShouldMatchers {
  describe("FlagParsers") {
    describe("OptFlagParser") {
      it("Should take a one length string and parse that one character.") {
        assert(Pirate.OptionParser("a")(new CharSequenceReader("a")).get === 'a')
      }
    }
    describe("OptionsParser") {
      it("Should take a one length string and parse that one character once.") {
        assert(Pirate.OptionsParser("a")(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a one length string and parse that one character twice.") {
        assert(Pirate.OptionsParser("a")(new CharSequenceReader("-aa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a one length string and parse that one character many times.") {
        assert(Pirate.OptionsParser("a")(new CharSequenceReader("-aaaaaaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a two length string and parse one character.") {
        assert(Pirate.OptionsParser("ab")(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a two length string and parse both characters.") {
        assert(Pirate.OptionsParser("ab")(new CharSequenceReader("-ababababbba")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take a multi length string and parse all.") {
        assert(Pirate.OptionsParser("abcde")(new CharSequenceReader("-abcde")).get === Arguments.empty.copy(flags = Set('a', 'b', 'c', 'd', 'e')))
      }
    }
    describe("FlagSchema") {
      it("Should take a letter and another letter and return a nice Arguments of just that letter.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a few letters and another letter and get just one of them.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("ab")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a few letters and another letter and get just one of them, many times.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("ab")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a few letters and another letter and get both of them.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("ab")).get(new CharSequenceReader("-ab")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take a few letters and another letter and get both of them, many times.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("ab")).get(new CharSequenceReader("-abbaba")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take many different letters, and get just two of them.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("abcdefgh")).get(new CharSequenceReader("-abbaba")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take many different letters, and get all of them.") {
        assert(Pirate.FlagSchema(new CharSequenceReader("abcdefgh")).get(new CharSequenceReader("-abcdefgh")).get === Arguments.empty.copy(flags = Set('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')))
      }
    }
  }
}
