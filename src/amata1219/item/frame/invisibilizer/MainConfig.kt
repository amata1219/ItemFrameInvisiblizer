package amata1219.item.frame.invisibilizer

import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration

object MainConfig {

    private val file: FileConfiguration
            get() = Main.INSTANCE.config

    val tip4InvisibilizingItemFrames: String
        get() = ChatColor.translateAlternateColorCodes(
                '&',
                file.getString("The tip that displays when a player held a item frame in them main hand")!!
        )

    val tip4HighlightingItemFrames: String
        get() = ChatColor.translateAlternateColorCodes(
                '&',
                file.getString("The tip that displays when a player invisibilized a item frame")!!
        )

    val updateIntervalOfHighlighterTask: Long
        get() = file.getLong("The update interval of the highlighter task in seconds") * 20

    val radiusOfArea2HighlightItemFrames: Double
        get() = file.getDouble("The radius of the area to highlight item frames")

}