package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.MainConfig
import amata1219.item.frame.invisibilizer.extension.bukkit.*
import amata1219.item.frame.invisibilizer.extension.bukkit.highlighters
import amata1219.item.frame.invisibilizer.extension.bukkit.stopHighlightingItemFrames
import amata1219.item.frame.invisibilizer.extension.bukkit.unhighlight
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import java.util.*

object HighlightingItemFrameListener : Listener {

    private val playersWhoHaveAlreadyFlaggedAboutTip4Invisibilizing = mutableSetOf<UUID>()

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

        if (!isHoldingItemFrameInMainHand && isHoldingItemFrameInOffHand)
            player.startHighlightingItemFrames()
        else if (isHoldingItemFrameInMainHand && !isHoldingItemFrameInOffHand)
            player.stopHighlightingItemFrames()
    }

    private fun isItemFrame(item: ItemStack?) = item?.type == Material.ITEM_FRAME

    @EventHandler
    fun on(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as? Player ?: return
        if (!(event.clickedInventory == player.inventory && event.slot == 40)) return
        val clickedItemType: Material = event.currentItem?.type ?: return
        if (clickedItemType != Material.ITEM_FRAME) return
        player.stopHighlightingItemFrames()
    }

    @EventHandler
    fun on(event: PlayerChangedWorldEvent) = event.player.unhighlightAllItemFrames()

    @EventHandler
    fun on(event: PlayerQuitEvent) = event.player.stopHighlightingItemFrames()

    @EventHandler
    fun on(event: HangingPlaceEvent) {
        val frame: ItemFrame = event.entity as? ItemFrame ?: return

        highlighters.filter {
            it.world == frame.world
        }.filter {
            it.location.distance(frame.location) < MainConfig.radiusOfArea2HighlightItemFrames
        }.forEach {
            it.highlightAll(frame)
        }
    }

    @EventHandler
    fun on(event: HangingBreakEvent) {
        val frame: ItemFrame = event.entity as? ItemFrame ?: return

        highlighters.forEach {
            it.unhighlight(frame)
        }
    }

}