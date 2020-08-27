package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.Main
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.ItemFrame
import org.bukkit.persistence.PersistentDataType.*

private val INVISIBLE_KEY = NamespacedKey(Main.INSTANCE, "invisible")

val ItemFrame.isInvisible: Boolean
    get() = persistentDataContainer.has(INVISIBLE_KEY, BYTE)

val ItemFrame.attachedBlock: Block
    get() = location.block.getRelative(attachedFace)

fun ItemFrame.invisibilize() = persistentDataContainer.set(INVISIBLE_KEY, BYTE, 1.toByte())

fun ItemFrame.visibilize() = persistentDataContainer.remove(INVISIBLE_KEY)