package com.freezedown.metallurgica.content.metalworking.forging.block;

import com.freezedown.metallurgica.foundation.block_entity.IntelligentBlockEntity;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class WorkedMetalBlockEntity extends IntelligentBlockEntity {
    private List<Vec3> positions = List.of();
    public LazyOptional<IItemHandlerModifiable> itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(this.inventory));
    public SmartInventory inventory = (new SmartInventory(1, this)).forbidInsertion().withMaxStackSize(1);

    public WorkedMetalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isComplete()
    {
        return positions.isEmpty();
    }

    public List<Vec3> getVoxelPositions() {
        return positions;
    }

    public void onClicked(float hitX, float hitZ)
    {
        int xPos = (int) (hitX * 4);
        int zPos = (int) (hitZ * 4);
        positions.add(new Vec3(xPos, 0, zPos));

        assert level != null;
        if (!level.isClientSide)
        {
            if (isComplete())
            {
                final ItemStack currentItem = inventory.getStackInSlot(0);
                //final ScrapingRecipe recipe = getRecipe(currentItem);
                //if (recipe != null)
                {
                    //final ItemStack extraDrop = recipe.getExtraDrop().getSingleStack(currentItem);
                    //if (!extraDrop.isEmpty()) {
                    //    level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), extraDrop));
                    //}
                    inventory.setStackInSlot(0, AllItems.IRON_SHEET.asStack());
                }
            }
            markForBlockUpdate();
        }
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        //positions = tag.getShort("positions");
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        //tag.putShort("positions", positions);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
