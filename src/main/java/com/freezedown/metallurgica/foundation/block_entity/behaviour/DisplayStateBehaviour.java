package com.freezedown.metallurgica.foundation.block_entity.behaviour;

import com.freezedown.metallurgica.foundation.util.CommonUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
