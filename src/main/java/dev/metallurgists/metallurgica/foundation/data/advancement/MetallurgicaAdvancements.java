package dev.metallurgists.metallurgica.foundation.data.advancement;

import dev.metallurgists.metallurgica.registry.MetallurgicaItems;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static dev.metallurgists.metallurgica.foundation.data.advancement.MAdvancement.TaskType.SILENT;

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

    private final PackOutput output;

    public MetallurgicaAdvancements(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            ResourceLocation id = advancement.getId();
            if (!set.add(id))
                throw new IllegalStateException("Duplicate advancement " + id);
            Path path = pathProvider.json(id);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
                    .serializeToJson(), path));
        };
        
        for (MAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
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
