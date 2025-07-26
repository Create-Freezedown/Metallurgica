package dev.metallurgists.metallurgica.infastructure.element.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record SubComposition(@Getter List<ElementData> elements, @Getter int amount) {
    public static final Codec<SubComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(ElementData.CODEC).fieldOf("elements").forGetter(SubComposition::elements),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(SubComposition::amount)
    ).apply(instance, SubComposition::new));

    public boolean shouldHaveBrackets() {
        return getElements().size() > 1;
    }

    public ElementData getElement(int index) {
        return elements.get(index);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", getAmount());
        JsonArray elementsArray = new JsonArray();
        for (ElementData element : elements) {
            elementsArray.add(element.toJson());
        }
        json.add("elements", elementsArray);
        return json;
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeInt(amount);
        buf.writeCollection(elements, (fBuf, element) -> element.writeToPacket(fBuf));
    }

    public static SubComposition fromNetwork(FriendlyByteBuf buf) {
        int amount = buf.readInt();
        List<ElementData> elements = buf.readList(ElementData::fromNetwork);
        return new SubComposition(elements, amount);
    }

    public static List<SubComposition> listFromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<SubComposition> subCompositions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            subCompositions.add(fromNetwork(buf));
        }
        return subCompositions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ElementData> elements = new ArrayList<>();
        private int amount = 1;

        public Builder() {}

        public Builder element(ElementData element) {
            elements.add(element);
            return this;
        }
        public Builder element(ElementData... elements) {
            this.elements.addAll(Arrays.stream(elements).toList());
            return this;
        }

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public SubComposition build() {
            return new SubComposition(elements, amount);
        }
    }
}
