package dev.metallurgists.metallurgica.infastructure.material.registry.flags.block;

import dev.metallurgists.metallurgica.foundation.config.server.subcat.MStress;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import dev.metallurgists.metallurgica.foundation.material.block.IMaterialBlock;
import dev.metallurgists.metallurgica.foundation.material.block.MaterialCogWheelBlock;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialCogWheelBlockItem;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IPartialHolder;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.ISpecialAssetGen;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dev.metallurgists.metallurgica.foundation.data.runtime.assets.MetallurgicaModels.*;

public class LargeCogWheelFlag extends BlockFlag implements IPartialHolder, ISpecialAssetGen {

    @Getter
    private String cogWheelModelVariant = "steel";

    public LargeCogWheelFlag(String existingNamespace) {
        super("large_%s_cogwheel", existingNamespace);
        this.setTagPatterns(List.of("c:large_cogwheels", "c:large_cogwheels/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("c:large_cogwheels", "c:large_cogwheels/%s"));
    }

    public LargeCogWheelFlag() {
        this("metallurgica");
    }

    public LargeCogWheelFlag variant(String variant) {
        this.cogWheelModelVariant = variant;
        return this;
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        NonNullFunction<BlockBehaviour.Properties, MaterialCogWheelBlock> factory = (p) -> MaterialCogWheelBlock.large(material, flag, p);
        return registrate.block(getIdPattern().formatted(material.getName()), factory)
                .initialProperties(SharedProperties::stone)
                .properties((p) -> p.sound(SoundType.COPPER))
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(MStress.setNoImpact())
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(MaterialCogWheelBlockItem::new)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build().register();
    }

    @Override
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.LARGE_COG_WHEEL;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.COG_WHEEL);
    }

    @Override
    public ResourceLocation getModelLocation(Material material) {
        return new ResourceLocation(material.getNamespace(), "large_cogwheel/" + material.getName());
    }

    @Override
    public JsonElement createModel(Material material) {
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/large_cogwheel.png")).isPresent();
        String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/large_cogwheel" : "metallurgica:item/materials/null/large_cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/"+cogWheelModelVariant+"/large_shaftless");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }

    @Override
    public void generateAssets(Material material) {
        MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), model(material));
        MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), simpleAxisBlockstate("metallurgica:block/" + getIdPattern().formatted(material.getName())));
        MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), simpleParentedModel(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
    }

    private JsonElement model(Material material) {
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/large_cogwheel.png")).isPresent();
        String texture = texturePresent ? "metallurgica:block/materials/" + material.getName() + "/large_cogwheel" : "metallurgica:block/materials/null/large_cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/"+cogWheelModelVariant+"/large");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }
}
