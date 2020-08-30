package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.Main
import amata1219.item.frame.invisibilizer.Main.Companion.radiusOfArea2HighlightItemFrames
import amata1219.item.frame.invisibilizer.extension.bukkit.*
import amata1219.item.frame.invisibilizer.extension.bukkit.highlight
import amata1219.item.frame.invisibilizer.extension.bukkit.highlighters
import amata1219.item.frame.invisibilizer.extension.bukkit.stopHighlightingItemFrames
import amata1219.item.frame.invisibilizer.extension.bukkit.unhighlight
import net.minecraft.server.v1_16_R1.EntityMagmaCube
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftMagmaCube
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object HighlightingItemFrameListener : Listener {

    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val player: Player = event.player
        val prevIsItemFrame: Boolean = event.previousItem?.type == Material.ITEM_FRAME
        val newIsItemFrame: Boolean = event.newItem?.type == Material.ITEM_FRAME
        if (!prevIsItemFrame && newIsItemFrame) {
            player.startHighlightingItemFrames()
        }else if(prevIsItemFrame && !newIsItemFrame) {
            player.stopHighlightingItemFrames()
        }
    }

    private val PlayerItemHeldEvent.previousItem: ItemStack?
        get() = player.inventory.getItem(previousSlot)

    private val PlayerItemHeldEvent.newItem: ItemStack?
        get() = player.inventory.getItem(newSlot)

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) = event.player.stopHighlightingItemFrames()

    @EventHandler
    fun onItemFramePlace(event: HangingPlaceEvent) {
        val hanging: Entity = event.entity
        if (hanging !is ItemFrame) return
        highlighters.filter {
            it.location.distance(hanging.location) < radiusOfArea2HighlightItemFrames
        }.forEach {
            it.highlight(hanging)
        }
    }

    @EventHandler
    fun onItemFrameBreak(event: HangingBreakEvent) {
        val hanging: Entity = event.entity
        if (hanging !is ItemFrame) return
        highlighters.forEach {
            it.unhighlight(hanging)
        }
    }

    @EventHandler
    fun onSummonMagmaCube4Debug(event: AsyncPlayerChatEvent) {
        Main.INSTANCE.server.scheduler.runTask(Main.INSTANCE, Runnable {
            val player = event.player
            val world = player.world
            when (event.message) {
                "nms" -> {
                    val entity = createMagmaCube4Highlighting(world, player.location).second as EntityMagmaCube
                    (world as CraftWorld).handle.addEntity(entity)
                }
                "bukkit" -> {
                    val cube: MagmaCube = world.spawnEntity(player.location, EntityType.MAGMA_CUBE) as MagmaCube
                    cube.setGravity(false)
                    cube.setAI(false)
                    cube.isGlowing = true
                    cube.isInvulnerable = true
                    cube.size = 2
                    cube.addPotionEffect(PotionEffect(
                            PotionEffectType.INVISIBILITY,
                            100000,
                            0,
                            false,
                            false,
                    ))

                    val craft = cube as CraftMagmaCube

                    /*
                    handle.setSize(2, true)
                    ai, invulnerable, gravity 不要
                    handle.glowing = true, handle.setFlag(6, true)
                    handle.invulnerable = true
                    https://www.spigotmc.org/threads/spawn-invisible-glowing-shulker-with-nms.417766/
                     */
                }
            }
        })
    }

}