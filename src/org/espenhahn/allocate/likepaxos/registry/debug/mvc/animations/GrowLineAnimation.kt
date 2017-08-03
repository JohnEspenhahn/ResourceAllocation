package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Line
import java.util.function.Consumer

class GrowLineAnimation(l: Line, end_x: Int, end_y: Int, callback: Consumer<Line>) : TranslateAnimation<Line>(l, end_x, end_y, callback) {

    override fun apply(x: Int, y: Int) {
        drawable.translateDelta(x, y)
    }

}
