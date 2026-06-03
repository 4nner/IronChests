package anner.ironchest.blocks;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.blockentities.CrystalChestEntity;
import anner.ironchest.blocks.blockentities.GenericChestEntity;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.inventory.MenuType;
import org.jspecify.annotations.Nullable;

public enum ChestTypes {
    NETHERITE(126, 14, "netherite_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/netherite_chest")),
    OBSIDIAN(108, 12, "obsidian_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/obsidian_chest")),
    CRYSTAL(108, 12, "crystal_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/crystal_chest")),
    DIAMOND(108, 12, "diamond_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/diamond_chest")),
    EMERALD(108, 12, "emerald_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/emerald_chest")),
    GOLD(81, 9, "gold_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/gold_chest")),
    IRON(54, 9, "iron_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/iron_chest")),
    COPPER(45, 9, "copper_chest", Identifier.fromNamespaceAndPath(IronChests.MOD_ID, "entity/chest/copper_chest")),
    CHRISTMAS(27, 9, "christmas_chest", Identifier.withDefaultNamespace("entity/chest/christmas")),
    WOOD(27, 9, null, Identifier.withDefaultNamespace("entity/chest/normal"));

    public static final ChestTypes[] PLAYABLE = {
        COPPER, IRON, GOLD, DIAMOND, EMERALD, CRYSTAL, OBSIDIAN, NETHERITE, CHRISTMAS
    };

    public final int size;
    public final int rowLength;
    public final String registryId;
    public final Identifier texture;

    private @Nullable Block block;
    private @Nullable BlockEntityType<? extends ChestBlockEntity> blockEntityType;
    private MenuType<ChestScreenHandler> menuType;

    ChestTypes(int size, int rowLength, String registryId, Identifier texture) {
        this.size = size;
        this.rowLength = rowLength;
        this.registryId = registryId;
        this.texture = texture;
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public void bindBlock(Block block) {
        this.block = block;
    }

    public void bindBlockEntityType(BlockEntityType<? extends ChestBlockEntity> blockEntityType) {
        this.blockEntityType = blockEntityType;
    }

    public void bindMenuType(MenuType<ChestScreenHandler> menuType) {
        this.menuType = menuType;
    }

    public MenuType<ChestScreenHandler> getMenuType() {
        return this.menuType;
    }

    public Block getBlock() {
        return this.block != null ? this.block : Blocks.CHEST;
    }

    public ChestBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (this == CRYSTAL) {
            return new CrystalChestEntity(pos, state);
        }
        return new GenericChestEntity(this, pos, state);
    }

    public BlockEntityType<? extends ChestBlockEntity> getBlockEntityType() {
        return this.blockEntityType != null ? this.blockEntityType : BlockEntityType.CHEST;
    }

    public BlockBehaviour.Properties blockProperties() {
        return switch (this) {
            case COPPER, GOLD -> BlockBehaviour.Properties.of()
                .strength(3.0F, 6.0F)
                .sound(SoundType.COPPER)
                .requiresCorrectToolForDrops();
            case IRON -> BlockBehaviour.Properties.of()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops();
            case DIAMOND, EMERALD -> BlockBehaviour.Properties.of()
                .strength(5.0F, 6.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();
            case CRYSTAL -> BlockBehaviour.Properties.of()
                .strength(3.0F, 3.0F)
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops();
            case OBSIDIAN -> BlockBehaviour.Properties.of()
                .strength(50.0F, 1200.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();
            case NETHERITE -> BlockBehaviour.Properties.of()
                .strength(50.0F, 1200.0F)
                .sound(SoundType.NETHERITE_BLOCK)
                .requiresCorrectToolForDrops();
            case WOOD, CHRISTMAS -> BlockBehaviour.Properties.of()
                .strength(3.0F, 3.0F)
                .sound(SoundType.WOOD);
            default -> BlockBehaviour.Properties.of();
        };
    }
}
