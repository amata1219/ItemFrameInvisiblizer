package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.Main
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.ItemFrame
import org.bukkit.persistence.PersistentDataType.*
import org.jooq.tools.reflect.Reflect

private val INVISIBLE_KEY = NamespacedKey(Main.INSTANCE, "invisible")

val ItemFrame.isInvisible: Boolean
    get() = persistentDataContainer.has(INVISIBLE_KEY, BYTE)

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