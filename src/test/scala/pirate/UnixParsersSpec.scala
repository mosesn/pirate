package pirate

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import scala.util.parsing.input.CharSequenceReader
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UnixParsersSpec extends FunSpec with ShouldMatchers {
  describe("UnixParsers") {
    describe("ArgSchema") {
      it("Should take a flag and return a nice Arguments of just that flag.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a flag and a few of the same argument, and understand it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several flags and their arguments, and understand it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-abcdef")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an int flag and parse it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take a double flag and parse it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take a double flag and parse an interesting number.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return a nice Arguments of just that flag.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take an optional flag and a few of the same argument, and understand it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several optional flags and their arguments, and understand it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an optional int flag and parse it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an optional int flag and parse an interesting number.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take an optional double flag and parse it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take an optional double flag and parse an interesting number.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return nothing") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags and nothing, and understand it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag and parse nothing.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag and parse nothing.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional flag with spaces and return nothing") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[ -a ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags with spaces and nothing, and understand it.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[ -abcdef ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag with spaces and parse nothing.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[ -f int ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag with spaces and parse nothing.") {
        assert(Pirate.ArgSchema(new CharSequenceReader("[ -f double ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
    }

    describe("MultiArgSchema") {
      it("Should take a flag and return a nice Arguments of just that flag.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a flag and a few of the same argument, and understand it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several flags and their arguments, and understand it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-abcdef")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an int flag and parse it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take a double flag and parse it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take a double flag and parse an interesting number.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return a nice Arguments of just that flag.") {
        val tmp = Pirate.MultiArgSchema(new CharSequenceReader("[-a]")).get
        val bleh = tmp(new CharSequenceReader("-a")).get
        assert(bleh === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take an optional flag and a few of the same argument, and understand it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several optional flags and their arguments, and understand it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an optional int flag and parse it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an optional int flag and parse an interesting number.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take an optional double flag and parse it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take an optional double flag and parse an interesting number.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return nothing") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags and nothing, and understand it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag and parse nothing.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag and parse nothing.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional flag with spaces and return nothing") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -a ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags with spaces and nothing, and understand it.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -abcdef ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag with spaces and parse nothing.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -f int ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag with spaces and parse nothing.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -f double ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take multiple things in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -lst -abc]")).get(new CharSequenceReader("-ls -aba")).get === Arguments.empty.copy(flags = Set('l', 's', 'a', 'b')))
      }
      it("Should take multiple things, when given nothing, in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -lst -abc]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take multiple things, nested.") {
        Pirate.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ] ]")).get
      }
      it("Should take multiple things, nested, in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ] ]")).get(new CharSequenceReader("-ls -aba")).get === Arguments.empty.copy(flags=Set('l', 's', 'a', 'b')))
      }
      it("Should take multiple things, and be ok with the one in the outer nesting, in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ]]")).get(new CharSequenceReader("-ls")).get === Arguments.empty.copy(flags=Set('l', 's')))
      }
      it("Should take multiple things, and be ok with neither, in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ]]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      //TODO MN
      it("Should take one things, and weird nesting, and be ok with taking both in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[[ -lst ]]")).get(new CharSequenceReader("-ls")).get === Arguments.empty.copy(flags=Set('l', 's')))
      }
      it("Should take multiple things, and weird nesting, and be ok with taking both in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("-ls -aba")).get === Arguments.empty.copy(flags=Set('l', 's', 'a', 'b')))
      }
      it("Should take multiple things, and weird nesting, and be ok with taking the first in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("-ls")).get === Arguments.empty.copy(flags=Set('l', 's')))
      }
      it("Should take multiple things, and weird nesting, and be ok with taking the second in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("-aba")).get === Arguments.empty.copy(flags=Set('a', 'b')))
      }
      it("Should take multiple things, and weird nesting, and be ok without taking anything in an option.") {
        assert(Pirate.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
    }
  }
}