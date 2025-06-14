package com.freezedown.metallurgica.foundation.item.registry;

import com.freezedown.metallurgica.foundation.item.registry.flags.*;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.infastructure.element.Element;
import com.freezedown.metallurgica.infastructure.element.ElementEntry;
import com.freezedown.metallurgica.infastructure.element.data.ElementData;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.util.entry.FluidEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(chain = true, fluent = true)
public class Material implements Comparable<Material> {

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    private final MaterialFlags flags;

    @Setter
    @Getter
    private boolean shouldRegister = true;

    private Material(@NotNull MaterialInfo materialInfo, @NotNull MaterialFlags flags) {
        this.materialInfo = materialInfo;
        this.flags = flags;
        this.flags.setMaterial(this);
        verifyMaterial();
    }

    public String getName() {
        return materialInfo.resourceLocation.getPath();
    }

    public String getModid() {
        return materialInfo.resourceLocation.getNamespace();
    }

    public ResourceLocation getId() {
        return materialInfo.resourceLocation;
    }

    public boolean noRegister(FlagKey<? extends IMaterialFlag> flag) {
        return flags.getNoRegister().contains(flag);
    }

    public void registerWhen(ConfigBase.ConfigBool config) {
        shouldRegister(config.get());
    }

    public List<ElementData> getComposition() {
        if (materialInfo.composition.isEmpty()) {
            return List.of(new ElementData(MetallurgicaElements.NULL.getId(), 1));
        }
        return materialInfo.composition;
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return getFlag(key) != null;
    }

    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return flags.getFlag(key);
    }

    public <T extends IMaterialFlag> void setFlag(FlagKey<T> key, IMaterialFlag flag) {
        flags.setFlag(key, flag);
        flags.verify();
    }

    public @NotNull MaterialFlags getFlags() {
        return flags;
    }

    public void verifyMaterial() {
        flags.verify();
    }

    @Override
    public int compareTo(@NotNull Material material) {
        return toString().compareTo(material.toString());
    }

    public String getUnlocalizedName() {
        return materialInfo.resourceLocation.toLanguageKey("material");
    }

    public MutableComponent getLocalizedName() {
        return Component.translatable(getUnlocalizedName());
    }

    public static class Builder {
        private final MaterialInfo materialInfo;
        private final MaterialFlags flags;
        private final List<ElementData> composition = new ArrayList<>();

        public Builder(ResourceLocation resourceLocation) {
            String name = resourceLocation.getPath();
            if (name.charAt(name.length() - 1) == '_')
                throw new IllegalArgumentException("Material name cannot end with a '_'!");
            materialInfo = new MaterialInfo(resourceLocation);
            flags = new MaterialFlags();
        }

        public Builder colour(int rgb) {
            materialInfo.withColour(rgb);
            return this;
        }

        @SafeVarargs
        public final Builder noRegister(FlagKey<? extends IMaterialFlag>... matFlags) {
            flags.getNoRegister().addAll(Arrays.stream(matFlags).toList());
            return this;
        }

        public Builder existingIds(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Existing Ids list malformed!");

            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null || components[i + 1] == null) {
                    throw new IllegalArgumentException(
                            "Existing Id in Existing Ids List is null for Material " + this.materialInfo.resourceLocation);
                }
                materialInfo.withExistingId((FlagKey<?>) components[i], (String) components[i + 1]);
            }
            return this;
        }

        public Builder nameAlternatives(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Name Alternatives list malformed!");

            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null || components[i + 1] == null) {
                    throw new IllegalArgumentException(
                            "Name Alternative in Name Alternatives List is null for Material " + this.materialInfo.resourceLocation);
                }
                materialInfo.nameAlternatives().put((FlagKey<?>) components[i], (String) components[i + 1]);
            }
            return this;
        }

        public Builder element(ElementEntry<?> entry) {
            materialInfo.composition().add(new ElementData(entry.getId(), 1));
            return this;
        }

        public Builder composition(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Composition list malformed!");

            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "ElementData in Compositions List is null for Material " + this.materialInfo.resourceLocation);
                }
                materialInfo.composition().add(new ElementData(components[i] instanceof ElementEntry<?> entry ? entry.getId() : ((Element) components[i]).getId(), ((Number) components[i + 1]).intValue()));
            }
            return this;
        }

        public Builder addFlags(IMaterialFlag... flags) {
            for (var flag : flags) {
                this.flags.setFlag(flag.getKey(), flag);
            }
            return this;
        }

        public Material buildAndRegister() {
            if (materialInfo.colour() == 0) materialInfo.withColour(0xFFFFFF);
            var mat = new Material(materialInfo, flags);
            ResourceLocation key = new ResourceLocation(mat.getModid(), mat.getName());
            MetMaterials.registeredMaterials.put(key, mat);
            return mat;
        }
    }

    @Accessors(chain = true)
    public static class MaterialInfo {
        private final ResourceLocation resourceLocation;
        @Getter
        public Map<FlagKey<?>, String> nameAlternatives = new HashMap<>();
        @Getter
        public Map<FlagKey<?>, ResourceLocation> existingIds = new HashMap<>();
        @Getter
        public List<ElementData> composition = new ArrayList<>();
        @Getter
        private int colour;

        private MaterialInfo(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public MaterialInfo withNameAlternative(FlagKey<?> flag, String alternative) {
            nameAlternatives.put(flag, alternative);
            return this;
        }

        public MaterialInfo withExistingId(FlagKey<?> flag, ResourceLocation id) {
            existingIds.put(flag, id);
            return this;
        }
        public MaterialInfo withExistingId(FlagKey<?> flag, String id) {
            return withExistingId(flag, new ResourceLocation(id));
        }

        public MaterialInfo withColour(int rgb) {
            colour = rgb;
            return this;
        }


        private void verifyInfo(MaterialFlags flags) {
            // no-op
        }
    }
}
