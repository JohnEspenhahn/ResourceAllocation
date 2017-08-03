package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects

import java.awt.Graphics2D

class Circle(x: Int, y: Int, private val radius: Int) : Drawable(x, y) {

    override fun translate(dx: Int, dy: Int) {
        setRelativePos(this.x + dx, this.y + dy)
    }

    fun rotateAbout(centerX: Int, centerY: Int, degrees: Double) {
        val tX = this.x - centerX
        val tY = this.y - centerY

        val rX = (tX * Math.cos(degrees) - tY * Math.sin(degrees)).toInt() + centerX
        val rY = (tX * Math.sin(degrees) + tY * Math.cos(degrees)).toInt() + centerY

        this.setRelativePos(rX, rY)
    }

    fun addLabel(id: Int, lbl: String) {
        addLabel(id, Text(lbl, 0, id * 20))
    }

    override fun render(g2: Graphics2D) {
        g2.drawOval(x - radius, y - radius, radius * 2, radius * 2)
        this.renderLabels(g2)
    }

}