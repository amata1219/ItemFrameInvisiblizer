package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.MainConfig
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
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import java.util.*

object HighlightingItemFrameListener : Listener {

    private val playersWhoHaveAlreadyFlaggedAboutTip4Invisibilizing = mutableSetOf<UUID>()
    private val playersWhoHaveAlreadyFlaggedAboutTip4Highlighting = mutableSetOf<UUID>()

    @EventHandler
    fun on(event: PlayerItemHeldEvent) {
        val player: Player = event.player
        val uuid: UUID = player.uniqueId

        if (playersWhoHaveAlreadyFlaggedAboutTip4Invisibilizing.contains(uuid)) return

        val isItemFrameInPreviousSlot: Boolean = isItemFrame(event.previousItem)
        val isItemFrameInNewSlot: Boolean = isItemFrame(event.newItem)

        if(isItemFrameInPreviousSlot || !isItemFrameInNewSlot) return

        player.sendMessage(MainConfig.tip4InvisibilizingItemFrames)
        playersWhoHaveAlreadyFlaggedAboutTip4Invisibilizing.add(uuid)
    }

    private val PlayerItemHeldEvent.previousItem: ItemStack?
        get() = player.inventory.getItem(previousSlot)

    private val PlayerItemHeldEvent.newItem: ItemStack?
        get() = player.inventory.getItem(newSlot)

    @EventHandler
    fun on(event: PlayerSwapHandItemsEvent) {
        val player: Player = event.player
        val isHoldingItemFrameInMainHand: Boolean = isItemFrame(event.mainHandItem)
        val isHoldingItemFrameInOffHand: Boolean = isItemFrame(event.offHandItem)

        if (!isHoldingItemFrameInMainHand && isHoldingItemFrameInOffHand) {
            player.startHighlightingItemFrames()
            val uuid = player.uniqueId
            if (playersWhoHaveAlreadyFlaggedAboutTip4Highlighting.contains(uuid)) return
            player.sendMessage(MainConfig.tip4HighlightingItemFrames)
            playersWhoHaveAlreadyFlaggedAboutTip4Highlighting.add(uuid)
        } else if (isHoldingItemFrameInMainHand && !isHoldingItemFrameInOffHand) {
            player.stopHighlightingItemFrames()
        }
    }

    private fun isItemFrame(val item: ItemStack?) = item?.type == Material.ITEM_FRAME

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) = event.player.stopHighlightingItemFrames()

    @EventHandler
    fun onItemFramePlace(event: HangingPlaceEvent) {
        val hanging: Entity = event.entity
        if (hanging !is ItemFrame) return
        highlighters.filter {
            it.location.distance(hanging.location) < MainConfig.radiusOfArea2HighlightItemFrames
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