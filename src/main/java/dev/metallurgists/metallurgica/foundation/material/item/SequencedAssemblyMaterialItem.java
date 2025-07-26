package dev.metallurgists.metallurgica.foundation.material.item;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class SequencedAssemblyMaterialItem extends SequencedAssemblyItem implements IMaterialItem {
    public final Material material;
    public final IItemRegistry itemFlag;

    public SequencedAssemblyMaterialItem(Properties properties, Material material, IItemRegistry itemFlag) {
        super(properties);
        this.material = material;
        this.itemFlag = itemFlag;
    }

    @Override
    public String getDescriptionId() {
        return itemFlag.getUnlocalizedName(material);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public MutableComponent getDescription() {
        return itemFlag.getLocalizedName(material);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IItemRegistry getFlag() {
        return this.itemFlag;
    }
}
