package com.example.examplemod;

import com.example.examplemod.BlockCopier.BlockCopier;
import com.example.examplemod.RamenMod.*;
import com.example.examplemod.javaskip.PlayerDeathEventHandler;
import com.example.examplemod.mc_01_myblock.BlockMyBlock;
import com.example.examplemod.mc_02_fortuneblock.BlockFortune;
import com.example.examplemod.mc_03_magicstick.ItemMagicStick;
import com.example.examplemod.mc_04_hipotion.ItemHiPotion;
import com.example.examplemod.mc_05_mysword.ItemMySword;
import com.example.examplemod.mc_06_rainbowblock.BlockRainbow;
import com.example.examplemod.mc_07_soundblock.BlockSound;
import com.example.examplemod.mc_08_woodcut.BlockBreakEventHandler;
import com.example.examplemod.mc_09_redstone.BlockSensor;
import com.example.examplemod.mc_10_snowball_fight.EntityMySnowball;
import com.example.examplemod.mc_10_snowball_fight.ItemMySnowball;
import com.example.examplemod.mc_11_footprints_sand.BlockFootprintsSand;
import com.example.examplemod.mc_12_biome.BiomeMyBiome;
import com.example.examplemod.mc_13_explosive_arrow.EntityExplosiveArrow;
import com.example.examplemod.mc_13_explosive_arrow.ItemExplosiveArrow;
import com.example.examplemod.mc_13_explosive_arrow.RenderExplosiveArrow;
import com.example.examplemod.mc_14_bull_fighting.EntityBull;
import com.example.examplemod.mc_14_bull_fighting.RenderBull;
import com.example.examplemod.mc_15_tobisuke.EntityTobisuke;
import com.example.examplemod.mc_15_tobisuke.ModelTobisuke;
import com.example.examplemod.mc_15_tobisuke.ModelOriginalTobisuke;
import com.example.examplemod.mc_15_tobisuke.RenderTobisuke;
import com.example.examplemod.mc_16_buildingblock.BlockBuilding;
import com.example.examplemod.mc_tips_countblock.BlockCount;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PublicKey;

@Mod(ExampleMod.MODID)
public class ExampleMod {

    //MODID
    public static final String MODID = "examplemod";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    // 自分だけのブロックを作ろう
    public static final Block BLOCK_MYBLOCK =
            new BlockMyBlock().setRegistryName(MODID, "block_myblock");

    // 占いブロックを作ろう
    public static final Block BLOCK_FORTUNE =
            new BlockFortune().setRegistryName(MODID, "block_fortune");

    // 魔法ステッキを作ろう
    public static final Item ITEM_MAGIC_STICK =
            new ItemMagicStick().setRegistryName(MODID, "magic_stick");

    // ハイポーションを作ろう
    public static final Item ITEM_HI_POTION =
            new ItemHiPotion().setRegistryName(MODID, "hi_potion");

    // 虹色ブロックを作ろう
    public static final Block BLOCK_RAINBOW =
            new BlockRainbow().setRegistryName(MODID, "block_rainbow");

    // 雪合戦Modを作ろう
    public static final EntityType<EntityMySnowball> ENTITY_MY_SNOWBALL =
            EntityType.Builder.<EntityMySnowball>of(EntityMySnowball::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("my_snowball");
    public static final Item ITEM_MY_SNOWBALL =
            new ItemMySnowball().setRegistryName(MODID, "my_snowball");

    // 建築Mod
    public static final Block BLOCK_BUILDING =
            new BlockBuilding().setRegistryName(MODID, "block_building_block");

    public static final Item ITEM_MY_SWORD =
            new ItemMySword().setRegistryName(MODID, "my_sword");

    // サウンドブロックを作ろう
    public static final Block BLOCK_SOUND =
            new BlockSound().setRegistryName(MODID, "block_sound");

    // センサーブロックを作ろう
    public static final Block BLOCK_SENSOR =
            new BlockSensor().setRegistryName(MODID, "block_sensor");

    // ブロックコピー機を作ろう
    public static final Item BLOCK_COPIER =
            new BlockCopier().setRegistryName(MODID, "block_copier");

    // 足あと砂を作ろう
    public static final Block BLOCK_FOOTPRINT_SAND =
            new BlockFootprintsSand().setRegistryName(MODID, "block_footprints_sand");

    // バイオームを作ろう
    public static final ResourceKey<Biome> MY_BIOME =
            ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(ExampleMod.MODID, "my_biome"));

    // 爆発する弓矢
    public static final Item ITEM_EXPLOSIVE_ARROW =
            new ItemExplosiveArrow().setRegistryName(MODID, "explosive_arrow");

    public static final EntityType<EntityExplosiveArrow> ENTITY_EXPLOSIVE_ARROW =
            EntityType.Builder.<EntityExplosiveArrow>of(EntityExplosiveArrow::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("explosive_arrow");

    // 闘牛MODを作ろう
    public static final EntityType<EntityBull> ENTITY_BULL =
            EntityType.Builder.of(EntityBull::new, MobCategory.CREATURE)
                    .sized(0.9f, 1.4f)
                    .setTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("bull");

    public static final Item BULL_SPAWN_EGG =
            new SpawnEggItem(ENTITY_BULL,
                    0x00FF00,
                    0x0000FF,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)
            ).setRegistryName(MODID, "bull_spawn_egg");

    // モデル（Mob）を作成しよう
    public static final EntityType<EntityTobisuke> ENTITY_TOBISUKE =
            EntityType.Builder
                    .of(EntityTobisuke::new, MobCategory.CREATURE)
                    .sized(0.9f, 0.9f)
                    .setTrackingRange(32)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("tobisuke");

    public static final Item TOBISUKE_SPAWN_EGG =
            new SpawnEggItem(ENTITY_TOBISUKE,
                    0xFF0000,
                    0x00FF00,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)
            ).setRegistryName(MODID, "tobisuke_spawn_egg");

    // カウントブロックを作ろう
    public static final Block BLOCK_COUNT =
            new BlockCount().setRegistryName(MODID, "block_count");

    public static final Block BLOCK_COUNT2 =
            new BlockCount().setRegistryName(MODID, "block_count2");

    // ラーメンMOD
    public static final Block BLOCK_RAMEN_BOWL =
            new BlockRamenBowl().setRegistryName(MODID, "block_ramen_bowl");

    public static final Block BLOCK_COOKING_POT =
            new BlockCookingPot().setRegistryName(MODID, "block_cooking_pot");

    public static final Block BLOCK_CUTTING_BOARD =
            new BlockCuttingBoard().setRegistryName(MODID, "block_cutting_board");

    public static final Item ITEM_GREEN_ONION =
            new ItemGreenOnion().setRegistryName(MODID, "item_green_onion");

    public static final Item ITEM_MENMA =
            new ItemMenma().setRegistryName(MODID, "item_menma");

    public static final Item ITEM_NOODLE_DOUGH =
            new ItemNoodleDough().setRegistryName(MODID, "item_noodle_dough");

    public static final Item ITEM_NOODLE =
            new ItemNoodle().setRegistryName(MODID, "item_noodle");

    public static final Item ITEM_RAMEN_INGREDIENT_SET =
            new ItemRamenIngredientSet().setRegistryName(MODID, "item_ramen_ingredient_set");

    public static final Item ITEM_COLANDER =
            new ItemColander().setRegistryName(MODID, "item_colander");

    public static final Item ITEM_COLANDER_NOODLE =
            new ItemColanderNoodle().setRegistryName(MODID, "item_colander_noodle");

    public static final Item ITEM_CHOPSTICK =
            new ItemChopstick().setRegistryName(MODID, "item_chopstick");

    public static final Item ITEM_LADLE =
            new ItemLadle().setRegistryName(MODID, "item_ladle");

    public static final Item ITEM_LADLE_SOUP =
            new ItemLadleSoup().setRegistryName(MODID, "item_ladle_soup");

    public static final Item ITEM_KNIFE =
            new ItemKnife().setRegistryName(MODID, "item_knife");

    public ExampleMod() {
        // Tipsの不死アイテム
//        MinecraftForge.EVENT_BUS.register(new PlayerDeathEventHandler());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        EntityRenderers.register(ENTITY_MY_SNOWBALL, ThrownItemRenderer::new);
        EntityRenderers.register(ENTITY_EXPLOSIVE_ARROW, RenderExplosiveArrow::new);
        EntityRenderers.register(ENTITY_BULL, RenderBull::new);
        EntityRenderers.register(ENTITY_TOBISUKE, RenderTobisuke::new);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        private static final RegisterBlockData[] registerBlocks = {
                // ここにBlockを書いてね！

            // 自分だけのブロックを作ろう
            new RegisterBlockData(BLOCK_MYBLOCK),

            // 占いブロックを作ろう
            new RegisterBlockData(BLOCK_FORTUNE),

            // 虹色ブロックを作ろう
            new RegisterBlockData(BLOCK_RAINBOW),

            // 建築Mod
            new RegisterBlockData(BLOCK_BUILDING),

            // サウンドブロックを作ろう
            new RegisterBlockData(BLOCK_SOUND),

            // センサーブロックを作ろう
            new RegisterBlockData(BLOCK_SENSOR),

            // 足あと砂を作ろう
            new RegisterBlockData(BLOCK_FOOTPRINT_SAND),

            // カウントブロックを作ろう
            new RegisterBlockData(BLOCK_COUNT),
            new RegisterBlockData(BLOCK_COUNT2),

            // ラーメンMOD
            new RegisterBlockData(BLOCK_RAMEN_BOWL),
            new RegisterBlockData(BLOCK_COOKING_POT),
            new RegisterBlockData(BLOCK_CUTTING_BOARD),

        };

        private static final Item[] registerItems = {
                // ここにItemを書いてね！

                // 魔法ステッキを作ろう
                ITEM_MAGIC_STICK,

                // ハイポーションを作ろう
                ITEM_HI_POTION,

                // 雪合戦Modを作ろう
                ITEM_MY_SNOWBALL,

                // 自分だけの剣を作ってみよう
                ITEM_MY_SWORD,

                // ブロックコピー機を作ろう
                BLOCK_COPIER,

                // 爆発する弓矢
                ITEM_EXPLOSIVE_ARROW,

                // 闘牛MODを作ろう
                BULL_SPAWN_EGG,

                // モデル（Mob）を作成しよう
                TOBISUKE_SPAWN_EGG,

                // ラーメンMOD
                ITEM_GREEN_ONION,
                ITEM_MENMA,
                ITEM_NOODLE,
                ITEM_RAMEN_INGREDIENT_SET,
                ITEM_COLANDER,
                ITEM_COLANDER_NOODLE,
                ITEM_CHOPSTICK,
                ITEM_LADLE,
                ITEM_LADLE_SOUP,
                ITEM_KNIFE,
                ITEM_NOODLE_DOUGH,
        };

        @SubscribeEvent
        public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {

        }

        @SubscribeEvent
        public static void registerBiomes(RegistryEvent.Register<Biome> event) {
            IForgeRegistry<Biome> registry = event.getRegistry();
            registry.register(BiomeMyBiome.makeMyBiome().setRegistryName(MY_BIOME.location()));
        }

        @SubscribeEvent
        public static void onAttributeCreation(final EntityAttributeCreationEvent event) {
            event.put(ENTITY_BULL, EntityBull.registerAttributes().build());
            event.put(ENTITY_TOBISUKE, EntityTobisuke.registerAttributes().build());
        }

        @SubscribeEvent
        public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event) {
            // 雪合戦Modを作ろう
            event.getRegistry().register(ENTITY_MY_SNOWBALL
                    .setRegistryName(MODID, "my_snowball"));

            // 爆発する弓矢
            event.getRegistry().register(ENTITY_EXPLOSIVE_ARROW
                    .setRegistryName(MODID, "explosive_arrow"));

            // 闘牛MODを作ろう
            event.getRegistry().register(ENTITY_BULL.setRegistryName(MODID, "bull"));

            // モデル（Mob）を作成しよう
            event.getRegistry().register(ENTITY_TOBISUKE.setRegistryName(MODID, "tobisuke"));
            ForgeHooksClient.registerLayerDefinition(RenderTobisuke.modelLayerLocation, ModelOriginalTobisuke::createLayer);

        }

        // ======================================================================================================
        // ここから下はいじらないよ！

        private static void setupBiome(Biome biome, int weight, BiomeManager.BiomeType biomeType, BiomeDictionary.Type... types) {
            ResourceKey<Biome> key = ResourceKey.create(ForgeRegistries.Keys.BIOMES, ForgeRegistries.BIOMES.getKey(biome));

            BiomeDictionary.addTypes(key, types);
            BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(key, weight));
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            LOGGER.info("HELLO from Register Block");
            for (RegisterBlockData data : registerBlocks) {
                event.getRegistry().register(data.block);
            }
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            for (RegisterBlockData data : registerBlocks) {
                event.getRegistry().register(new BlockItem(data.block, new Item.Properties().tab(data.creativeModeTab)).setRegistryName(data.block.getRegistryName()));
            }

            for (Item item : registerItems) {
                event.getRegistry().register(item);
            }
        }

        static class RegisterBlockData {
            Block block;
            CreativeModeTab creativeModeTab;

            public RegisterBlockData(Block block) {
                this.block = block;
                creativeModeTab = CreativeModeTab.TAB_BUILDING_BLOCKS;
            }

            public RegisterBlockData(Block block, CreativeModeTab creativeModeTab) {
                this.block = block;
                this.creativeModeTab = creativeModeTab;
            }
        }
    }
}
