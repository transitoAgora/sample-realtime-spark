package spark.transitobh;

import spark.transitobh.model._

object Trafic {

  def route(t:String, routes:List[Route]) = {
    val maxRouteAproximate = t.split(" ")
    .filter(_.length >= 3)
    .map(
      (word:String) => { 
        routes
        //.filter(_.city == ct.city )
        .map((r:Route) => { (word,r.maxPointKeyWord(word),r) })
        .toList
        .sortWith(_._2 > _._2).head 
      }
    )
    .sortWith(_._2 > _._2)
    if(!maxRouteAproximate.isEmpty && maxRouteAproximate(0)._2 > 90){
      maxRouteAproximate(0)._3.name
    }else{
      "-"
    }
  }
}
