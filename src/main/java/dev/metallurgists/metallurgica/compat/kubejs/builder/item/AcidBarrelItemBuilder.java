package dev.metallurgists.metallurgica.compat.kubejs.builder.item;

import dev.metallurgists.metallurgica.compat.kubejs.builder.fluid.AcidFluidBuilder;
import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.world.item.BucketItem;

public class AcidBarrelItemBuilder extends ItemBuilder {
    public final AcidFluidBuilder acidBuilder;

    public AcidBarrelItemBuilder(AcidFluidBuilder b) {
        super(b.newID("", "_barrel"));
        acidBuilder = b;
        maxStackSize(1);
    }

    @Override
    public BucketItem createObject() {
        return new ArchitecturyBucketItem(acidBuilder, createItemProperties());
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(id), modelJson);
            return;
        }

        generator.itemModel(id, m -> {
            if (!parentModel.isEmpty()) {
                m.parent(parentModel);
            } else {
                m.parent("kubejs:item/generated_barrel");
            }

            if (textureJson.size() > 0) {
                m.textures(textureJson);
            }
        });
    }
}
