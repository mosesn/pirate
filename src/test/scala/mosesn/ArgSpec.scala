package mosesn

import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArgSpec extends FunSpec {
  describe("Argh") {
    describe("apply") {
      it("Should handle empty help text.") {
        assert(Argh("")("") === Arguments(Set())) 
      }
      
      it("Should handle very simple help text.  Affirmed.") {
        assert(Argh("[ -f ]")("-f") === Arguments(Set('f')))
      }

      it("Should handle very simple help text.  Denied.") {
        assert(Argh("[ -f ]")("") === Arguments(Set()))
      }
    }
  }
	
}