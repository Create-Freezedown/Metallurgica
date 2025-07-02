package com.freezedown.metallurgica.foundation.config.common.dev;

import net.createmod.catnip.config.ConfigBase;

public class DevConfig extends ConfigBase {

    public final ConfigBool dumpRecipes = this.b(false,"dump_recipes", "Dump Runtime Generated Recipes to the local files");
    public final ConfigBool dumpCompositions = this.b(false,"dump_compositions", "Dump Runtime Generated Compositions to the local files");
    public final ConfigBool dumpAssets = this.b(false,"dump_assets", "Dump Runtime Generated Assets to the local files");

    @Override
    public String getName() {
        return "dev";
    }
}
