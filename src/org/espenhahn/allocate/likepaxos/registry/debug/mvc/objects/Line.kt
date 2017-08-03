package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects

import java.awt.Graphics2D

class Line @JvmOverloads constructor(x: Int, y: Int, private var dx: Int, private var dy: Int, private val options: DrawableOptions? = null) : Drawable(x, y) {

    fun translateDelta(dx: Int, dy: Int) {
        this.dx += dx
        this.dy += dy
    }

    override fun render(g2: Graphics2D) {
        // Load options
        var old_options: DrawableOptions? = null
        if (options != null) {
            old_options = DrawableOptions(g2)
            options.apply(g2)
        }

        // Render
        g2.drawLine(x, y, x + dx, y + dy)

        // Reset
        if (old_options != null) {
            old_options.apply(g2)
        }
    }

}
