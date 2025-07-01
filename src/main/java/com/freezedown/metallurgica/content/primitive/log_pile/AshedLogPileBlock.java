package com.freezedown.metallurgica.content.primitive.log_pile;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class AshedLogPileBlock extends LogPileBlock {
    public AshedLogPileBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        int layers = state.getValue(LogPileBlock.LAYERS);
        if (heldItem.is(MetallurgicaTags.AllItemTags.WATER_CONTAINERS.tag) || (heldItem.getItem() instanceof PotionItem && PotionUtils.getPotion(heldItem) == Potions.WATER)) {
            boolean hasRemainder = !heldItem.getCraftingRemainingItem().isEmpty();
            if (!world.isClientSide) {
                world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (hasRemainder)
                    player.setItemInHand(hand, ItemUtils.createFilledResult(heldItem.getCraftingRemainingItem(), player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
                ServerLevel serverlevel = (ServerLevel) world;
                
                for(int i = 0; i < 5; ++i) {
                    serverlevel.sendParticles(ParticleTypes.SPLASH, (double)pos.getX() + world.random.nextDouble(), pos.getY() + 1, (double)pos.getZ() + world.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
                
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                world.setBlockAndUpdate(pos, MetallurgicaBlocks.charcoalPile.get().defaultBlockState().setValue(LogPileBlock.LAYERS, layers));
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }
    
}
