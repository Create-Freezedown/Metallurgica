package com.freezedown.metallurgica.infastructure.material.registry.flags.block;

import com.drmangotea.tfmg.config.StressConfig;
import com.freezedown.metallurgica.foundation.config.server.subcat.MStress;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import com.freezedown.metallurgica.foundation.material.block.IMaterialBlock;
import com.freezedown.metallurgica.foundation.material.block.MaterialCogWheelBlock;
import com.freezedown.metallurgica.foundation.material.item.MaterialCogWheelBlockItem;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels.*;

@SuppressWarnings("removal")
public class CogWheelFlag extends BlockFlag implements IPartialHolder, ISpecialAssetGen {

    public CogWheelFlag(String existingNamespace) {
        super("%s_cogwheel", existingNamespace);
        this.setTagPatterns(List.of("c:cogwheels", "c:cogwheels/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("c:cogwheels", "c:cogwheels/%s"));
    }

    public CogWheelFlag() {
        this("metallurgica");
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, BlockFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        return registrate.block(getIdPattern().formatted(material.getName()), (p) -> MaterialCogWheelBlock.small(material, flag, p))
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
        return FlagKey.COG_WHEEL;
    }

    @Override
    public ResourceLocation getModelLocation(Material material) {
        return new ResourceLocation(material.getNamespace(), "cogwheel/" + material.getName());
    }

    @Override
    public JsonElement createModel(Material material) {
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/cogwheel.png")).isPresent();
        String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/cogwheel" : "metallurgica:item/materials/null/cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/small_shaftless");
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
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/cogwheel.png")).isPresent();
        String texture = texturePresent ? "metallurgica:block/materials/" + material.getName() + "/cogwheel" : "metallurgica:block/materials/null/cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/small");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }
}
