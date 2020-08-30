package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.Main.Companion.radiusOfArea2HighlightItemFrames
import amata1219.item.frame.invisibilizer.extension.bukkit.*
import amata1219.item.frame.invisibilizer.extension.bukkit.highlight
import amata1219.item.frame.invisibilizer.extension.bukkit.highlighters
import amata1219.item.frame.invisibilizer.extension.bukkit.stopHighlightingItemFrames
import amata1219.item.frame.invisibilizer.extension.bukkit.unhighlight
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

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

}