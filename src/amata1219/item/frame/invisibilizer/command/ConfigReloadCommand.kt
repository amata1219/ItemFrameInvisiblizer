package amata1219.item.frame.invisibilizer.command

import amata1219.item.frame.invisibilizer.Main
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object ConfigReloadCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        Main.INSTANCE.reloadConfig()
        sender.sendMessage("${ChatColor.AQUA}ItemFrameInvisibilizerのコンフィグを再読み込みしました。")
        return true
    }

}