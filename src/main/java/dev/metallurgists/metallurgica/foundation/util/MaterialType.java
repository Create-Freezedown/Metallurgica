package dev.metallurgists.metallurgica.foundation.util;

public enum MaterialType {
    METAL(),
    ALLOY(),
    GEMSTONE()
    ;
    
    MaterialType() {
    }
    
    public static MaterialType fromString(String string) {
        for (MaterialType type : values()) {
            if (type.name().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return null;
    }
}
