package spark.transitobh.util;

import _root_.java.math.MathContext

object FuzzyQueryUtil {
	def fuzzyQuery(a:String,b:String) = SimilarityTool.compareStrings(a,b)
}

/// This class implements string comparison algorithm
/// based on character pair similarity
/// Source: http://www.catalysoft.com/articles/StrikeAMatch.html
object SimilarityTool{
    def compareStrings(str1:String,str2:String):Double = {
        val pairs1 = letterPairs(str1.toUpperCase())
        val pairs2 = letterPairs(str2.toUpperCase())
        var intersection = pairs1.filter(pairs2.contains(_)).size.toFloat + pairs2.filter(pairs1.contains(_)).size.toFloat
        def union = (pairs1.size + pairs2.size).toFloat;
        ((intersection) / union) * 100.0
    }
    def letterPairs(str:String) = for( i <- 0 to str.length-2) yield str.substring(i,i+2);
}