package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.Main
import amata1219.item.frame.invisibilizer.reflection.MinecraftPackage.*
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.jooq.tools.reflect.Reflect
import java.util.*

private val highlighters2HighlightedItemFrames: MutableMap<Player, MutableMap<ItemFrame, Int>> = mutableMapOf()

internal val highlighters: Set<Player>
    get() = highlighters2HighlightedItemFrames.keys

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
                    "uniqueId",
                    UUID.randomUUID()
            ).set(
                    "world",
                    Reflect.on(world)
                            .call("getHandle")
                            .get()
            ).set(
                    "loc",
                    Reflect.onClass("${NMS}.Vec3D")
                            .create(loc.x, loc.y, loc.z)
                            .get()
            ).get()
}

internal val Player.isHighlightingItemFrames: Boolean
    get() = highlighters2HighlightedItemFrames.contains(this)

internal fun Player.startHighlightingItemFrames() {
    highlighters2HighlightedItemFrames[this] = mutableMapOf()
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutScoreboardTeam")
                    .set("a", nameOfTeam4Highlighting) //teamName = IFI:Xx12
                    .set("i", 0) //teamAction = 0(CREATE)
                    .set("e", "pushOwnTeam") //collisionRule = pushOwnTeam
                    .set("h", listOf(name)) //members = listOf(playerName)
                    .get()
    )
}

internal fun Player.stopHighlightingItemFrames() {
    if (!isHighlightingItemFrames) return
    unhighlightAllItemFrames()
    highlighters2HighlightedItemFrames.remove(this)
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutScoreboardTeam")
                    .set("a", nameOfTeam4Highlighting) //teamName = IFI:Xx12
                    .set("i", 4) //teamAction = 4(DELETE)
                    .get()
    )
}

internal fun Player.highlight(target: ItemFrame) {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this]!!
    if (state.containsKey(target)) return
    val (entityId, magmaCube) = createMagmaCube4Highlighting(world, target.attachedBlock.location)
    state[target] = entityId
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutSpawnEntityLiving")
                    .create(magmaCube)
                    .get()
    )
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutScoreboardTeam")
                    .set("a", nameOfTeam4Highlighting) //teamName = IFI:Xx12
                    .set("i", 0) //teamAction = 3(JOIN)
                    .set("h", listOf(
                            (Reflect.on(magmaCube)
                                    .field("uniqueId")
                                    .get() as UUID)
                                    .toString()
                    )) //members = listOf(magmaCubeUniqueId)
    )
}

internal fun Player.highlightItemFramesInArea(radius: Double) {
    getNearbyEntities(radius, radius, radius)
            .filterIsInstance(ItemFrame::class.java)
            .forEach(::highlight)
}

internal fun Player.unhighlight(target: ItemFrame) {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this]!!
    val entityId: Int = state.remove(target) ?: return
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutEntityDestroy")
                    .create(entityId)
                    .get()
    )
}

internal fun Player.unhighlightItemFramesOutOfArea(radius: Double) {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this] ?: return
    state.keys.filter {
        it.location.distance(location) > radius
    }.forEach(::unhighlight)
}

internal fun Player.unhighlightAllItemFrames() {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this] ?: return
    state.keys.toList().forEach(::unhighlight)
}

internal val Player.nameOfTeam4Highlighting: String
    get() = "IFI:${uniqueId.toString().substring(0, 11)}"

private fun Player.receive(packet: Any) {
    Reflect.on(this)
            .call("getHandle")
            .call("playerConnection")
            .call("sendPacket", packet)
}
