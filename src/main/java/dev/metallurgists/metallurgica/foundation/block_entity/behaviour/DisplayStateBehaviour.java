package dev.metallurgists.metallurgica.foundation.block_entity.behaviour;

import dev.metallurgists.metallurgica.foundation.util.CommonUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public class DisplayStateBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<DisplayStateBehaviour> TYPE = new BehaviourType<>();

    @Getter
    @Setter
    private BlockState displayState;

    public DisplayStateBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public boolean hasDisplayState() {
        return getDisplayState() != null && !getDisplayState().isAir();
    }

    public SoundType getDisplayedSoundType() {
        if (hasDisplayState()) return getDisplayState().getSoundType();
        return blockEntity.getBlockState().getSoundType();
    }

    public boolean canHarvest(Level level, BlockPos blockPos, Player player) {
        if (hasDisplayState()) return getDisplayState().canHarvestBlock(level, blockPos, player);
        return blockEntity.getBlockState().canHarvestBlock(level, blockPos, player);
    }

    public List<ItemStack> getDrops(LootParams.Builder params) {
        if (hasDisplayState()) return getDisplayState().getDrops(params);
        return blockEntity.getBlockState().getDrops(params);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        setDisplayState(CommonUtil.readBlockState(nbt.getCompound("DisplayState")));
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (hasDisplayState())
            nbt.put("DisplayState", NbtUtils.writeBlockState(getDisplayState()));
    }
}
