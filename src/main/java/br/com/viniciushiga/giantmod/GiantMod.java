package br.com.viniciushiga.giantmod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(GiantMod.MODID)
public final class GiantMod {
    public static final String MODID = "giant_mod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public GiantMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        var modBus = FMLCommonSetupEvent.getBus(modBusGroup);
        modBus.addListener(this::commonSetup);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}
}
