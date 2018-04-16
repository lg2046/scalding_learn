package tutorial

import com.twitter.scalding.mathematics.{ColVector, Matrix, RowVector}
import com.twitter.scalding._

class MatrixDemo(args: Args) extends Job(args) {

  import com.twitter.scalding.mathematics.Matrix._

  val adjacencyMatrix: Matrix[Long, Long, Double] = Csv("data/tutorial/graph.tsv", ",", ('user1, 'user2, 'rel))
    .read
    .toMatrix[Long, Long, Double]('user1, 'user2, 'rel)

  adjacencyMatrix.sumColVectors.write(Tsv("data/tutorial/graph.output.tsv1"))
  adjacencyMatrix.sumRowVectors.write(Tsv("data/tutorial/graph.output.tsv2")) //sumRow就是对每一列的所有行求和

  //矩阵乘法
  (adjacencyMatrix * adjacencyMatrix.transpose).write(Tsv("data/tutorial/graph.output.tsv3"))

  val outdegree: ColVector[Long, Double] = adjacencyMatrix.sumColVectors
  val outdegreeFiltered: ColVector[Long, Double] = outdegree.toMatrix[Int](1)
    .filterValues(_ == 1)
    .binarizeAs[Double].getCol(1)

  outdegreeFiltered.diag.write(Tsv("data/tutorial/graph.output.tsvm"))
  //左乘表示行只留基输出度==1的，即只留下3的数据行
  (outdegreeFiltered.diag * adjacencyMatrix).write(Tsv("data/tutorial/graph.output.tsv4"))

  val normMatrix: Matrix[Long, Long, Double] = adjacencyMatrix.rowL2Normalize //行向量标准化
  (normMatrix * normMatrix.transpose).write(Tsv("data/tutorial/graph.output.tsv5")) //对角线为1

  val aBinary: Matrix[Long, Long, Double] = adjacencyMatrix.binarizeAs[Double]
  val intersectMat: Matrix[Long, Long, Double] = aBinary * aBinary.transpose
  val aSumVct: ColVector[Long, Double] = aBinary.sumColVectors
  val bSumVct: RowVector[Long, Double] = aBinary.sumRowVectors
}
