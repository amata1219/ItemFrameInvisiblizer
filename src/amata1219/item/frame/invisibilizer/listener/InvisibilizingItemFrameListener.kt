package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.MainConfig
import amata1219.item.frame.invisibilizer.extension.bukkit.actionbar
import amata1219.item.frame.invisibilizer.extension.bukkit.invisibilize
import amata1219.item.frame.invisibilizer.extension.bukkit.isInvisible
import amata1219.item.frame.invisibilizer.extension.bukkit.visibilize
import org.bukkit.ChatColor
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot.*
import java.util.*

object InvisibilizingItemFrameListener : Listener {

    private val playersWhoHaveAlreadyFlaggedAboutTip4Highlighting = mutableSetOf<UUID>()

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEntityEvent){
        val player: Player = event.player
        if (!player.isSneaking) return

        val frame = event.rightClicked as? ItemFrame ?: return

        when {
            event.hand == OFF_HAND -> {
                event.isCancelled = true
                return
            }
            event.hand != HAND -> return
        }

        if (frame.isInvisible) {
            frame.visibilize()
            player.actionbar("${ChatColor.AQUA}クリックした額縁を透明化しました！")
        } else {
            frame.invisibilize()
            player.actionbar("${ChatColor.AQUA}クリックした額縁を可視化しました！")
            val uuid = player.uniqueId
            if (!playersWhoHaveAlreadyFlaggedAboutTip4Highlighting.contains(uuid)) {
                player.sendMessage(MainConfig.tip4HighlightingItemFrames)
                playersWhoHaveAlreadyFlaggedAboutTip4Highlighting.add(uuid)
            }
        }
        event.isCancelled = true
    }

}