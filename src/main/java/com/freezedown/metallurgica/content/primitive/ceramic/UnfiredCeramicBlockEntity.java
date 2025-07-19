package com.freezedown.metallurgica.content.primitive.ceramic;

import com.freezedown.metallurgica.content.primitive.pit_smelting.PitFuelRecipe;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.util.PistonPushable;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.*;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HEAT_LEVEL;

@PistonPushable
public class UnfiredCeramicBlockEntity extends SmartBlockEntity {
    @Getter
    private float cookTime;
    @Getter
    private float fakeCookTime;
    private ResourceLocation firedBlockKey;
    @Getter
    @Setter
    private boolean hasFuel;
    
    public UnfiredCeramicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        cookTime = -15;
        fakeCookTime = -15;
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
        firedBlockKey = new ResourceLocation(thisBlock.getNamespace(), firedBlockPath);
        Block firedBlock = BuiltInRegistries.BLOCK.get(firedBlockKey);
        if (cookTime <= 0) cookTime = -15;
        if (fakeCookTime <= 0) fakeCookTime = -15;


        if (!isCooking()) return;

        int countdown = 80;

        if (!isEncased()) {
            countdown--;
            if (countdown <= 0) {
                fakeCookTime = cookTime - this.level.random.nextInt(MetallurgicaConfigs.server().machineConfig.ceramicConfig.ceramicCookTime.get() / 2);
                cookTime = -15;
                countdown = 80;
            }
        }

        if (!(fakeCookTime > 0)) {
            if (cookTime > 0) {
                cookTime--;
            } else {
                if (firedBlock != null) {
                    this.level.setBlockAndUpdate(this.worldPosition, firedBlock.defaultBlockState());
                }
                setHasFuel(false);
            }
        } else if (!(cookTime > 0)) {
            if (fakeCookTime > 0) {
                fakeCookTime--;
            } else {
                setHasFuel(false);
            }
        }
    }

    public InteractionResult addFuel(Player player) {
        Inventory inventory = player.getInventory();
        Optional<PitFuelRecipe> recipe = MetallurgicaRecipeTypes.pit_fuel.find(inventory, this.level);
        if (recipe.isEmpty()) {
            this.sendData();
            return tryLight(player);
        } else {
            inventory.getSelected().shrink(1);
            setHasFuel(true);
            this.sendData();
            return InteractionResult.SUCCESS;
        }
    }

    public InteractionResult tryLight(Player player) {
        Inventory inventory = player.getInventory();
        if (inventory.getSelected().is(MetallurgicaTags.AllItemTags.IGNITES_LOG_PILE.tag)) {
            if (this.cookTime == -15 && isHasFuel()) {
                cookTime = MetallurgicaConfigs.server().machineConfig.ceramicConfig.ceramicCookTime.get();
                this.sendData();
                inventory.getSelected().hurtAndBreak(1, player, (p) -> {
                    p.broadcastBreakEvent(player.getUsedItemHand());
                });
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    public boolean isEncased() {
        if (this.level == null) return false;
        int coveredSides = 0;
        for (Direction direction : Direction.values()) {
            BlockState state = this.level.getBlockState(this.worldPosition.relative(direction));
            if (state.isCollisionShapeFullBlock(this.level, this.worldPosition.relative(direction))) {
                coveredSides++;
            }
        }
        return  coveredSides >= Direction.values().length;
    }

    public boolean isCooking() {
        return this.cookTime > 0 || this.fakeCookTime > 0;
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        cookTime = compound.getFloat("CookTime");
        fakeCookTime = compound.getFloat("FakeCookTime");
        if (compound.contains("FiredBlock"))
            firedBlockKey = new ResourceLocation(compound.getString("FiredBlock"));
        super.read(compound, clientPacket);
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("CookTime", cookTime);
        compound.putFloat("FakeCookTime", fakeCookTime);
        if (firedBlockKey != null)
            compound.putString("FiredBlock", firedBlockKey.toString());
        super.write(compound, clientPacket);
    }
}
