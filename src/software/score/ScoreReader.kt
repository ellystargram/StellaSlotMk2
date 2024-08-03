package software.score

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class ScoreReader {
    val answers:HashMap<Pattern, String> = HashMap()

    init {
        val jsonArray = JSONParser().parse(File("src/software/score/score.json").readText()) as JSONArray

        for (jsonObject in jsonArray) {
            jsonObject as JSONObject
            val score = jsonObject["score"]
            val pattern = jsonObject["pattern"] as JSONArray
            answers[Pattern(pattern[0] as String, pattern[1] as String, pattern[2] as String)] = score as String
        }

        for (answer in answers) {
            println("${answer.key.pattern1} ${answer.key.pattern2} ${answer.key.pattern3} = ${answer.value}")
        }

    }

    fun getScore(pattern: Pattern): String {
        for (answer in answers) {
            if (answer.key.pattern1 == pattern.pattern1 && answer.key.pattern2 == pattern.pattern2 && answer.key.pattern3 == pattern.pattern3) {
                return answer.value
            }
        }
        return "꽝"
    }
}

class Pattern(val pattern1: String, val pattern2: String, val pattern3: String)