package com.flechazo.kinddust;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = KindDust.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = KindDust.MODID, value = Dist.CLIENT)
public class KindDustClient {
    public KindDustClient(ModContainer container) {
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }
}
