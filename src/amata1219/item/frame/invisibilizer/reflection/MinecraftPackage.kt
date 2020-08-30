package amata1219.item.frame.invisibilizer.reflection

import org.bukkit.Bukkit

enum class MinecraftPackage(prefix: String) {

    NMS("net.minecraft.server"),
    OBC("org.bukkit.craftbukkit");

    private val path: String

    init {
        val bukkitPackageName: String = Bukkit.getServer().javaClass.`package`.name
        val version: String = bukkitPackageName.replaceFirst(Regex(".*(\\d+_\\d+_R\\d+).*"), "$1")
        path = "${prefix}.v${version}"
    }

    override fun toString(): String = path

}