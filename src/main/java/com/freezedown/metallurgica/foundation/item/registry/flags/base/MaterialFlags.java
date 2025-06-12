package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class MaterialFlags {
    private static final Set<FlagKey<?>> baseTypes = new HashSet<>(Arrays.asList(
            FlagKey.LIQUID, FlagKey.DUST,
            FlagKey.INGOT, FlagKey.GEM,
            FlagKey.MINERAL, FlagKey.EMPTY));

    @SuppressWarnings("unused")
    public static void addBaseType(FlagKey<?> baseTypeKey) {
        baseTypes.add(baseTypeKey);
    }

    private final Map<FlagKey<? extends IMaterialFlag>, IMaterialFlag> flagMap;
    @Setter
    @Getter
    private Material material;

    @Setter
    @Getter
    private List<FlagKey<? extends IMaterialFlag>> noRegister = new ArrayList<>();

    public MaterialFlags() {
        flagMap = new HashMap<>();
    }

    public boolean isEmpty() {
        return flagMap.isEmpty();
    }

    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return key.cast(flagMap.get(key));
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return flagMap.get(key) != null;
    }

    public <T extends IMaterialFlag> void setFlag(FlagKey<T> key, IMaterialFlag value) {
        if (value == null) throw new IllegalArgumentException("Material Flag must not be null!");
        if (hasFlag(key))
            throw new IllegalArgumentException("Material Flag " + key.toString() + " already registered!");
        flagMap.put(key, value);
        flagMap.remove(FlagKey.EMPTY);
    }

    public <T extends IMaterialFlag> void removeFlag(FlagKey<T> flag) {
        if (!hasFlag(flag))
            throw new IllegalArgumentException("Material Flag " + flag.toString() + " not present!");
        flagMap.remove(flag);
        if (flagMap.isEmpty())
            flagMap.put(FlagKey.EMPTY, FlagKey.EMPTY.constructDefault());
    }

    public <T extends IMaterialFlag> void ensureSet(FlagKey<T> key, boolean verify) {
        if (!hasFlag(key)) {
            flagMap.put(key, key.constructDefault());
            flagMap.remove(FlagKey.EMPTY);
            if (verify) verify();
        }
    }

    public <T extends IMaterialFlag> void ensureSet(FlagKey<T> key) {
        ensureSet(key, false);
    }

    public void verify() {
        List<IMaterialFlag> oldList;
        do {
            oldList = new ArrayList<>(flagMap.values());
            oldList.forEach(p -> p.verifyFlag(this));
        } while (oldList.size() != flagMap.size());

        if (flagMap.keySet().stream().noneMatch(baseTypes::contains)) {
            if (flagMap.isEmpty()) {
                flagMap.put(FlagKey.EMPTY, FlagKey.EMPTY.constructDefault());
            } else
                throw new IllegalArgumentException("Material must have at least one of: " + baseTypes + " specified!");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        flagMap.forEach((k, v) -> sb.append(k.toString()).append("\n"));
        return sb.toString();
    }

    public Set<FlagKey<?>> getFlagKeys() {
        return flagMap.keySet();
    }

}
