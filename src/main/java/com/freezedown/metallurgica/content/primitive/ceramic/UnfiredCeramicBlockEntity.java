package com.freezedown.metallurgica.content.primitive.ceramic;

import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.util.PistonPushable;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.*;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HEAT_LEVEL;

@PistonPushable
public class UnfiredCeramicBlockEntity extends SmartBlockEntity {
    private float cookTime;
    private ResourceLocation firedBlock;
    private HeatLevel heatLevel = HeatLevel.NONE;
    
    public UnfiredCeramicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        cookTime = -15;
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        ResourceLocation thisBlock = ForgeRegistries.BLOCKS.getKey(this.getBlockState().getBlock());
        String firedBlockPath = thisBlock.getPath().replace("unfired_", "");
        firedBlock = new ResourceLocation(thisBlock.getNamespace(), firedBlockPath);
        
        
        if (MetallurgicaConfigs.server().machineConfig.ceramicConfig.allowBlazeBurners.get()) {
            heatLevel = getHeatLevelOf(level.getBlockState(worldPosition.below()));
        } else {
            if (level.getBlockState(worldPosition.below()).is(MetallurgicaTags.AllBlockTags.CERAMIC_HEAT_SOURCES.tag)) {
                if (level.getBlockState(worldPosition.below()).hasProperty(BlockStateProperties.LIT)) {
                    if (level.getBlockState(worldPosition.below()).getValue(BlockStateProperties.LIT)) {
                        heatLevel = HeatLevel.KINDLED;
                    }
                } else {
                    heatLevel = HeatLevel.KINDLED;
                }
            }
        }
        
        if (!heatLevel.isAtLeast(HeatLevel.SMOULDERING)) {
            return;
        }
        
        if (this.cookTime == -15) {
            cookTime = MetallurgicaConfigs.server().machineConfig.ceramicConfig.ceramicCookTime.get();
        }
        
        if (cookTime > 0) {
            float toSubtract = heatLevel == HeatLevel.SEETHING ? 2 : (heatLevel == HeatLevel.KINDLED ? 1 : 0.5f);
            cookTime -= toSubtract;
        } else {
            this.level.setBlock(this.worldPosition, Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(firedBlock)).defaultBlockState(), 3);
        }
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        cookTime = compound.getFloat("CookTime");
        if (compound.contains("FiredBlock"))
            firedBlock = new ResourceLocation(compound.getString("FiredBlock"));
        super.read(compound, clientPacket);
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("CookTime", cookTime);
        if (firedBlock != null)
            compound.putString("FiredBlock", firedBlock.toString());
        super.write(compound, clientPacket);
    }
    
    public static HeatLevel getHeatLevelOf(BlockState state) {
        if (MetallurgicaConfigs.server().machineConfig.ceramicConfig.allowBlazeBurners.get()) {
            if (state.hasProperty(HEAT_LEVEL)) return state.getValue(HEAT_LEVEL);
            return checkTag(state);
        } else {
            return checkTag(state);
        }
    }
    
    public static HeatLevel checkTag(BlockState state) {
        if (state.is(MetallurgicaTags.AllBlockTags.CERAMIC_HEAT_SOURCES.tag)) {
            if (state.is(MetallurgicaTags.AllBlockTags.LOW_HEAT_SOURCES.tag)) {
                if (hasLit(state))
                    return isLit(state) ? HeatLevel.SMOULDERING : HeatLevel.NONE;
                return HeatLevel.SMOULDERING;
            } else if (state.is(MetallurgicaTags.AllBlockTags.MEDIUM_HEAT_SOURCES.tag)) {
                if (hasLit(state))
                    return isLit(state) ? HeatLevel.KINDLED : HeatLevel.NONE;
                return HeatLevel.KINDLED;
            } else if (state.is(MetallurgicaTags.AllBlockTags.HIGH_HEAT_SOURCES.tag)) {
                if (hasLit(state))
                    return isLit(state) ? HeatLevel.SEETHING : HeatLevel.NONE;
                return HeatLevel.SEETHING;
            }
        }
        return HeatLevel.NONE;
    }
    
    public static boolean hasLit(BlockState state) {
        return state.hasProperty(BlockStateProperties.LIT);
    }
    public static boolean isLit(BlockState state) {
        return state.getValue(BlockStateProperties.LIT);
    }
    
}
