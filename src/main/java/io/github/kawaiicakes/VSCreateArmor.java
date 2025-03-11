package io.github.kawaiicakes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(VSCreateArmor.MOD_ID)
public class VSCreateArmor {
    public static final String MOD_ID = "vscarmor";

    public static final DeferredRegister<Block> BLOCK_REGISTRY
            = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final DeferredRegister<Item> ITEM_REGISTRY
            = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS
            = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public VSCreateArmor(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        BLOCK_REGISTRY.register(modEventBus);
        ITEM_REGISTRY.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
