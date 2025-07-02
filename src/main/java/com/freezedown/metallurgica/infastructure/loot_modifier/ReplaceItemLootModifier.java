package com.freezedown.metallurgica.infastructure.loot_modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class ReplaceItemLootModifier extends LootModifier {
    public List<ItemStackPair> itemPairs;
    
    public static final Codec<ReplaceItemLootModifier> CODEC = RecordCodecBuilder.create((instance) -> {
        return codecStart(instance).and(Codec.list(ItemStackPair.CODEC).fieldOf("replacements").forGetter(
                (lm) -> lm.itemPairs
        )).apply(instance, ReplaceItemLootModifier::new);
    });
    
    protected ReplaceItemLootModifier(LootItemCondition[] conditionsIn, List<ItemStackPair> itemPairs) {
        super(conditionsIn);
        this.itemPairs = itemPairs;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext ctx) {
        List<ItemStack> toCheck = new ObjectArrayList<>(loot);
        for (ItemStackPair pair : itemPairs) {
            for (int i = 0; i < toCheck.size(); i++) {
                ItemStack stack = toCheck.get(i);
                int count = stack.getCount();
                if (ItemStack.isSameItem(stack, pair.getInput())) {
                    ItemStack output = pair.getOutput().copy();
                    output.setCount(count);
                    loot.set(i, output);
                }
            }
        }
        return loot;
    }
    
    @Override
    public Codec<ReplaceItemLootModifier> codec() {
        return CODEC;
    }
}
