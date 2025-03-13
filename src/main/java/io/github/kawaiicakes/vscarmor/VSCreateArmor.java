package io.github.kawaiicakes.vscarmor;

import io.github.kawaiicakes.vscarmor.block.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.NETHERITE_BLOCK;

// TODO (1.1) - Eighths
// TODO (1.1) - Reactive armour
// TODO (1.1) - Alphabet shit.
// TODO (1.1) - Sandbag
// TODO (1.1) - Tooltip includes armour stats
// TODO (1.1) - Borderless variants. Tiling is key.
// TODO (1.1) - Porthole texture/model improvement. Infrastructure is already in place
// TODO (1.1) - LargeWindow
// TODO (1.1) - Hatches, bulkhead doors
// TODO (2.0) - Connecting textures
// TODO - (2.0+) port datagen stuff from Fabric

@SuppressWarnings("removal")
@Mod(VSCreateArmor.MOD_ID)
public class VSCreateArmor {
    public static final String MOD_ID = "vscarmor";

    public static final DeferredRegister<Block> BLOCK_REGISTRY
            = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final DeferredRegister<Item> ITEM_REGISTRY
            = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final List<String> REGISTERED = new ArrayList<>();

    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab("itemGroup.vscarmor_group") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return RegistryObject.create(
                    new ResourceLocation(MOD_ID, "light_armor"),
                    ForgeRegistries.ITEMS
            ).get().getDefaultInstance();
        }
    };

    public VSCreateArmor() {
        @SuppressWarnings("removal")
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        registerArmor();

        BLOCK_REGISTRY.register(modEventBus);
        ITEM_REGISTRY.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SuppressWarnings("removal")
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            for (String id : REGISTERED) {
                RegistryObject<Block> registryObject = RegistryObject.create(
                        new ResourceLocation(MOD_ID, id),
                        ForgeRegistries.BLOCKS
                );

                if (registryObject.isPresent() && registryObject.get() instanceof WindowBlock block) {
                    ItemBlockRenderTypes.setRenderLayer((Block) block, block.getRenderLayer());
                }
            }
        }
    }

    private static <T extends Block> RegistryObject<Block> registerBlock(String id, Supplier<T> baseBlock) {
        REGISTERED.add(id);
        return BLOCK_REGISTRY.register(id, baseBlock);
    }

    /*
        --HELPER METHODS BELOW--
     */
    static void registerArmor() {
        for (String color : colors()) {
            String prefix = color.isEmpty() ? "" : color + "_";

            registerArmorBlockFamily(prefix + "light_armor", 3.0F, 5.0F);
            registerArmorBlockFamily(prefix + "steel_armor", 10.0F, 7.0F);
            registerArmorBlockFamily(prefix + "composite_armor", 28.0F, 8.0F);
            registerArmorBlockFamily(prefix + "reinforced_armor", 50.0F, 20.0F);
        }

        for (String stringId : REGISTERED) {
            ResourceLocation id = new ResourceLocation(MOD_ID, stringId);

            RegistryObject<Block> baseBlock = RegistryObject.create(id, ForgeRegistries.BLOCKS);

            ITEM_REGISTRY.register(
                    id.getPath(), () ->
                            new BlockItem(
                                    baseBlock.get(),
                                    // TODO (1.1) - New organization layout
                                    new Item.Properties().tab(CREATIVE_MODE_TAB)
                            )
            );
        }
    }

    // TODO (1.1) - Add commented colours + patterns.
    public static String[] colors() {
        return new String[] {
                "",
                "white",
                "light_gray",
                "gray",
                "black",
                "brown",
                "red",
                "orange",
                "yellow",
                "lime",
                "green",
                "cyan",
                "light_blue",
                "blue",
                "purple",
                "magenta",
                "pink",
                "4b0",
                "29",
                "31",
                "32",
                "33",
                "dunkelgelb",
                "panzergrau",
                // "parade",
                "rotbraun",
                "ship_lower",
                // rainbow,
                "camo_desert",
                "camo_forest",
                /*
                "camo_jungle",
                "camo_mesa",
                "camo_plains",
                "camo_snow",
                "camo_swamp",
                "camo_taiga"
                 */
                "camo_bush",
                "camo_arctic"
        };
    }

    private static void registerArmorBlockFamily(String id, float hardness, float blastResistance) {
        BlockBehaviour.Properties baseProperties = BlockBehaviour.Properties.copy(NETHERITE_BLOCK)
                .destroyTime(hardness)
                .explosionResistance(blastResistance);

        final RegistryObject<Block> base = registerBlock(id, () -> new Block(baseProperties));
        registerBlock(
                id + "_slab",
                () -> new SlabBlock(
                        baseProperties
                                .destroyTime(hardness * 0.5F)
                                .explosionResistance(blastResistance * 0.5F)
                )
        );
        registerBlock(
                id + "_vertical_slab",
                () -> new VerticalSlabBlock(
                        baseProperties
                                .destroyTime(hardness * 0.5F)
                                .explosionResistance(blastResistance * 0.5F)
                )
        );
        registerBlock(
                id + "_stairs",
                () -> new StairBlock(
                        base.get()::defaultBlockState,
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                )
        );
        registerBlock(
                id + "_vertical_stairs",
                () -> new VerticalStairsBlock(
                        base.get()::defaultBlockState,
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                )
        );
        registerBlock(
                id + "_fence",
                () -> new FenceBlock(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_wall",
                () -> new WallBlock(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_porthole",
                () -> new PortholeBlock(
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_porthole_slab",
                () -> new PortholeSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_porthole_vertical_slab",
                () -> new PortholeVerticalSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_vertical_window",
                () -> new VerticalWindowBlock(
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_vertical_window_slab",
                () -> new VerticalWindowSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_vertical_window_vertical_slab",
                () -> new VerticalWindowVerticalSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_horizontal_window",
                () -> new HorizontalWindowBlock(
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_horizontal_window_slab",
                () -> new HorizontalWindowSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                id + "_horizontal_window_vertical_slab",
                () -> new HorizontalWindowVerticalSlab(
                baseProperties
                        .destroyTime(hardness * 0.25F)
                        .explosionResistance(blastResistance * 0.25F)
                        .isRedstoneConductor((a,b,c) -> true)
                )
        );

        // Waterline Black cannot exist
        if (id.startsWith("black_")) return;

        final RegistryObject<Block> wlBase = registerBlock("wl_" + id, () -> new Block(baseProperties));

        registerBlock(
                "wl_" + id + "_slab",
                () -> new SlabBlock(
                        baseProperties
                                .destroyTime(hardness * 0.5F)
                                .explosionResistance(blastResistance * 0.5F)
                )
        );
        registerBlock(
                "wl_" + id + "_vertical_slab",
                () -> new VerticalSlabBlock(
                        baseProperties
                                .destroyTime(hardness * 0.5F)
                                .explosionResistance(blastResistance * 0.5F)
                )
        );
        registerBlock(
                "wl_" + id + "_stairs",
                () -> new StairBlock(
                        wlBase.get()::defaultBlockState,
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                )
        );
        registerBlock(
                "wl_" + id + "_vertical_stairs",
                () -> new VerticalStairsBlock(
                        wlBase.get()::defaultBlockState,
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                )
        );
        registerBlock(
                "wl_" + id + "_fence",
                () -> new FenceBlock(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_wall",
                () -> new WallBlock(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_porthole",
                () -> new PortholeBlock(
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_porthole_slab",
                () -> new PortholeSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_porthole_vertical_slab",
                () -> new PortholeVerticalSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_vertical_window",
                () -> new VerticalWindowBlock(
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_vertical_window_slab",
                () -> new VerticalWindowSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_vertical_window_vertical_slab",
                () -> new VerticalWindowVerticalSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_horizontal_window",
                () -> new HorizontalWindowBlock(
                        baseProperties
                                .destroyTime(hardness * 0.75F)
                                .explosionResistance(blastResistance * 0.75F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_horizontal_window_slab",
                () -> new HorizontalWindowSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
        registerBlock(
                "wl_" + id + "_horizontal_window_vertical_slab",
                () -> new HorizontalWindowVerticalSlab(
                        baseProperties
                                .destroyTime(hardness * 0.25F)
                                .explosionResistance(blastResistance * 0.25F)
                                .isRedstoneConductor((a,b,c) -> true)
                )
        );
    }
    /*
        HELPER METHODS END
     */
}
