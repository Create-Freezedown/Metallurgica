package com.freezedown.metallurgica.foundation.material.registry.flags.item;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.TFMGConductor;
import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.material.recycling.Scrappable;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.IRecipeHandler;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.conductor.CableItem;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.conductor.ConductorEntry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import net.createmod.catnip.data.Pair;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

public class CableFlag extends ItemFlag implements IRecipeHandler, Scrappable {

    @Getter
    private Pair<int[],int[]> colors;

    @Getter
    private double resistivity;

    @Getter
    private final String idPattern;

    public CableFlag(double resistivity, Pair<int[],int[]> colors) {
        super("%s_cable", "metallurgica");
        this.idPattern = "%s_cable";
        this.resistivity = resistivity;
        this.colors = colors;
        this.setTagPatterns(List.of("metallurgica:cables", "metallurgica:cables/%s"));
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.CABLE;
    }

    @Override
    public ItemEntry<? extends MaterialItem> registerItem(@NotNull Material material, ItemFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        ConductorEntry<Conductor> conductor = registrate.conductor(material.getName(), Conductor::new)
                .properties(p -> p.color1(getColors().getFirst()).color2(getColors().getSecond()))
                .transform(TFMGConductor.setResistivity(getResistivity()))
                .register();
        return registrate.item("%s_cable".formatted(material.getName()), (p) -> new CableItem(p, conductor, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.WIRE, true);
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        var wire = MaterialHelper.getItem(material, FlagKey.WIRE);
        var cable = MaterialHelper.getItem(material, getKey());
        ShapedRecipeBuilder builder = new ShapedRecipeBuilder(RecipeCategory.MISC, cable, 4);
        builder.pattern(" W ").pattern("WSW").pattern(" W ")
                .define('W', wire).define('S', Tags.Items.RODS_WOODEN);
        builder.unlockedBy("has_wire", has(wire));
        builder.save(provider,  Metallurgica.asResource("runtime_generated/" + material.getNamespace() + "/" + material.getName() + "_cable_from_wire"));
    }

    @Override
    public Map<Material, Integer> scrapsInto(Material mainMaterial) {
        return Map.of(mainMaterial, 3);
    }

    @Override
    public Map<Material, Pair<Integer, Float>> discardChance(Material mainMaterial) {
        return Map.of(mainMaterial, Pair.of(1, 0.5f));
    }

    @Override
    public Map<ItemLike, Pair<Integer, Float>> extraItems(Material mainMaterial) {
        return Map.of(Items.STICK, Pair.of(1, 0.25f), MaterialHelper.getItem(mainMaterial, FlagKey.WIRE), Pair.of(1, 0.15f));
    }
}
