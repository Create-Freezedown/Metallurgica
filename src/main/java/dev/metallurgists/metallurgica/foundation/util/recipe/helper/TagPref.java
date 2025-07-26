package dev.metallurgists.metallurgica.foundation.util.recipe.helper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record TagPref(List<String> tagPreferences) {
    public static final Codec<TagPref> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("namespaces").forGetter(TagPref::tagPreferences)
    ).apply(instance, TagPref::new));
}
