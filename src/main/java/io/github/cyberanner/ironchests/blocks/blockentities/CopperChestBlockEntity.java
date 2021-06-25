package io.github.cyberanner.ironchests.blocks.blockentities;


import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class CopperChestBlockEntity extends BlockEntity {

    // Store the current value of the number
    private int number = 7;

    public CopperChestBlockEntity(BlockPos pos, BlockState state) {
        super((ModBlockEntityType.COPPER_CHEST_ENTITY), pos, state);
    }

    // Serialize the BlockEntity
    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        // SAve the current value of the number to the tag
        tag.putInt("number", number);

        return tag;
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        number = tag.getInt("number");
    }
}
