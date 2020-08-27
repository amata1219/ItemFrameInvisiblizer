package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.extension.nms.isGlowing
import net.minecraft.server.v1_16_R1.Entity
import net.minecraft.server.v1_16_R1.EntityMagmaCube
import net.minecraft.server.v1_16_R1.EntityTypes
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player

private val highlightersToHighlightedItemFrames: MutableMap<Player, MutableMap<ItemFrame, Int>> = mutableMapOf()

private val glowingMagmaCube = EntityMagmaCube(EntityTypes.MAGMA_CUBE, null).also {
    it.setSize(1, false)
    it.isInvisible = true
    it.isInvulnerable = true
    it.isNoGravity = true
    it.isNoAI = true
    it.isGlowing = true
}

internal fun Player.highlight(target: ItemFrame) {
}

internal fun Player.unhighlight(target: ItemFrame) {

}