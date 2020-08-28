package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.reflection.MinecraftPackage.*
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.jooq.tools.reflect.Reflect

private val highlighters2HighlightedItemFrames: MutableMap<Player, MutableMap<ItemFrame, Int>> = mutableMapOf()

private val magmaCube4Highlighting: Any = Reflect.onClass("${NMS}.EntityMagmaCube").create(
        Reflect.onClass("${NMS}.EntityTypes").field("MAGMA_CUBE").get(),
        null
).apply {
    call("setSize", 1, false)
    call("setNoGravity", true)
    call("setNoAI", true)
    call("setFlag", 5, true) //invisibilize
    call("setFlag", 6, true) //glow
    //it.set("isInvulnerable, true)
}.get()

internal fun Player.highlight(target: ItemFrame) {
}

internal fun Player.unhighlight(target: ItemFrame) {

}

internal fun sendTo(player: Player) {
    Reflect.on(player)
            .call("getHandle")
            .field("playerConnection")
            .call("sendPacket", this)
}