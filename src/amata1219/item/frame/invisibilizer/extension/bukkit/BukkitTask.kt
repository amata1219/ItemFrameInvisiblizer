package amata1219.item.frame.invisibilizer.extension.bukkit

import amata1219.item.frame.invisibilizer.Main
import org.bukkit.scheduler.BukkitTask

fun runTaskTimer(delay: Long, period: Long, action: Runnable): BukkitTask {
    return Main.INSTANCE.server.scheduler.runTaskTimer(Main.INSTANCE, action, delay, period)
}