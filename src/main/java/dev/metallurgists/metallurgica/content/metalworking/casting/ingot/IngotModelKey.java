package dev.metallurgists.metallurgica.content.metalworking.casting.ingot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public record IngotModelKey(BlockState state, ItemStack item) {
}
