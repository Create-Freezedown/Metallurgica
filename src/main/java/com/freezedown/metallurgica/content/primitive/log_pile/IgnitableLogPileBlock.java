package com.freezedown.metallurgica.content.primitive.log_pile;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;

public class IgnitableLogPileBlock extends LogPileBlock {
    public IgnitableLogPileBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        int layers = state.getValue(LogPileBlock.LAYERS);
        if (heldItem.is(MetallurgicaTags.AllItemTags.IGNITES_LOG_PILE.tag)) {
            boolean hasDurability = heldItem.isDamageableItem();
            if (!world.isClientSide) {
                if (hasDurability) {
                    heldItem.hurtAndBreak(1, player, (event) -> {
                        event.broadcastBreakEvent(hand);
                    });
                } else {
                    heldItem.shrink(1);
                }
                world.setBlock(pos, MetallurgicaBlocks.charredLogPile.getDefaultState().setValue(LogPileBlock.LAYERS, layers), 3);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }
}
