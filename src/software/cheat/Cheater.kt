package software.cheat

import software.score.Pack
import software.score.SlotPattern
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextField

class Cheater :JFrame(){
    var cheatPattern = SlotPattern("", "", "")
    val commandPrompt = JTextField()
    val commandButton = JButton("execute")
    init {
        title = "Cheater"
        setSize(600, 300)
        defaultCloseOperation = HIDE_ON_CLOSE
        add(commandPrompt, "Center")
        add(commandButton, "South")

        commandButton.addActionListener {
            val command = commandPrompt.text
            if (command.matches(Regex("sudo 1\\w* 2\\w* 3\\w*"))){
                val commandArray = command.split(" ")
                val slot1 = commandArray[1].split("1")[1]
                val slot2 = commandArray[2].split("2")[1]
                val slot3 = commandArray[3].split("3")[1]
                cheatPattern = SlotPattern(slot1, slot2, slot3)
            }
            isVisible = false
        }
    }
}