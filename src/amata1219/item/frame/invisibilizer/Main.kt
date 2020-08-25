package amata1219.item.frame.invisibilizer

import amata1219.item.frame.invisibilizer.listener.InvisiblizingItemFrameListener
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    companion object {

        lateinit var INSTANCE: Main

    }

    override fun onEnable() {
        INSTANCE = this
        server.pluginManager.registerEvents(InvisiblizingItemFrameListener(), this)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this as JavaPlugin)
    }

}