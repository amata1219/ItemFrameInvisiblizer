package amata1219.item.frame.invisibilizer

import amata1219.item.frame.invisibilizer.listener.HighlightingItemFrameListener
import amata1219.item.frame.invisibilizer.listener.InvisiblizingItemFrameListener
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    companion object {

        lateinit var INSTANCE: Main

        val radiusOfArea2HighlightItemFrames: Double
            get() = INSTANCE.config.getDouble("The radius of the area to highlight item frames")

    }

    override fun onEnable() {
        INSTANCE = this

        listOf(
                InvisiblizingItemFrameListener,
                HighlightingItemFrameListener
        ).forEach {
            server.pluginManager.registerEvents(it, this)
        }
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this as JavaPlugin)
    }

}