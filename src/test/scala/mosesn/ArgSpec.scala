package mosesn

import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.util.parsing.input.CharSequenceReader
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ArgSpec extends FunSpec with ShouldMatchers{
  describe("Argh") {
    describe("apply") {
      it("Should handle empty help text.") {
        assert(Argh("")(Array("")) === Arguments.empty) 
      }

      it("Should handle very simple help text.  Affirmed.") {
        assert(Argh("[ -f ]")(Array("-f")) === Arguments.empty.copy(flags = Set('f')))
      }

      it("Should handle very simple help text.  Denied.") {
        assert(Argh("[ -f ]")(Array("")) === Arguments.empty)
      }

      it("Should handle more complicated help text.") {
        assert(Argh("[ -fasjklm ]")(Array("-fsjmss")) === Arguments.empty.copy(flags = Set('f','s','j','m')))
      }

      it("Should handle more complicated flags text.") {
        assert(Argh("[ -fasjklm ]")(Array(" -fsjmssask")) === 
          Arguments.empty.copy(flags = Set('a','k','f','s','j','m')))
      }
    }
    describe("NamedStringParser") {
      it("Should be able to take a single named string and parse it, with a value.") {
        assert(Argh.NamedStringParser("key")(new CharSequenceReader("value")).get ===
          Arguments.empty.copy(strings = Map("key" -> "value")))
      }
    }
    describe("StringSchema") {
      it("Should be able to take a single string and parse it.") {
        assert(Argh.StringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string with weird characters and read it.") {
        assert(Argh.StringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid.txt")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid.txt")))
      }
    }
    describe("OptStringSchema") {
      it("Should be able to take a single string and parse it, with an input.") {
        assert(Argh.OptStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, without an input.") {
        assert(Argh.OptStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
    }
    describe("FlexibleStringSchema") {
      it("Should be able to take a single string and parse it.") {
        assert(Argh.FlexibleStringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, with an input.") {
        assert(Argh.FlexibleStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, without an input.") {
        assert(Argh.FlexibleStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
    }
    describe("MultiStringSchema") {
      it("Should be able to take a single string and parse it.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("input")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, with an input.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a single string and parse it, without an input.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("[ input ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
      it("Should be able to take a couple strings and parse them.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("input key")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
      it("Should be able to take a couple strings and parse them, with an input.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("[ input key ]")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
      it("Should be able to take a couple strings and parse them, without an input.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("[ input key ]")).get(new CharSequenceReader("")).get ===
          Arguments.empty)
      }
      it("Should be able to take a couple strings and parse them, without an input for one.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("[ input [ key ] ]")).get(new CharSequenceReader("valid")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid")))
      }
      it("Should be able to take a couple strings and parse them, where one is optional, but both inputs are given.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("input [ key ]")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
      it("Should be able to take a couple strings and parse them, when each is optional separately.") {
        assert(Argh.MultiStringSchema(new CharSequenceReader("[input] [ key ]")).get(new CharSequenceReader("valid value")).get ===
          Arguments.empty.copy(strings = Map("input" -> "valid", "key" -> "value")))
      }
    }
    describe("OptFlagParser") {
      it("Should take a one length string and parse that one character.") {
        assert(Argh.OptFlagParser("a")(new CharSequenceReader("a")).get === 'a')
      }
    }
    describe("OptFlagsParser") {
      it("Should take a one length string and parse that one character once.") {
        assert(Argh.OptFlagsParser("a")(new CharSequenceReader("a")).get === List('a'))
      }
      it("Should take a one length string and parse that one character twice.") {
        assert(Argh.OptFlagsParser("a")(new CharSequenceReader("aa")).get === List('a', 'a'))
      }
      it("Should take a one length string and parse that one character many times.") {
        assert(Argh.OptFlagsParser("a")(new CharSequenceReader("aaaaaaaaa")).get === List('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'))
      }
      it("Should take a two length string and parse one character.") {
        assert(Argh.OptFlagsParser("ab")(new CharSequenceReader("a")).get === List('a'))
      }
      it("Should take a two length string and parse both characters.") {
        assert(Argh.OptFlagsParser("ab")(new CharSequenceReader("ababababbba")).get === List('a', 'b', 'a', 'b', 'a', 'b', 'a', 'b', 'b', 'b', 'a'))
      }
      it("Should take a multi length string and parse all.") {
        assert(Argh.OptFlagsParser("abcde")(new CharSequenceReader("abcde")).get === List('a', 'b', 'c', 'd', 'e'))
      }
    }
    describe("OptionsParser") {
      it("Should take a one length string and parse that one character once.") {
        assert(Argh.OptionsParser("a")(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a one length string and parse that one character twice.") {
        assert(Argh.OptionsParser("a")(new CharSequenceReader("-aa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a one length string and parse that one character many times.") {
        assert(Argh.OptionsParser("a")(new CharSequenceReader("-aaaaaaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a two length string and parse one character.") {
        assert(Argh.OptionsParser("ab")(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a two length string and parse both characters.") {
        assert(Argh.OptionsParser("ab")(new CharSequenceReader("-ababababbba")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take a multi length string and parse all.") {
        assert(Argh.OptionsParser("abcde")(new CharSequenceReader("-abcde")).get === Arguments.empty.copy(flags = Set('a', 'b', 'c', 'd', 'e')))
      }
    }
    describe("FlagsParser") {
      it("Should take a letter and return a letter.") {
        assert(Argh.FlagsParser(new CharSequenceReader("a")).get === "a")
      }
      it("Should take a different letter and return a letter.") {
        assert(Argh.FlagsParser(new CharSequenceReader("b")).get === "b")
      }
      it("Should take a short, boring string and return the same one.") {
        assert(Argh.FlagsParser(new CharSequenceReader("baa")).get === "baa")
      }
      it("Should take a slightly less boring string and return the same one.") {
        assert(Argh.FlagsParser(new CharSequenceReader("baca")).get === "baca")
      }
      it("Should take an interesting string and get it back.") {
        assert(Argh.FlagsParser(new CharSequenceReader("abcdefgh")).get === "abcdefgh")
      }
    }
    describe("AllFlagsParser") {
      it("Should take a letter and another letter and return a nice Arguments of just that letter.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a few letters and another letter and get just one of them.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("ab")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a few letters and another letter and get just one of them, many times.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("ab")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a few letters and another letter and get both of them.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("ab")).get(new CharSequenceReader("-ab")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take a few letters and another letter and get both of them, many times.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("ab")).get(new CharSequenceReader("-abbaba")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take many different letters, and get just two of them.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("abcdefgh")).get(new CharSequenceReader("-abbaba")).get === Arguments.empty.copy(flags = Set('a', 'b')))
      }
      it("Should take many different letters, and get all of them.") {
        assert(Argh.AllFlagsParser(new CharSequenceReader("abcdefgh")).get(new CharSequenceReader("-abcdefgh")).get === Arguments.empty.copy(flags = Set('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')))
      }
    }
    describe("Innteger") {
      it("Should take a 0 and get a 0 back.") {
        assert(Argh.Innteger(new CharSequenceReader("0")).get === 0)
      }
      it("Should take a 123456 and get a 123456 back.") {
        assert(Argh.Innteger(new CharSequenceReader("123456")).get === 123456)
      }
    }
    describe("Douuble") {
      it("Should take a 0 and get a 0 back.") {
        Argh.Doouble(new CharSequenceReader("0.0")).get should be (0.0)
      }
      it("Should take a 123.456 and get a 123.456 back.") {
        Argh.Doouble(new CharSequenceReader("123.456")).get should be (123.456 plusOrMinus 0.0001)
      }
      it("Should take a .456 and get a .456 back.") {
        Argh.Doouble(new CharSequenceReader(".456")).get should be  (.456 plusOrMinus 0.0001)
      }
    }
    describe("IntParser") {
      it("Should take a 0 and get a 0 back.") {
        assert(Argh.IntParser('f')(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take a 456 and get a 0 back.") {
        assert(Argh.IntParser('f')(new CharSequenceReader("-f 456")).get === Arguments.empty.copy(intMap = Map('f' -> 456)))
      }
    }
    describe("WhiteSpacedInt") {
      it("Should take the word int with one space.") {
        Argh.WhiteSpacedInt(new CharSequenceReader(" int")).get
      }
      it("Should take the word int with spaces.") {
        Argh.WhiteSpacedInt(new CharSequenceReader("    int")).get
      }
      it("Should take the word int without spaces.") {
        Argh.WhiteSpacedInt(new CharSequenceReader("int")).get
      }
    }
    describe("IntegerParser") {
      it("Should take an int flag.") {
        Argh.IntegerParser(new CharSequenceReader("f int")).get
      }
      it("Should take an int flag and parse it.") {
        assert(Argh.IntegerParser(new CharSequenceReader("f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Argh.IntegerParser(new CharSequenceReader("f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
    }
    describe("StringParser") {
      it("Should parse a character properly.") {
        assert(Argh.StringParser(new CharSequenceReader("p string")).get(new CharSequenceReader("-p pppp")).get ===
          Arguments.empty.copy(strings = Map("p" -> "pppp")))
      }
    }
    //TODO MN
    /*
    describe("HyphenStartedSchema") {
      it("Should take a flag and return a nice Arguments of just that flag.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a flag and a few of the same argument, and understand it.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several flags and their arguments, and understand it.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-abcdef")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an int flag and parse it.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take a double flag and parse it.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take a double flag and parse an interesting number.") {
        assert(Argh.HyphenStartedSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
    }
    describe("ArgSchema") {
      it("Should take a flag and return a nice Arguments of just that flag.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a flag and a few of the same argument, and understand it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several flags and their arguments, and understand it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-abcdef")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an int flag and parse it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take a double flag and parse it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take a double flag and parse an interesting number.") {
        assert(Argh.ArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return a nice Arguments of just that flag.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take an optional flag and a few of the same argument, and understand it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several optional flags and their arguments, and understand it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an optional int flag and parse it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an optional int flag and parse an interesting number.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take an optional double flag and parse it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take an optional double flag and parse an interesting number.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return nothing") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags and nothing, and understand it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag and parse nothing.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag and parse nothing.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional flag with spaces and return nothing") {
        assert(Argh.ArgSchema(new CharSequenceReader("[ -a ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags with spaces and nothing, and understand it.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[ -abcdef ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag with spaces and parse nothing.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[ -f int ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag with spaces and parse nothing.") {
        assert(Argh.ArgSchema(new CharSequenceReader("[ -f double ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
    }*/
    describe("MultiArgSchema") {
      it("Should take a flag and return a nice Arguments of just that flag.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-a")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take a flag and a few of the same argument, and understand it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-a")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several flags and their arguments, and understand it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-abcdef")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an int flag and parse it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an int flag and parse an interesting number.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-f int")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take a double flag and parse it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take a double flag and parse an interesting number.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("-f double")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return a nice Arguments of just that flag.") {
        val tmp = Argh.MultiArgSchema(new CharSequenceReader("[-a]")).get
        println("ESCAPE")
        val bleh = tmp(new CharSequenceReader("-a")).get
        println("again")
        assert(bleh === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take an optional flag and a few of the same argument, and understand it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("-aaaaa")).get === Arguments.empty.copy(flags = Set('a')))
      }
      it("Should take several optional flags and their arguments, and understand it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("-abbddeff")).get === Arguments.empty.copy(flags = Set('a', 'b', 'd', 'e', 'f')))
      }
      it("Should take an optional int flag and parse it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 0")).get === Arguments.empty.copy(intMap = Map('f' -> 0)))
      }
      it("Should take an optional int flag and parse an interesting number.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("-f 63643")).get === Arguments.empty.copy(intMap = Map('f' -> 63643)))
      }
      it("Should take an optional double flag and parse it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 0.0")).get === Arguments.empty.copy(doubleMap = Map('f' -> 0.0)))
      }
      it("Should take an optional double flag and parse an interesting number.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("-f 636.43")).get === Arguments.empty.copy(doubleMap = Map('f' -> 636.43)))
      }
      it("Should take an optional flag and return nothing") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-a]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags and nothing, and understand it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-abcdef]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag and parse nothing.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-f int]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag and parse nothing.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[-f double]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional flag with spaces and return nothing") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -a ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take several optional flags with spaces and nothing, and understand it.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -abcdef ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional int flag with spaces and parse nothing.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -f int ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take an optional double flag with spaces and parse nothing.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -f double ]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take multiple things in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -lst -abc]")).get(new CharSequenceReader("-ls -aba")).get === Arguments.empty.copy(flags = Set('l', 's', 'a', 'b')))
      }
      it("Should take multiple things, when given nothing, in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -lst -abc]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      it("Should take multiple things, nested.") {
        Argh.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ] ]")).get
      }
      it("Should take multiple things, nested, in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ] ]")).get(new CharSequenceReader("-ls -aba")).get === Arguments.empty.copy(flags=Set('l', 's', 'a', 'b')))
      }
      it("Should take multiple things, and be ok with the one in the outer nesting, in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ]]")).get(new CharSequenceReader("-ls")).get === Arguments.empty.copy(flags=Set('l', 's')))
      }
      it("Should take multiple things, and be ok with neither, in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[ -lst [ -abc ]]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      //TODO MN
      /*
      it("Should take multiple things, and weird nesting, and be ok with taking both in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("-ls -aba")).get === Arguments.empty.copy(flags=Set('l', 's', 'a', 'b')))
      }
      it("Should take multiple things, and weird nesting, and be ok with taking the first in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("-ls")).get === Arguments.empty.copy(flags=Set('l', 's')))
      }
      it("Should take multiple things, and weird nesting, and be ok with taking the second in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("-aba")).get === Arguments.empty.copy(flags=Set('a', 'b')))
      }
      it("Should take multiple things, and weird nesting, and be ok without taking anything in an option.") {
        assert(Argh.MultiArgSchema(new CharSequenceReader("[[ -lst ] [ -abc ]]")).get(new CharSequenceReader("")).get === Arguments.empty)
      }
      */
    }
     describe("FlagsAndStringsSchema") {
       it("Should handle wc args") {
         assert(Argh.FlagsAndStringsSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("-cl")).get ===
           Arguments.empty.copy(flags=Set('c', 'l')))
       }
       it("Should handle wc args with a file") {
         assert(Argh.FlagsAndStringsSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("-cl input.txt")).get ===
           Arguments.empty.copy(flags=Set('c', 'l'), strings=Map("file" -> "input.txt")))
       }
       it("Should handle wc args with only file") {
         assert(Argh.FlagsAndStringsSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("input.txt")).get ===
           Arguments.empty.copy(strings=Map("file" -> "input.txt")))
       }
       it("Should handle wc args without args") {
         assert(Argh.FlagsAndStringsSchema(new CharSequenceReader("[-clmw] [file]")).get(new CharSequenceReader("")).get ===
           Arguments.empty.copy())
       }
       it("Should handle man args without args") {
         assert(Argh.FlagsAndStringsSchema(new CharSequenceReader("[-acdfFhkKtwW] [--path] " +
           "[-m system] [-p string] [-C config_file] [-M pathlist] [-P pager] [-B browser]" +
           "[-H htmlpager] [-S section_list] [section]")).get(new CharSequenceReader("")).get ===
             Arguments.empty)
       }
       //TODO MN
       ignore("Should handle man args with some args") {
         assert(Argh.PhraseFASSchema(new CharSequenceReader("[-acdfFhkKtwW]")).get(new CharSequenceReader("-acfkW")).get ===
             Arguments.empty.copy(flags = Set('a', 'c', 'f', 'k', 'W'), intMap = Map('B' -> 3)))
       }
       //TODO MN
       ignore("Should handle man args with lots of args") {
         assert(Argh.FlagsAndStringsSchema(new CharSequenceReader("[-acdfFhkKtwW] " +
           "[-m int] [-p string] [-C int] [-M int] [-P int] [-B int]" +
           "[-H int] [-S int] [key]")).get(new CharSequenceReader("-acfkW -p crap -B 3 value")).get ===
             Arguments.empty.copy(flags = Set('a', 'c', 'f', 'k', 'W'), intMap = Map('B' -> 3),
                 strings = Map("p" -> "crap", "key" -> "value")))
       }
     }
  }
}