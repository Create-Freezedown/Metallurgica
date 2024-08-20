package com.freezedown.metallurgica.content.primitive.clamps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClampsItem extends Item {
    
    public ClampsItem(Properties pProperties) {
        super(pProperties);
    }
    
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        BlockPos heaterPos = getHeaterPos(pLevel, pPlayer);
        if (heaterPos == null) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
    
    private static BlockPos getHeaterPos(Level level, Player player) {
        Direction facing = player.getDirection();
        BlockPos playerPos = player.blockPosition();
        BlockPos heaterPos = playerPos.relative(facing);
        return heaterPos.above();
    }
}
