package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.task.HighlightingItemFramesAroundPlayerTask
import amata1219.item.frame.invisibilizer.task.SelfCancelableAsyncTask
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class HighlightingItemFrameListener : Listener {

    val highlightersToTasks = mutableMapOf<Player, SelfCancelableAsyncTask>()


    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        event.player.stopHighlightingItemFramesAround()
    }

    private fun Player.startHighlightingItemFramesAround() {
        HighlightingItemFramesAroundPlayerTask(this, 0)
                .executeTaskTimer(0, 0)
    }

    private fun Player.stopHighlightingItemFramesAround() {
        highlightersToTasks.remove(this)?.self?.cancel()
    }


}