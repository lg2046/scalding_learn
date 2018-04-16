package tutorial

import com.twitter.scalding.{Args, Job, TextLine}

class Demo1(args: Args) extends Job(args) {
  val input = TextLine("data/tutorial/hello.txt")
  val output = TextLine("data/tutorial/hello_output.txt")

  input.read
    .flatMap('line -> 'word) { line: String => line.split("\\s") }
    .project('word)
    .write(output)
  println("hha")
}
