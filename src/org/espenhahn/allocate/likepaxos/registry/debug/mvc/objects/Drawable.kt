package org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects

import java.awt.Graphics2D
import java.util.*

abstract class Drawable @JvmOverloads constructor(relativeX: Int, relativeY: Int, private var owner: Drawable? = null) {

    private val labels: SortedMap<Int, Text>

    var relativeX: Int
        private set

    var relativeY: Int
        private  set

    init {
        this.relativeX = relativeX
        this.relativeY = relativeY
        this.labels = TreeMap<Int, Text>()
    }

    protected fun setOwner(d: Drawable) {
        this.owner = d
    }

    fun addLabel(id: Int, label: Text) {
        labels.put(id, label)
        label.setOwner(this)
    }

    fun renderLabels(g: Graphics2D) {
        for ((_, value) in labels) {
            value.render(g)
        }
    }

    abstract fun render(g2: Graphics2D)

    fun setRelativePos(x: Int, y: Int) {
        this.relativeX = x
        this.relativeY = y
    }

    val x: Int
        get() = relativeX + (owner?.x ?: 0)

    val y: Int
        get() = relativeY + (owner?.y ?: 0)

    open fun translate(dx: Int, dy: Int) {
        setRelativePos(relativeX + dx, relativeY + dy)
    }
}
