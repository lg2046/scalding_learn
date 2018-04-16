package programming_mp

import cascading.pipe.Pipe
import com.twitter.scalding._

import scala.util.matching.Regex

case class Product(productID: String, price: Double, quantity: String)

class TextLineParse(args: Args) extends Job(args) {
  val pipe: Pipe = TextLine("data/prog_mp/textline_parse.txt").read

  val pipe2 = pipe.mapTo('line -> ('productID, 'price, 'quantity)) { x: String => parseLine(x) }
    .packTo[Product](('productID, 'price, 'quantity) -> 'product)
    .unpackTo[Product]('product -> ('productID, 'price, 'quantity))

  //对具有相同field的pipe进行组合
  //(pipe2 ++ pipe2).write(Tsv("data/prog_mp/textline_parse.output.tsv"))

  //给pipe2赋一个名字，可以在可视化管道时进行展示
  pipe2.name("name")

  //打印一些内容进行调试
  pipe2.debug

  //字段名修改 可同时修改多个
  pipe.rename('fields -> 'new_field)

  val pattern = new Regex("(?<=\\[)[^]]+(?=\\])")

  def parseLine(s: String): (String, Double, String) = {
    (pattern.findFirstIn(s).get, s.split(" ").toList(1).toDouble, s.split(" ").toList(2))
  }

  //  pipe.joinWithSmaller('productID -> 'productID_, pipe.rename('productID -> 'productID_), joiner = new LeftJoin)

  //  pipe.groupBy(fields) { group => group.operation1.operation2}


  pipe2.groupBy('productID) { group =>
    group
      .sortBy('price).reverse
      .take(10)
      //.reducers(10) 覆盖mp自动计算的reducer数
      .size //每个集合操作产生一个字段  size("count")将字段名从size修改为count
    //.average('price -> 'avgPrice) //类似 .average('age -> 'meanAge) 重命名字段
    //.sum[Double]('price -> 'sumPrice)
  }.write(Tsv("data/prog_mp/textline_parse.output.tsv", writeHeader = true))

  //newPipe.groupAll { group => group.groupOp } //只有一个reducer
}
