package com.mosesn.pirate

import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.util.parsing.input.CharSequenceReader
import scala.util.parsing.combinator.RegexParsers

@RunWith(classOf[JUnitRunner])
class PirateSpec extends FunSpec {
  describe("Pirate") {
    describe("apply") {
      it("Should handle empty help text.") {
        assert(Pirate("")(Array("")) === Arguments.empty) 
      }

      it("Should handle very simple help text.  Affirmed.") {
        assert(Pirate("[ -f ]")(Array("-f")) === Arguments.empty.copy(flags = Set('f')))
      }

      it("Should handle very simple help text.  Denied.") {
        assert(Pirate("[ -f ]")(Array("")) === Arguments.empty)
      }

      it("Should handle more complicated help text.") {
        assert(Pirate("[ -fasjklm ]")(Array("-fsjmss")) === Arguments.empty.copy(flags = Set('f','s','j','m')))
      }

      it("Should handle more complicated flags text.") {
        assert(Pirate("[ -fasjklm ]")(Array(" -fsjmssask")) === 
          Arguments.empty.copy(flags = Set('a','k','f','s','j','m')))
      }
    }
    describe("CombinedSchema") {
      it("Should handle wc args") {
        assert(Pirate.CombinedSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("-cl")).get ===
          Arguments.empty.copy(flags=Set('c', 'l')))
      }
      it("Should handle wc args with a file") {
        assert(Pirate.CombinedSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("-cl input.txt")).get ===
          Arguments.empty.copy(flags=Set('c', 'l'), strings=Map("file" -> "input.txt")))
      }
      it("Should handle wc args with only file") {
        assert(Pirate.CombinedSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("input.txt")).get ===
          Arguments.empty.copy(strings=Map("file" -> "input.txt")))
      }
      it("Should handle wc args without args") {
        assert(Pirate.CombinedSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("")).get ===
          Arguments.empty.copy())
      }
      it("Should handle man args without args") {
        assert(Pirate.CombinedSchema(new CharSequenceReader("[-acdfFhkKtwW] [--path] " +
          "[-m system] [-p string] [-C config_file] [-M pathlist] [-P pager] [-B browser]" +
          "[-H htmlpager] [-S section_list] [section]")).get(new CharSequenceReader("")).get ===
            Arguments.empty)
      }
      it("Should handle man args with some args") {
        assert(Pirate.CombinedSchema(new CharSequenceReader("[-acdfFhkKtwW -B int]")).get(new CharSequenceReader("-acfkW -B 3")).get ===
          Arguments.empty.copy(flags = Set('a', 'c', 'f', 'k', 'W'), intMap = Map('B' -> 3)))
      }
    }
  }
}
