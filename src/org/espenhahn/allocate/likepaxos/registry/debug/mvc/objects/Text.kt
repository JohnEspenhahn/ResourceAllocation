package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects

import java.awt.Graphics2D

class Text @JvmOverloads constructor(private val lbl: String, x: Int, y: Int, private val options: DrawableOptions? = null) : Drawable(x, y) {

    override fun render(g2: Graphics2D) {
        // Load options
        var old_options: DrawableOptions? = null
        if (options != null) {
            old_options = DrawableOptions(g2)
            options.apply(g2)
        }

        // Render
        val width = g2.fontMetrics.stringWidth(lbl)
        g2.drawString(lbl, x - width / 2, y)

        // Reset
        if (old_options != null) {
            old_options.apply(g2)
        }
    }

}
