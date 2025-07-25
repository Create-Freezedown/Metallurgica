package dev.metallurgists.metallurgica.foundation.data.advancement;

import dev.metallurgists.metallurgica.Metallurgica;
import com.simibubi.create.foundation.advancement.AllTriggers;
import com.simibubi.create.foundation.advancement.SimpleCreateTrigger;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class MAdvancement {
    static final ResourceLocation BACKGROUND = Metallurgica.asResource("textures/gui/advancements.png");
    static final String LANG = "advancement." + Metallurgica.ID + ".";
    static final String SECRET_SUFFIX = "\n\u00A77(Hidden Advancement)";
    
    private Advancement.Builder builder;
    private SimpleCreateTrigger builtinTrigger;
    private MAdvancement parent;
    
    Advancement datagenResult;
    
    private String id;
    private String title;
    private String description;
    
    public MAdvancement(String id, UnaryOperator<Builder> b) {
        this.builder = Advancement.Builder.advancement();
        this.id = id;
        
        Builder t = new Builder();
        b.apply(t);
        
        if (!t.externalTrigger) {
            builtinTrigger = AllTriggers.addSimple(id + "_builtin");
            builder.addCriterion("0", builtinTrigger.instance());
        }
        
        builder.display(t.icon, Component.translatable(titleKey()),
                Component.translatable(descriptionKey()).withStyle(s -> s.withColor(0x3E4A63)),
                id.equals("root") ? BACKGROUND : null, t.type.frame, t.type.toast, t.type.announce, t.type.hide);
        
        if (t.type == TaskType.SECRET)
            description += SECRET_SUFFIX;
        
        MetallurgicaAdvancements.ENTRIES.add(this);
    }
    
    private String titleKey() {
        return LANG + id;
    }
    
    private String descriptionKey() {
        return titleKey() + ".desc";
    }
    
    public boolean isAlreadyAwardedTo(Player player) {
        if (!(player instanceof ServerPlayer sp))
            return true;
        Advancement advancement = sp.getServer()
                .getAdvancements()
                .getAdvancement(Metallurgica.asResource(id));
        if (advancement == null)
            return true;
        return sp.getAdvancements()
                .getOrStartProgress(advancement)
                .isDone();
    }
    
    public void awardTo(Player player) {
        if (!(player instanceof ServerPlayer sp))
            return;
        if (builtinTrigger == null)
            throw new UnsupportedOperationException(
                    "Advancement " + id + " uses external Triggers, it cannot be awarded directly");
        builtinTrigger.trigger(sp);
    }
    
    void save(Consumer<Advancement> t) {
        if (parent != null)
            builder.parent(parent.datagenResult);
        datagenResult = builder.save(t, Metallurgica.asResource(id)
                .toString());
    }
    
    void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(titleKey(), title);
        consumer.accept(descriptionKey(), description);
    }
    
    static enum TaskType {
        
        SILENT(FrameType.TASK, false, false, false),
        NORMAL(FrameType.TASK, true, false, false),
        NOISY(FrameType.TASK, true, true, false),
        EXPERT(FrameType.GOAL, true, true, false),
        SECRET(FrameType.GOAL, true, true, true),
        
        ;
        
        private FrameType frame;
        private boolean toast;
        private boolean announce;
        private boolean hide;
        
        private TaskType(FrameType frame, boolean toast, boolean announce, boolean hide) {
            this.frame = frame;
            this.toast = toast;
            this.announce = announce;
            this.hide = hide;
        }
    }
    
    class Builder {
        
        private TaskType type = TaskType.NORMAL;
        private boolean externalTrigger;
        private int keyIndex;
        private ItemStack icon;
        
        Builder special(TaskType type) {
            this.type = type;
            return this;
        }
        
        Builder after(MAdvancement other) {
            MAdvancement.this.parent = other;
            return this;
        }
        
        Builder icon(ItemProviderEntry<?> item) {
            return icon(item.asStack());
        }
        
        Builder icon(ItemLike item) {
            return icon(new ItemStack(item));
        }
        
        Builder icon(ItemStack stack) {
            icon = stack;
            return this;
        }
        
        Builder title(String title) {
            MAdvancement.this.title = title;
            return this;
        }
        
        Builder description(String description) {
            MAdvancement.this.description = description;
            return this;
        }
        
        Builder whenBlockPlaced(Block block) {
            return externalTrigger(ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
        }
        
        Builder whenIconCollected() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(icon.getItem()));
        }
        
        Builder whenItemCollected(ItemProviderEntry<?> item) {
            return whenItemCollected(item.asStack()
                    .getItem());
        }
        
        Builder whenItemCollected(ItemLike itemProvider) {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider));
        }
        
        Builder whenItemCollected(TagKey<Item> tag) {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance
                    .hasItems(new ItemPredicate(tag, null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
                            EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY)));
        }
        
        Builder awardedForFree() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] {}));
        }
        
        Builder externalTrigger(CriterionTriggerInstance trigger) {
            builder.addCriterion(String.valueOf(keyIndex), trigger);
            externalTrigger = true;
            keyIndex++;
            return this;
        }
        
    }
    
}
