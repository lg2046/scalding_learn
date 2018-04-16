package programming_mp

import cascading.pipe.Pipe
import com.twitter.scalding._

class WordCountDemo(args: Args) extends Job(args) {
  val input = TextLine("data/tutorial/hello.txt")
  val output = TextLine("data/tutorial/hello_output.tsv")

  //加性的，line字段还在 flatMapTo只取映射后的字段, 字段名没有变
  input
    .read
    .flatMap('line -> 'word) { line: String => line.split("\\s") }
    .groupBy('word) {
      _.size
    }
    .write(output)



  val inputSchema = List('ProductId, 'price, 'quantity)
  val data: Pipe = MultipleTsvFiles(List(args("input")), inputSchema).read

}


//对pipe的操作
// map
// groupBy
// every 对组里的tuple的操作 count sum
// coGroup 应用于sql的join; inner outer left right