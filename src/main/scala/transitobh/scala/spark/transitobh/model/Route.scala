package spark.transitobh.model;
import spark.transitobh.util._

case class Route(keywords:String, id:Long, name:String){

	lazy val keywordsList = keywords.split(",").map(_.trim.toLowerCase)

	def maxPointKeyWord (text:String) =  keywordsList.map(FuzzyQueryUtil.fuzzyQuery(text.toLowerCase,_)).toList.sortWith(_>_).head

}