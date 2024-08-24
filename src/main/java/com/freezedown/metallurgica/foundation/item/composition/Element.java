package com.freezedown.metallurgica.foundation.item.composition;

import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Element(String name, int amount, boolean areNumbersUp, boolean bracketed) {
    public static final Codec<Element> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("element").forGetter(Element::getName),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(Element::getAmount),
            Codec.BOOL.optionalFieldOf("areNumbersUp", false).forGetter(Element::areNumbersUp),
            Codec.BOOL.optionalFieldOf("bracketed", false).forGetter(Element::bracketed)
    ).apply(instance, Element::new));
    
    public static Element create(String name) {
        return new Element(name, 1, false, false);
    }
    
    public Element withAmount(int amount) {
        return new Element(name, amount, areNumbersUp, bracketed);
    }
    
    public Element withNumbersUp() {
        return new Element(name, amount, true, bracketed);
    }
    
    public Element withBrackets() {
        return new Element(name, amount, areNumbersUp, true);
    }
    
    public String getName() {
        return name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public boolean areNumbersUp() {
        return areNumbersUp;
    }
    
    public boolean bracketed() {
        return bracketed;
    }
    
    public static List<Element> createComposition(Element... elements) {
        return List.of(elements);
    }
    
    public String getDisplay() {
        String display = ClientUtil.lang().translate("element." + name.toLowerCase()).string();
        if (amount > 1)
            display += amount;
        return areNumbersUp ? ClientUtil.toSmallUpNumbers(display) : ClientUtil.toSmallDownNumbers(display);
    }
}
