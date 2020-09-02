package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.MainConfig
import amata1219.item.frame.invisibilizer.reflection.MinecraftPackage.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.jooq.tools.reflect.Reflect
import java.util.*

private val highlighters2HighlightedItemFrames: MutableMap<Player, MutableMap<ItemFrame, Int>> = mutableMapOf()
private val highlighters2ActiveTasks: MutableMap<Player, BukkitTask> = mutableMapOf()

internal val highlighters: Set<Player>
    get() = highlighters2HighlightedItemFrames.keys

private val basicMagmaCube4Highlighting: Any = Reflect.onClass("${NMS}.EntityMagmaCube").create(
        Reflect.onClass("${NMS}.EntityTypes")
                .field("MAGMA_CUBE")
                .get(),
        Reflect.on(Bukkit.getWorld("world"))
                .call("getHandle")
                .get()
)
        .call("setSize", 2, false)
        .call("setNoGravity", true)
        .call("setNoAI", true)
        .set("glowing", true)
        .call("setFlag", 5, true) //invisibilize
        .call("setFlag", 6, true) //glow
        .call("setInvulnerable", true)
        .get()

internal fun createMagmaCube4Highlighting(world: World, loc: Location): Pair<Int, Any> {
    val entityId: Int = Reflect.onClass("${NMS}.Entity")
            .field("entityCount")
            .call("incrementAndGet")
            .get()
    return entityId to Reflect.on(basicMagmaCube4Highlighting)
            .set(
                    "id",
                    entityId
            ).set(
                    "uniqueID",
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

internal fun createEntityDestroyPacket(entityIds: IntArray): Any {
    return Reflect.onClass("${NMS}.PacketPlayOutEntityDestroy")
            .create(entityIds)
            .get()
}

internal val Player.isHighlightingItemFrames: Boolean
    get() = highlighters2HighlightedItemFrames.contains(this)

internal fun Player.startHighlightingItemFrames() {
    highlighters2HighlightedItemFrames[this] = mutableMapOf()
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutScoreboardTeam").create()
                    .set("a", nameOfTeam4Highlighting) //teamName = IFI:Xx12
                    .set("i", 0) //teamAction = 0(CREATE)
                    .set("f", "never") //collisionRule = pushOwnTeam
                    .set("h", listOf(name)) //members = listOf(playerName)
                    .get()
    )
    val task: BukkitTask = runTaskTimer(0, MainConfig.updateIntervalOfHighlighterTask) {
        unhighlightItemFramesOutOfArea()
        highlightItemFramesInArea()
    }
    highlighters2ActiveTasks[this] = task
}

internal fun Player.stopHighlightingItemFrames() {
    if (!isHighlightingItemFrames) return
    highlighters2ActiveTasks.remove(this)!!.cancel()
    unhighlightAllItemFrames()
    highlighters2HighlightedItemFrames.remove(this)
    receive(
            Reflect.onClass("${NMS}.PacketPlayOutScoreboardTeam").create()
                    .set("a", nameOfTeam4Highlighting) //teamName = IFI:Xx12
                    .set("i", 4) //teamAction = 4(DELETE)
                    .get()
    )
}

internal fun Player.highlightAll(vararg targets: ItemFrame) {
    val uniqueIds: List<String> = targets.mapNotNull(::highlight).map(UUID::toString)

    receive(
            Reflect.onClass("${NMS}.PacketPlayOutScoreboardTeam").create()
                    .set("a", nameOfTeam4Highlighting) //teamName = IFI:Xx12
                    .set("i", 3) //teamAction = 3(JOIN)
                    .set("f", "never")
                    .set("h", uniqueIds) //members = listOf(magmaCubeUniqueId)
                    .get()
    )
}

private fun Player.highlight(target: ItemFrame): UUID? {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this]!!
    if (state.containsKey(target)) return null

    val loc: Location = target.attachedBlock.location.add(0.5, 0.0, 0.5)
    val (entityId, magmaCube) = createMagmaCube4Highlighting(world, loc)
    state[target] = entityId

    receive(
            Reflect.onClass("${NMS}.PacketPlayOutSpawnEntityLiving")
                    .create(magmaCube)
                    .set("j", 0.toByte())
                    .set("k", 0.toByte())
                    .set("l", 0.toByte())
                    .get()
    )

    receive(
            Reflect.onClass("${NMS}.PacketPlayOutEntityMetadata").create(
                    entityId,
                    Reflect.on(magmaCube)
                            .call("getDataWatcher")
                            .get(),
                    true
            ).get()
    )

    return Reflect.on(magmaCube).get("uniqueID")
}

internal fun Player.highlightItemFramesInArea() {
    val radius: Double = MainConfig.radiusOfArea2HighlightItemFrames
    val frames: Array<ItemFrame> = getNearbyEntities(radius, radius, radius)
            .filterIsInstance(ItemFrame::class.java)
            .toTypedArray()
    highlightAll(*frames)
}

internal fun Player.unhighlight(vararg targets: ItemFrame) {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this]!!
    val entityIds: IntArray = targets.mapNotNull {
        state.remove(it)
    }.toIntArray()
    receive(createEntityDestroyPacket(entityIds))
}

internal fun Player.unhighlightItemFramesOutOfArea() {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this] ?: return
    val targets: Array<ItemFrame> = state.keys.filter {
        it.location.distance(location) > MainConfig.radiusOfArea2HighlightItemFrames
    }.toTypedArray()
    if (targets.isNotEmpty()) unhighlight(*targets)
}

internal fun Player.unhighlightAllItemFrames() {
    val state: MutableMap<ItemFrame, Int> = highlighters2HighlightedItemFrames[this] ?: return
    val targets: Array<ItemFrame> = state.keys.toTypedArray()
    if (targets.isNotEmpty()) unhighlight(*targets)
}

internal val Player.nameOfTeam4Highlighting: String
    get() = "IFI-${uniqueId.toString().substring(0, 12)}"

private fun Player.receive(packet: Any) {
    Reflect.on(this)
            .call("getHandle")
            .field("playerConnection")
            .call("sendPacket", packet)
}
