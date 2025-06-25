package com.freezedown.metallurgica.infastructure.material;

import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.infastructure.IHasDescriptionId;
import com.freezedown.metallurgica.infastructure.element.Element;
import com.freezedown.metallurgica.infastructure.element.ElementEntry;
import com.freezedown.metallurgica.infastructure.element.data.ElementData;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(chain = true, fluent = true)
public class Material implements Comparable<Material>, IHasDescriptionId {

    private String descriptionId;

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

    public String getNamespace() {
        return materialInfo.resourceLocation.getNamespace();
    }

    public ResourceLocation getId() {
        return materialInfo.resourceLocation;
    }

    public ResourceLocation asResource(String path) {
        return new ResourceLocation(getNamespace(), path);
    }

    public String asResourceString(String path) {
        return asResource(path).toString().replace(":", "/");
    }

    public boolean noRegister(FlagKey<? extends IMaterialFlag> flag) {
        return flags.getNoRegister().contains(flag);
    }

    public void registerWhen(ConfigBase.ConfigBool config) {
        shouldRegister(config.get());
    }

    public List<SubComposition> getComposition() {
        if (materialInfo.composition.isEmpty()) {
            return List.of(new SubComposition(List.of(new ElementData(MetallurgicaElements.NULL.getId(), 1)), 1));
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

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("material", getId());
        }

        return this.descriptionId;
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

        public Builder meltingPoint(double meltingPoint) {
            materialInfo.meltingPoint(meltingPoint);
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
            ElementData elementData = new ElementData(entry.getId(), 1);
            materialInfo.composition().add(new SubComposition(List.of(elementData), 1));
            return this;
        }

        public Builder composition(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Composition list malformed!");
            List<ElementData> elementDataList = new ArrayList<>();
            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "ElementData in Compositions List is null for Material " + this.materialInfo.resourceLocation);
                }
                ElementData elementData = new ElementData(components[i] instanceof ElementEntry<?> entry ? entry.getId() : ((Element) components[i]).getId(), ((Number) components[i + 1]).intValue());
                elementDataList.add(elementData);
            }
            ElementData.createFromList(elementDataList).forEach(materialInfo.composition()::add);
            return this;
        }

        public Builder alloyComposition(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Composition list malformed!");
            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "ElementData in Compositions List is null for Material " + this.materialInfo.resourceLocation);
                }
                MaterialEntry<Material> materialEntry = ((MaterialEntry<Material>) components[i]);
                int amount = ((Number) components[i + 1]).intValue();
                List<SubComposition> materialComp = materialEntry.get().getComposition();
                List<ElementData> elementDataList = new ArrayList<>();
                for (SubComposition subComposition : materialComp) {
                    elementDataList.addAll(subComposition.getElements());
                }
                materialInfo.composition().add(new SubComposition(elementDataList, amount));
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
            ResourceLocation key = new ResourceLocation(mat.getNamespace(), mat.getName());
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
        public List<SubComposition> composition = new ArrayList<>();
        @Getter
        private int colour;
        @Getter
        private double meltingPoint;

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

        public MaterialInfo meltingPoint(double mp) {
            this.meltingPoint = mp;
            return this;
        }


        private void verifyInfo(MaterialFlags flags) {
            // no-op
        }
    }
}
