package amata1219.item.frame.invisibilizer.extension.nms

import net.minecraft.server.v1_16_R1.Entity

internal var Entity.isGlowing
    get() = getFlag(6)
    set(flag) = setFlag(6, flag)