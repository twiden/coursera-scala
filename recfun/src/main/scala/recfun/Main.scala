package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r)
      1
    else
      pascal(c - 1, r - 1) + pascal(c, r - 1)

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def balanceReq(i: Int, ch: List[Char]): Boolean = {
      if (ch.isEmpty && i == 0)
        true
      else if (ch.isEmpty)
        false
      else if (i < 0)
        false
      else if (ch.head == '(')
        balanceReq(i + 1, ch.tail)
      else if (ch.head == ')')
        balanceReq(i - 1, ch.tail)
      else
        balanceReq(i, ch.tail)
    }

    balanceReq(0, chars)
  }


  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def count(n: Int, m: Int): Int =
      if (n == 0)
        1
      else if (n < 0)
        0
      else if (m < 0 && n >= 1)
        0
      else
        count(n, m - 1) + count(n - coins(m), m)

    count(money, coins.length - 1)
  }

}