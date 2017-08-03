package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects

import java.awt.Graphics2D
import java.awt.Stroke

class DrawableOptions(var stroke: Stroke?) {

    constructor(g2: Graphics2D) : this(g2.stroke)

    fun apply(g2: Graphics2D) {
        if (this.stroke != null) g2.stroke = this.stroke
    }

}
