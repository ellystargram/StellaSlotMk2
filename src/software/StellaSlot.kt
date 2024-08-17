package software

import software.cheat.Cheater
import software.score.Pack
import software.score.ScoreReader
import software.score.SlotPattern
import software.slot.Slot
import software.topbar.TopBar
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.random.Random

class StellaSlot : JFrame() {
    val rollButton: JButton = JButton("Roll")
    val topBar: TopBar = TopBar(this)
    val slots: ArrayList<Slot> = ArrayList()
    val slotPanel: JPanel = JPanel()
    var rolling = false
    val gamePanel: JPanel = JPanel()
    val scoreReader: ScoreReader = ScoreReader()
    val scoreDisplayLabel: JLabel = JLabel()
    val cheater = Cheater()
    val keyInputted:HashMap<Int, Boolean> = HashMap()

    init {
        title = "Stella Slot MK2"
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1200, 800)

        rollButton.addKeyListener(object : KeyAdapter(){
            override fun keyPressed(e: KeyEvent) {
                super.keyPressed(e)
                keyInputted[e.keyCode] = true
                println(e.keyCode)
                if (keyInputted[KeyEvent.VK_SHIFT] == true && (keyInputted[KeyEvent.VK_BACK_QUOTE] == true||keyInputted[KeyEvent.VK_DEAD_TILDE] == true)){
                    cheater.isVisible = true
                    keyInputted.clear()
                }
            }
            override fun keyReleased(e: KeyEvent) {
                super.keyReleased(e)
                keyInputted[e.keyCode] = false
                keyInputted.remove(e.keyCode)
            }
        })
        rollButton.requestFocus()

        gamePanel.layout = BorderLayout()
        add(gamePanel, "Center")

        gamePanel.add(scoreDisplayLabel, "North")
        scoreDisplayLabel.font = Font("Arial", Font.BOLD, 20)
        scoreDisplayLabel.preferredSize = Dimension(0, 50)
        scoreDisplayLabel.horizontalAlignment = JLabel.CENTER

        slotPanel.layout = null
        gamePanel.add(slotPanel, "Center")
        slotPanel.setBounds(0, 0, 1200, 620)

        //slot init
        for (i in 0 until 3) {
            slots.add(Slot())
            slots[i].setBounds(90 + i * 350, 90, 320, 620)
            slotPanel.add(slots[i])
        }

        rollButton.addActionListener {
            if (rolling) {
                rollButton.text = "Roll"
                rolling=false
                slots[0].cheatTarget = cheater.cheatPattern.pattern1
                slots[1].cheatTarget = cheater.cheatPattern.pattern2
                slots[2].cheatTarget = cheater.cheatPattern.pattern3
                val stopThread: Thread = Thread {
                    for (slot in slots) {
                        slot.stopRoll()
                        Thread.sleep(100)
                    }
                    scoreDisplayLabel.text = scoreReader.getScore(SlotPattern(slots[0].imageName, slots[1].imageName, slots[2].imageName))
//                    scoreDisplayLabel.text = scoreReader.getScore(Pack(slots[0].imageName, slots[1].imageName, slots[2].imageName))
                }
                stopThread.start()
            }
            else {
                rolling=true
                rollButton.text = "Stop"
                val startThread: Thread = Thread {
                    for (slot in slots) {
                        slot.rolling = true
                    }
                    for (slot in slots) {
                        slot.startRoll(Random.nextLong(5, 20))
                        Thread.sleep(200)
                    }
                }
                startThread.start()
            }
        }
        rollButton.font=Font("Arial", Font.BOLD, 20)
        rollButton.preferredSize = Dimension(0, 50)

        add(topBar, "North")
        gamePanel.add(rollButton, "South")

        isUndecorated = true
        isVisible = true
    }
}

fun main() {
    val stellaSlot = StellaSlot()
}