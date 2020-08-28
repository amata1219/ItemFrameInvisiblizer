package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.extension.kotlin.putIfUnset
import amata1219.item.frame.invisibilizer.reflection.MinecraftPackage.*
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.jooq.tools.reflect.Reflect

private val highlighters2HighlightedItemFrames: MutableMap<Player, MutableMap<ItemFrame, Int>> = mutableMapOf()

private val basicMagmaCube4Highlighting: Any = Reflect.onClass("${NMS}.EntityMagmaCube").create(
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

private fun createMagmaCube4Highlighting(world: World, loc: Location): Pair<Int, Any> {
    val entityId: Int = Reflect.onClass("${NMS}.Entity")
            .field("entityCount")
            .call("incrementAndGet")
            .get()
    return entityId to Reflect.on(basicMagmaCube4Highlighting)
            .set(
                    "id",
                    entityId
            ).set(
                    "world",
                    Reflect.on(world)
                            .call("getHandle")
                            .get()
            ).set(
                    "loc",
                    Reflect.onClass("${NMS}.Vec3D")
                            .create(loc.x, loc.y, loc.z)
            ).get()
}

internal fun Player.highlight(target: ItemFrame) {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames.putIfUnset(this, mutableMapOf())
    if (state.containsKey(target)) return
    val (entityId, magmaCube) = createMagmaCube4Highlighting(world, target.location)
    state[target] = entityId
    sendPacket(
            Reflect.onClass("${NMS}.PacketPlayOutSpawnEntityLiving")
                    .create(magmaCube)
                    .get()
    )
}

internal fun Player.unhighlight(target: ItemFrame) {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this] ?: return
    val entityId: Int = state.remove(target) ?: return
    if (state.isEmpty()) highlighters2HighlightedItemFrames.remove(this)
    sendPacket(
            Reflect.onClass("${NMS}.PacketPlayOutEntityDestroy")
                    .create(entityId)
                    .get()
    )
}

internal fun Player.clearUntrakingItemFrames() {

}

private fun Player.sendPacket(packet: Any) {
    Reflect.on(this)
            .call("getHandle")
            .call("playerConnection")
            .call("sendPacket", packet)
}
