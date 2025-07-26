package dev.metallurgists.metallurgica.infastructure.material.registry.flags.base;

import java.util.List;

public abstract class BlockSetFlag implements IMaterialFlag{

    private final List<String> idPatterns;
    private String existingNamespace = "metallurgica";

    public BlockSetFlag(String... idPatterns) {
        this.idPatterns = List.of(idPatterns);
    }

    public BlockSetFlag(List<String> idPatterns, String existingNamespace) {
        this.idPatterns = idPatterns;
        this.existingNamespace = existingNamespace;
    }
}
