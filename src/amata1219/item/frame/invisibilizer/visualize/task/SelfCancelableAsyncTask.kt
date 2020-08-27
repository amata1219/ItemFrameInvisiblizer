package amata1219.item.frame.invisibilizer.visualize.task

import amata1219.item.frame.invisibilizer.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

abstract class SelfCancelableAsyncTask : Runnable {

    lateinit var self: BukkitTask

    abstract fun process(self: BukkitTask)

    override fun run() {
        process(self)
    }

    fun executeTask() {
        self = Bukkit.getScheduler().runTaskAsynchronously(
                Main.INSTANCE,
                this
        )
    }

    fun executeTaskLater(delay: Long) {
        self = Bukkit.getScheduler().runTaskLaterAsynchronously(
                Main.INSTANCE,
                this,
                delay
        )
    }

    fun executeTaskTimer(delay: Long, period: Long) {
        self = Bukkit.getScheduler().runTaskTimerAsynchronously(
                Main.INSTANCE,
                this,
                delay,
                period
        )
    }

}