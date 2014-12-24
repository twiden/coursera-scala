package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection contains common elements") {
    new TestSets {
      val s = Set(1, 2)
      val t = Set(2, 3)
      val inters = intersect(s, t)
      assert(!contains(inters, 1), "Not 1")
      assert(contains(inters, 2), "2 is OK")
      assert(!contains(inters, 3), "Not 3")
    }
  }

  test("diff contains non common elements") {
    new TestSets {
      val s = Set(1, 2)
      val t = Set(2, 3)
      val difference = diff(s, t)
      assert(contains(difference, 1), "1")
      assert(!contains(difference, 2), "not 2")
      assert(!contains(difference, 3), "3")

      val sx = Set(1, 3, 4, 5, 7, 1000)
      val tx = Set(1, 2, 3, 4)
      val diffx = diff(sx, tx)
      assert(!contains(diffx, 2))
      assert(contains(diffx, 5))
      assert(contains(diffx, 7))
      assert(contains(diffx, 1000))
      assert(!contains(diffx, 1))
      assert(!contains(diffx, 3))
      assert(!contains(diffx, 4))

      val sy = Set(1, 2, 3, 4)
      val ty = Set(-1000, 0)
      val diffy = diff(sy, ty)
      assert(contains(diffy, 1))
      assert(contains(diffy, 2))
      assert(contains(diffy, 3))
      assert(contains(diffy, 4))
      assert(!contains(diffy, -1000))
      assert(!contains(diffy, 0))
    }
  }

  test("filter") {
    new TestSets {
      val s = Set(1, 2, 3, 4)
      assert(!contains(filter(s, (x: Int) => x % 2 == 0), 1), "odd number 1 not in set")
      assert(contains(filter(s, (x: Int) => x % 2 == 0), 2), "even number 2 in set")
      assert(!contains(filter(s, (x: Int) => x % 2 == 0), 3), "odd number 3 not in set")
      assert(contains(filter(s, (x: Int) => x % 2 == 0), 4), "even number 4 in set")
      assert(!contains(filter(s, (x: Int) => x % 2 == 0), 6), "6 not in original set")
    }
  }

  test("forall") {
    new TestSets {
      val s = Set(2, 4)
      assert(forall(s, (x: Int) => x % 2 == 0), "true since all numbers are even")
      assert(!forall(s, (x: Int) => x % 2 == 1), "false since no numbers are odd")

      val t = Set(1, 2, 3, 4)
      assert(!forall(t, (x: Int) => x % 2 == 0), "false since not all numbers are even")
      assert(!forall(t, (x: Int) => x % 2 == 1), "false since not all numbers are odd")
    }
  }

  test("exists") {
    new TestSets {
      val s = Set(1, 2, 3, 4)
      assert(exists(s, (x: Int) => x == 3), "true since 3 in set")
      assert(!exists(s, (x: Int) => x == 5), "false since 5 not in set")
    }
  }

  test("map") {
    new TestSets {
      val s = Set(1, 2)
      assert(!contains(map(s, (x: Int) => x * 2), 1), "false since 1 not in set")
      assert(contains(map(s, (x: Int) => x * 2), 2), "true since 1 * 2 in set")
      assert(!contains(map(s, (x: Int) => x * 2), 3), "false since 3 not in set")
      assert(contains(map(s, (x: Int) => x * 2), 4), "true since 2 * 2 in set")
    }
  }
}
