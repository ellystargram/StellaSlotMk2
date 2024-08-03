package software.slot

import java.awt.Color
import java.awt.Dimension
import java.io.File
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.math.abs

class Slot:JPanel() {
    val slotImages:HashMap<String, ImageIcon> = HashMap()
    val slotLabels:ArrayList<JLabel> = ArrayList()
    var rolling = false
    val movement:Int = 60
    var imageName = ""
    var cheatTarget:String = ""
    var delay:Long = 0
    init {
        layout = null
        preferredSize = Dimension(320,620)
        background= Color.black

        val imageDirAddress:String = "src/resources/degari/"
        val imageDir:File = File(imageDirAddress)
        if (!imageDir.exists()){
            throw Exception("Image directory not found")
        }
        if (!imageDir.isDirectory){
            throw Exception("Image directory is not a directory")
        }
        val files = imageDir.listFiles()
        if (files == null){
            throw Exception("No files found in image directory")
        }
        if (files.size <4){
            throw Exception("Not enough images in image directory")
        }

        slotImages["buki"] = ImageIcon(javaClass.getResource("/resources/degari/buki.png"))
        slotImages["nana"] = ImageIcon(javaClass.getResource("/resources/degari/nana.png"))
        slotImages["riko"] = ImageIcon(javaClass.getResource("/resources/degari/riko.png"))
        slotImages["lin"] = ImageIcon(javaClass.getResource("/resources/degari/lin.png"))
        slotImages["tabi"] = ImageIcon(javaClass.getResource("/resources/degari/tabi.png"))
        slotImages["rize"] = ImageIcon(javaClass.getResource("/resources/degari/rize.png"))
        slotImages["hina"] = ImageIcon(javaClass.getResource("/resources/degari/hina.png"))
        slotImages["mashiro"] = ImageIcon(javaClass.getResource("/resources/degari/mashiro.png"))
        slotImages["kanna"] = ImageIcon(javaClass.getResource("/resources/degari/kanna.png"))
        slotImages["yuni"] = ImageIcon(javaClass.getResource("/resources/degari/yuni.png"))

        for (slotImage in slotImages){
            slotImage.value.image = slotImage.value.image.getScaledInstance(300, 300, 1)
            val label = JLabel(slotImage.value)
            slotLabels.add(label)
        }
        for (i in 0 until slotLabels.size){
            val label = slotLabels[i]
            label.setBounds(10, 10 + i * 300 - 150, 300, 300)
            add(label)
        }

//        for (i in 0 until slotImages.size){
//            val label = JLabel(slotImages[i])
//            label.setBounds(10, 10 + i * 300 - 150, 300, 300)
//            slotLabels.add(label)
//            add(label)
//        }
    }

    fun startRoll(delay:Long){
        this.delay = delay
        Thread{
            while (rolling){
                for (i in 0 until slotLabels.size){
                    val label = slotLabels[i]
                    label.setLocation(label.x, label.y - movement)
                    if (label.y < -300){
                        label.setLocation(label.x, label.y + slotLabels.size * 300)
                    }
                }
                Thread.sleep(this.delay)
            }
        }.start()

    }

    fun stopRoll(){
        if (cheatTarget != ""){
            rolling = false
            val findTargetIndex = slotImages.values.indexOfFirst { it == slotImages[cheatTarget] }
            while (abs(slotLabels[findTargetIndex].y - 310+150 )>=10){
                for (i in 0 until slotLabels.size){
                    val label = slotLabels[i]
                    label.setLocation(label.x, label.y - movement)
                    if (label.y < -300){
                        label.setLocation(label.x, label.y + slotLabels.size * 300)
                    }
                }
                Thread.sleep(4)
            }
        }
        var mostCenteredLabelIndex = 0
        val error:HashMap<Int, Int> = HashMap()
        val errorAbs:HashMap<Int, Int> = HashMap()
        for (i in 0 until slotLabels.size){
            val label = slotLabels[i]
            //label_y + 150 is near the 310
            error[i] = label.y + 150 - 310
            errorAbs[i] = abs(error[i]!!)
        }

        for (i in 0 until slotLabels.size){
            if (errorAbs[i]!! < errorAbs[mostCenteredLabelIndex]!!){
                mostCenteredLabelIndex = i
            }
        }

        rolling = false
        if (errorAbs[mostCenteredLabelIndex]!! > 100){
            imageName = "None"
            println("None")
        }
        else{
            var x:Double = 0.0
            val centralizerThread = Thread{
                var leftToGo = slotLabels[mostCenteredLabelIndex].y + 150 - 310
                println(leftToGo)
                while (abs(leftToGo) >= 10){
                    for (i in 0 until slotLabels.size) {
                        val label = slotLabels[i]
                        if (leftToGo > 0){
                            label.setLocation(label.x, (label.y - 10))
                        }
                        else{
                            label.setLocation(label.x, (label.y + 10))
                        }
                        leftToGo = slotLabels[mostCenteredLabelIndex].y + 150 - 310
                    }

                    Thread.sleep(10)
                }
                for (i in 0 until slotLabels.size) {
                    val label = slotLabels[i]
                    label.setLocation(label.x, i*300+160-300*mostCenteredLabelIndex)
                    if (label.y < -300){
                        label.setLocation(label.x, label.y + slotLabels.size * 300)
                    }
                    else if (label.y > (slotLabels.size-1) * 300){
                        label.setLocation(label.x, label.y - slotLabels.size * 300)
                    }
                }
            }
            centralizerThread.start()
            val mostCenteredImageIcon = slotLabels[mostCenteredLabelIndex].icon
            for (candidate in slotImages){
                if (candidate.value == mostCenteredImageIcon){
                    println("Stopped at ${candidate.key}")
                    imageName = candidate.key
                    break
                }
            }
        }


    }
}