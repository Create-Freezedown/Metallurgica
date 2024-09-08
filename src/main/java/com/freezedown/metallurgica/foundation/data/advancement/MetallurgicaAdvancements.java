package com.freezedown.metallurgica.foundation.data.advancement;

import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.freezedown.metallurgica.foundation.data.advancement.MAdvancement.TaskType.SILENT;

public class MetallurgicaAdvancements implements DataProvider {
    
    public static final List<MAdvancement> ENTRIES = new ArrayList<>();
    public static final MAdvancement START = null,
    
    ROOT = create("root", b -> b.icon(MetallurgicaItems.dirtyClayBall)
            .title("Welcome to Metallurica")
            .description("Prepare yourself for pain")
            .awardedForFree()
            .special(SILENT)),
    
    ROCKS_FROM_ROCKS = create("rocks_from_rocks", b -> b.icon(MetallurgicaItems.amphiboleShard)
            .title("Rocks from Rocks")
            .description("Crush a Stone Into More Useful Shards")
            .whenItemCollected(MetallurgicaTags.forgeItemTag("rock_shards"))
            .after(ROOT)),
    
    INEFFICIENT_CHARCOAL = create("inefficient_charcoal", b -> b.icon(Items.CHARCOAL)
            .title("Inefficient Charcoal")
            .description("Use a Charcoal Pit to Make Charcoal")
            .after(ROOT)),
    
    END = null;
    
    private static MAdvancement create(String id, UnaryOperator<MAdvancement.Builder> b) {
        return new MAdvancement(id, b);
    }
    
    // Datagen
    
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DataGenerator generator;
    
    public MetallurgicaAdvancements(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }
    
    @Override
    public void run(CachedOutput cache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (p_204017_3_) -> {
            if (!set.add(p_204017_3_.getId()))
                throw new IllegalStateException("Duplicate advancement " + p_204017_3_.getId());
            
            Path path1 = getPath(path, p_204017_3_);
            
            try {
                DataProvider.saveStable(cache, p_204017_3_.deconstruct()
                        .serializeToJson(), path1);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save advancement {}", path1, ioexception);
            }
        };
        
        for (MAdvancement advancement : ENTRIES)
            advancement.save(consumer);
    }
    
    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId()
                .getNamespace() + "/advancements/"
                + advancementIn.getId()
                .getPath()
                + ".json");
    }
    
    @Override
    public String getName() {
        return "Metallurgica's Advancements";
    }
    
    public static void provideLang(BiConsumer<String, String> consumer) {
        for (MAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }
    
    public static void register() {}
    
}
