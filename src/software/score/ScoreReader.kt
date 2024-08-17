package software.score

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import javax.sound.sampled.AudioSystem
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ScoreReader {

    private val packList: ArrayList<Pack> = ArrayList()

    init {
//        val jsonArray = JSONParser().parse(File(javaClass.getResourceAsStream("score.json")!!.toString()).readText()) as JSONArray
        val jsonArray = JSONParser().parse(javaClass.getResource("score.json")!!.readText()) as JSONArray
        for (jsonObject in jsonArray) {
            jsonObject as JSONObject
            val output = jsonObject["output"]
            val pattern = jsonObject["pattern"] as JSONArray
            val soundBase64: String? = jsonObject["sound"] as String?

            val slotPattern = SlotPattern(pattern[0] as String, pattern[1] as String, pattern[2] as String)
//            val soundFile: File? = null
            val completePack = Pack(slotPattern, output as String, soundBase64)

            packList.add(completePack)
        }

    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getScore(pattern: SlotPattern): String {
        for (pack in packList) {
            if (pack.slotPattern.pattern1 == pattern.pattern1 && pack.slotPattern.pattern2 == pattern.pattern2 && pack.slotPattern.pattern3 == pattern.pattern3) {
                // play wav by base64
                if (pack.soundBase64 != null) {
                    val decodedBytes = Base64.decode(pack.soundBase64)
                    val soundFile = File.createTempFile("temp", ".wav")
                    soundFile.writeBytes(decodedBytes)

                    val audioInputStream = AudioSystem.getAudioInputStream(soundFile)
                    val clip = AudioSystem.getClip()
                    clip.open(audioInputStream)
                    clip.start()
                }

                return pack.output
            }
        }
        return "꽝"
    }
}

class SlotPattern(val pattern1: String, val pattern2: String, val pattern3: String)
class Pack(val slotPattern: SlotPattern, val output: String, val soundBase64: String?) {

}