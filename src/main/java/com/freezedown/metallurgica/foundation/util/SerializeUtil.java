package com.freezedown.metallurgica.foundation.util;

import com.freezedown.metallurgica.Metallurgica;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SerializeUtil {
    public static class Deserializer<T extends JsonElement, U extends Writeable> {
        private int id;
        public Function<T, U> fromJson;
        public Function<FriendlyByteBuf, U> fromPacket;
        
        public Deserializer(Function<T, U> fromJson, Function<FriendlyByteBuf, U> fromPacket) {
            super();
            this.fromJson = fromJson;
            this.fromPacket = fromPacket;
        }
        
        public U read(T json) {
            return fromJson.apply(json);
        }
        
        public U read(FriendlyByteBuf packet) {
            return fromPacket.apply(packet);
        }
        
        public void write(FriendlyByteBuf packet, U obj) {
            packet.writeVarInt(id);
            obj.write(packet);
        }
        
        public JsonElement serialize(U obj) {
            return obj.serialize();
        }
    }
    public static class CompoundBuilder{
        CompoundTag nbt=new CompoundTag();
        public static CompoundBuilder create() {
            return new CompoundBuilder();
        }
        public CompoundBuilder put(String key,Tag val) {
            nbt.put(key, val);
            return this;
        }
        public CompoundBuilder put(String key, UUID val) {
            nbt.putUUID(key, val);
            return this;
        }
        public CompoundTag build() {
            return nbt;
        }
        public CompoundBuilder put(String key,int val) {
            nbt.putInt(key, val);
            return this;
        }
    }
    
    
    private SerializeUtil() {
    
    }
    
    
    public static <T> Optional<T> readOptional(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> func) {
        if (buffer.readBoolean())
            return Optional.ofNullable(func.apply(buffer));
        return Optional.empty();
    }
    
    public static <T> void writeOptional(FriendlyByteBuf buffer, T data, BiConsumer<T, FriendlyByteBuf> func) {
        writeOptional(buffer, Optional.ofNullable(data), func);
    }
    
    public static <T> void writeOptional2(FriendlyByteBuf buffer, T data, BiConsumer<FriendlyByteBuf, T> func) {
        writeOptional(buffer, data, (a, b) -> func.accept(b, a));
    }
    
    public static <T> void writeOptional(FriendlyByteBuf buffer, Optional<T> data, BiConsumer<T, FriendlyByteBuf> func) {
        if (data.isPresent()) {
            buffer.writeBoolean(true);
            func.accept(data.get(), buffer);
            return;
        }
        buffer.writeBoolean(false);
    }
    public static boolean[] readBooleans(FriendlyByteBuf buffer) {
        boolean[] ret=new boolean[8];
        byte in=buffer.readByte();
        for(int i=ret.length-1;i>=0;i--) {
            ret[i]=(in&1)!=0;
            in>>=1;
        }
        return ret;
    }
    /**
     * Write boolean as a byte into buffer
     * @param elms elements to write, 8 elements max
     * */
    public static void writeBooleans(FriendlyByteBuf buffer,boolean...elms) {
        if (elms.length>8) {
            throw new IllegalArgumentException("count of boolean must not excess 8");
        }
        byte b=0;
        for(int i=0;i<8;i++) {
            boolean bl=elms.length>i?elms[i]:false;
            b<<=1;
            b|=bl?1:0;
            
        }
        buffer.writeByte(b);
    }
    
    public static <T> List<T> readList(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> func) {
        if (!buffer.readBoolean())
            return null;
        int cnt = buffer.readVarInt();
        List<T> nums = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++)
            nums.add(func.apply(buffer));
        return nums;
    }
    public static short[] readShortArray(FriendlyByteBuf buffer) {
        if (!buffer.readBoolean())
            return null;
        int cnt = buffer.readVarInt();
        short[] nums = new short[cnt];
        for (int i = 0; i < cnt; i++)
            nums[i]=buffer.readShort();
        return nums;
    }
    public static void writeShortArray(FriendlyByteBuf buffer,short[] arr) {
        if (arr == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        buffer.writeVarInt(arr.length);
        for(short s:arr)
            buffer.writeShort(s);
    }
    public static <T> void writeList(FriendlyByteBuf buffer, Collection<T> elms, BiConsumer<T, FriendlyByteBuf> func) {
        if (elms == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        buffer.writeVarInt(elms.size());
        elms.forEach(e -> func.accept(e, buffer));
    }
    
    public static <T> void writeList2(FriendlyByteBuf buffer, Collection<T> elms, BiConsumer<FriendlyByteBuf, T> func) {
        if (elms == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        buffer.writeVarInt(elms.size());
        elms.forEach(e -> func.accept(buffer, e));
    }
    
    public static <T> List<T> parseJsonList(JsonElement elm, Function<JsonObject, T> mapper) {
        if (elm == null)
            return Lists.newArrayList();
        if (elm.isJsonArray())
            return StreamSupport.stream(elm.getAsJsonArray().spliterator(), false).map(JsonElement::getAsJsonObject)
                    .map(mapper).collect(Collectors.toList());
        return Lists.newArrayList(mapper.apply(elm.getAsJsonObject()));
    }
    
    public static <T> List<T> parseJsonElmList(JsonElement elm, Function<JsonElement, T> mapper) {
        if (elm == null)
            return Lists.newArrayList();
        if (elm.isJsonArray())
            return StreamSupport.stream(elm.getAsJsonArray().spliterator(), false).map(mapper)
                    .collect(Collectors.toList());
        return Lists.newArrayList(mapper.apply(elm));
    }
    
    
    public static <T> JsonArray toJsonList(Collection<T> li, Function<T, JsonElement> mapper) {
        JsonArray ja = new JsonArray();
        li.stream().map(mapper).forEach(ja::add);
        return ja;
    }
    
    public static <T, B> JsonArray toJsonStringList(Collection<T> li, Function<T, B> mapper) {
        JsonArray ja = new JsonArray();
        li.stream().map(mapper).map(B::toString).forEach(ja::add);
        return ja;
    }
    
    public static <T> ListTag toNBTList(Collection<T> stacks, Function<T, Tag> mapper) {
        ListTag nbt = new ListTag();
        stacks.stream().map(mapper).forEach(nbt::add);
        return nbt;
    }
    
    public static JsonElement toJson(ItemStack stack) {
        boolean hasCount = stack.getCount() > 1, hasTag = stack.hasTag();
        if (!hasCount && !hasTag)
            return new JsonPrimitive(stack.getItem().getDescriptionId().toString());
        JsonObject jo = new JsonObject();
        jo.addProperty("id", stack.getItem().getDescriptionId().toString());
        if (hasCount)
            jo.addProperty("count", stack.getCount());
        if (hasTag)
            jo.addProperty("nbt", stack.getTag().toString());
        return jo;
    }
    
    public static ItemStack fromJson(JsonElement elm) {
        if (elm.isJsonPrimitive())
            return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(elm.getAsString())));
        else if (elm.isJsonObject()) {
            JsonObject jo = elm.getAsJsonObject();
            ItemStack ret = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("id").getAsString())));
            if (jo.has("count"))
                ret.setCount(jo.get("count").getAsInt());
            if (jo.has("nbt"))
                try {
                    ret.setTag(TagParser.parseTag(jo.get("nbt").getAsString()));
                } catch (CommandSyntaxException e) {
                    Metallurgica.LOGGER.warn(e.getMessage());
                }
            return ret;
        }
        return ItemStack.EMPTY;
    }
}
