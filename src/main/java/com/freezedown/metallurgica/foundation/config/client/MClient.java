package com.freezedown.metallurgica.foundation.config.client;

import com.negodya1.vintageimprovements.infrastructure.config.VCClient;
import com.simibubi.create.foundation.config.ConfigBase;

public class MClient extends ConfigBase {
    
    public final ConfigBool renderExcavationParticles = b(true, "renderExcavationParticles", Comments.renderExcavationParticles);
    public final ConfigBool appendTextToItems = b(true, "appendTextToItems", Comments.appendTextToItems);
    
    public final ConfigBase.ConfigGroup configButton = this.group(1, "configButton", Comments.configButton);;
    public final ConfigBase.ConfigInt mainMenuConfigButtonRow = this.i(2, 0, 4, "mainMenuConfigButtonRow", Comments.mainMenuConfigButtonRow);;
    public final ConfigBase.ConfigInt mainMenuConfigButtonOffsetX = this.i(4, Integer.MIN_VALUE, Integer.MAX_VALUE, "mainMenuConfigButtonOffsetX", Comments.mainMenuConfigButtonOffsetX);
    public final ConfigBase.ConfigInt ingameMenuConfigButtonRow = this.i(2, 0, 5, "ingameMenuConfigButtonRow", Comments.ingameMenuConfigButtonRow);
    public final ConfigBase.ConfigInt ingameMenuConfigButtonOffsetX = this.i(4, Integer.MIN_VALUE, Integer.MAX_VALUE, "ingameMenuConfigButtonOffsetX", Comments.ingameMenuConfigButtonOffsetX);
    @Override
    public String getName() {
        return "client";
    }
    
    private static class Comments {
        static String renderExcavationParticles = "Whether or not to render particles when excavating from deposits";
        static String appendTextToItems = "Whether or not items like alloys should have extra information appended to their display name";
        static String configButton = "Position of Metallurgica's config button in the main menu & pause menu";
        static String[] mainMenuConfigButtonRow = new String[]{"Choose the menu row that Metallurgica's config button appears on in the main menu", "Set to 0 to disable the button altogether"};
        static String[] mainMenuConfigButtonOffsetX = new String[]{"Offset Metallurgica's config button in the main menu by this many pixels on the X axis", "The sign (-/+) of this value determines what side of the row the button appears on (left/right)"};
        static String[] ingameMenuConfigButtonRow = new String[]{"Choose the menu row that Metallurgica's config button appears on in the in-game menu", "Set to 0 to disable the button altogether"};
        static String[] ingameMenuConfigButtonOffsetX = new String[]{"Offset Metallurgica's config button in the in-game menu by this many pixels on the X axis", "The sign (-/+) of this value determines what side of the row the button appears on (left/right)"};
        
    }
}
