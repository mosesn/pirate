package pirate

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArgumentsSpec extends FunSpec with ShouldMatchers{
  describe("Arguments") {
    describe("empty") {
      it("Should know empties are empty") {
        assert(Arguments.empty.isEmpty)
      }
      it("Should know other things are empty") {
        assert(Arguments(Set.empty[Char], Map.empty[Char, Int], Map.empty[Char, Double], Map.empty[String, String]).isEmpty)
      }
      it("Should know different things are empty") {
        assert(Arguments(Set[Char](), Map[Char, Int](), Map[Char, Double](), Map[String, String]()).isEmpty)
      }
    }
  }
}