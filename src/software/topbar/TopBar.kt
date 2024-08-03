package software.topbar

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import kotlin.system.exitProcess

class TopBar(target: JFrame) : JLabel() {
    var clickedLocation: Point? = null
    val closeButton: JButton = JButton("X")

    init{
        preferredSize=Dimension(0, 30)
        text="Stella Slot MK2"
        font=Font("Arial", Font.BOLD, 20)
        horizontalAlignment=JLabel.CENTER
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                super.mousePressed(e)
                clickedLocation = e.point
            }
        })
        addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                super.mouseDragged(e)
                clickedLocation?.let {
                    val current = e.point
                    val jf = target.location
                    val xMoved = current.x - it.x
                    val yMoved = current.y - it.y
                    val x = jf.x + xMoved
                    val y = jf.y + yMoved
                    target.setLocation(x, y)
                }
            }
        })
        background = Color.BLACK
        foreground = Color.WHITE
        isOpaque = true
        layout = BorderLayout()
        add(closeButton, "East")
        closeButton.background = Color.RED
        closeButton.isOpaque = true
        closeButton.foreground = Color.WHITE
        closeButton.addActionListener {
            exitProcess(0)
        }
        target.add(this, "North")
    }
}