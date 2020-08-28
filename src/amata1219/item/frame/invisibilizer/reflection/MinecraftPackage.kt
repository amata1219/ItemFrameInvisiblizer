package amata1219.item.frame.invisibilizer.reflection

import org.bukkit.Bukkit

enum class MinecraftPackage(prefix: String) {

    NMS("net.minecraft.server"),
    OBC("org.bukkit.craftbukkit");

    private val path: String

    init {
        val version: String = Bukkit.getServer().javaClass.`package`.name.replaceFirst(".*(\\d+_\\d+_R\\d+).*", "$1")
        path = "${prefix}.${version}"
    }

    override fun toString(): String = path

}