package com.flechazo.kinddust;

import com.flechazo.kinddust.mc.KindDustCommon;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(KindDust.MODID)
public class KindDust {
    public static final String MODID = KindDustCommon.MOD_ID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public KindDust(IEventBus modEventBus, ModContainer modContainer) {
    }
}
