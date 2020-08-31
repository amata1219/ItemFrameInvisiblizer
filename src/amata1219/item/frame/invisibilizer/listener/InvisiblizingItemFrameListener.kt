package amata1219.item.frame.invisibilizer.listener

import amata1219.item.frame.invisibilizer.extension.bukkit.invisibilize
import amata1219.item.frame.invisibilizer.extension.bukkit.isInvisible
import amata1219.item.frame.invisibilizer.extension.bukkit.visibilize
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot

object InvisiblizingItemFrameListener : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEntityEvent){
        if (!(event.player.isSneaking && event.hand == EquipmentSlot.HAND)) return
        val frame = event.rightClicked as? ItemFrame ?: return
        if (frame.isInvisible) frame.visibilize()
        else frame.invisibilize()
        event.isCancelled = true
    }

}