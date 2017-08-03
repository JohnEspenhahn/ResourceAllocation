package org.espenhahn.allocate.likepaxos.registry.debug.mvc.animations

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Text
import java.util.function.Consumer

class TranslateTextAnimation(t: Text, end_x: Int, end_y: Int, callback: Consumer<Text>) : TranslateAnimation<Text>(t, end_x, end_y, callback) {

    override fun apply(x: Int, y: Int) {
        drawable.translate(x, y)
    }

}
