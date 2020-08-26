package amata1219.item.frame.invisibilizer.task

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class HighlightingItemFramesAroundPlayerTask(
        val player: Player,
        val scopeOfHighlighting: Int,
) : SelfCancelableAsyncTask() {

    var ticks: Long = 0
        private set

    override fun process(self: BukkitTask) {

    }

}