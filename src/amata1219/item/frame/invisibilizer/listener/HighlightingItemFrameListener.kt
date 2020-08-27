package amata1219.item.frame.invisibilizer.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.PlayerQuitEvent

object HighlightingItemFrameListener : Listener {

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {

    }

    @EventHandler
    fun onItemFramePlace(event: HangingPlaceEvent) {

    }

    @EventHandler
    fun onItemFrameBreak(event: HangingBreakEvent) {

    }

}