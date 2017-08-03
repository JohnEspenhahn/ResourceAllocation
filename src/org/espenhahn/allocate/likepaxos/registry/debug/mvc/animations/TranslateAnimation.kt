package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Drawable
import java.util.function.Consumer

abstract class TranslateAnimation<out T : Drawable>(protected val drawable: T, end_x: Int, end_y: Int, private val callback: Consumer<T>?) : Animation {

    private var steps: Int = 0
    private val x1: Int
    private val y1: Int
    private val dx: Int
    private val dy: Int

    init {
        this.steps = 0
        this.x1 = drawable.x
        this.y1 = drawable.y
        this.dx = ((end_x - x1) * 0.1).toInt()
        this.dy = ((end_y - y1) * 0.1).toInt()
    }

    protected abstract fun apply(x: Int, y: Int)

    override fun animate(): Boolean {
        this.steps += 1
        apply(dx, dy)
        if (this.steps >= 10) {
            callback?.accept(drawable)

            return true
        } else {
            return false
        }
    }

}
