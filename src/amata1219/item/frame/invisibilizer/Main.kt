package amata1219.item.frame.invisibilizer

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    companion object {

        lateinit var INSTANCE: Main
        lateinit var INVISIBLE_KEY: NamespacedKey

    }

    override fun onEnable() {
        INSTANCE = this
        INVISIBLE_KEY = NamespacedKey(this, "invisible")
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this as JavaPlugin)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEntityEvent){
        if (!event.player.isSneaking || event.hand != EquipmentSlot.HAND) return
        val frame: Entity = event.rightClicked
        if (frame !is ItemFrame) return
        val holder: PersistentDataContainer = frame.persistentDataContainer
        if (holder.has(INVISIBLE_KEY, PersistentDataType.BYTE)) holder.remove(INVISIBLE_KEY)
        else holder.set(INVISIBLE_KEY, PersistentDataType.BYTE, 1)
    }
}