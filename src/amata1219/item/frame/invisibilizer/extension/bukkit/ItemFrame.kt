package amata1219.item.frame.invisibilizer.extension.bukkit

import org.bukkit.block.Block
import org.bukkit.entity.ItemFrame
import org.jooq.tools.reflect.Reflect

val ItemFrame.isInvisible: Boolean
    get() = Reflect.on(this)
            .call("getHandle")
            .call("isInvisible")
            .get() as Boolean

val ItemFrame.attachedBlock: Block
    get() = location.block.getRelative(attachedFace)

fun ItemFrame.invisibilize() {
    Reflect.on(this)
            .call("getHandle")
            .call("setInvisible", true)
}

fun ItemFrame.visibilize() {
    Reflect.on(this)
            .call("getHandle")
            .call("setInvisible", false)
}