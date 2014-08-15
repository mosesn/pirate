package com.mosesn.pirate

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import scala.util.parsing.input.CharSequenceReader

@RunWith(classOf[JUnitRunner])
class StringParsersSpec extends FunSpec {
  describe("StringParsers") {
    describe("RawStringSchema") {
      it("Should be able to take a single string and parse it.") {
        assert(Pirate.RawStringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string with weird characters and read it.") {
        assert(Pirate.RawStringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid.txt")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid.txt")))
      }
    }
    describe("OptStringSchema") {
      it("Should be able to take a single string and parse it, with an input.") {
        assert(Pirate.OptionSchema(Pirate.MultiStringSchema)(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, without an input.") {
        assert(Pirate.OptionSchema(Pirate.MultiStringSchema)(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
    }
    describe("StringSchema") {
      it("Should be able to take a single string and parse it.") {
        assert(Pirate.StringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, with an input.") {
        assert(Pirate.StringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, without an input.") {
        assert(Pirate.StringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
    }
    describe("MultiStringSchema") {
      it("Should be able to take a single string and parse it.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, with an input.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, without an input.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
      it("Should be able to take a couple strings and parse them.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("input key")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
      it("Should be able to take a couple strings and parse them, with an input.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("[ input key ]")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
      it("Should be able to take a couple strings and parse them, without an input.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("[ input key ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
      it("Should be able to take a couple strings and parse them, without an input for one.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("[ input [ key ] ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a couple strings and parse them, where one is optional, but both inputs are given.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("input [ key ]")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
      it("Should be able to take a couple strings and parse them, when each is optional separately.") {
        assert(Pirate.MultiStringSchema(new CharSequenceReader("[input] [ key ]")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
    }
  }
}
