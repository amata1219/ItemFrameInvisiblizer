package amata1219.item.frame.invisibilizer

import amata1219.item.frame.invisibilizer.extension.bukkit.highlighters
import amata1219.item.frame.invisibilizer.extension.bukkit.stopHighlightingItemFrames
import amata1219.item.frame.invisibilizer.listener.HighlightingItemFrameListener
import amata1219.item.frame.invisibilizer.listener.InvisibilizingItemFrameListener
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {

    companion object {

        lateinit var INSTANCE: Main

    }

    override fun onEnable() {
        INSTANCE = this

        saveDefaultConfig()

        listOf(
                InvisibilizingItemFrameListener,
                HighlightingItemFrameListener
        ).forEach {
            server.pluginManager.registerEvents(it, this)
        }
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this as JavaPlugin)
        highlighters.forEach(Player::stopHighlightingItemFrames)
    }

}