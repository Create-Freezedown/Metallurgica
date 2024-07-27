package com.freezedown.metallurgica.infastructure;

import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.registry.MetallurgicaMaterials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OpenMetallurgicaMenuButton extends Button {
    public static final ItemStack ICON;
    
    public OpenMetallurgicaMenuButton(int x, int y) {
        super(x, y, 20, 20, Components.immutableEmpty(), OpenMetallurgicaMenuButton::click);
    }
    
    public void renderBg(PoseStack mstack, Minecraft mc, int mouseX, int mouseY) {
        Minecraft.getInstance().getItemRenderer().renderGuiItem(ICON, this.x + 2, this.y + 2);
    }
    
    public static void click(Button b) {
        ScreenOpener.open(new BaseConfigScreen(Minecraft.getInstance().screen, "metallurgica"));
    }
    
    static {
        ICON = MetallurgicaMaterials.CASSITERITE.MATERIAL.raw().asStack();
    }
    
    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class OpenConfigButtonHandler {
        public OpenConfigButtonHandler() {
        }
        
        @SubscribeEvent
        public static void onGuiInit(ScreenEvent.Init event) {
            Screen gui = event.getScreen();
            MenuRows menu = null;
            int rowIdx = 0;
            int offsetX = 0;
            if (gui instanceof TitleScreen) {
                menu = MenuRows.MAIN_MENU;
                rowIdx = (Integer) MetallurgicaConfigs.client().mainMenuConfigButtonRow.get();
                offsetX = (Integer)MetallurgicaConfigs.client().mainMenuConfigButtonOffsetX.get();
            } else if (gui instanceof PauseScreen) {
                menu = MenuRows.INGAME_MENU;
                rowIdx = (Integer)MetallurgicaConfigs.client().ingameMenuConfigButtonRow.get();
                offsetX = (Integer)MetallurgicaConfigs.client().ingameMenuConfigButtonOffsetX.get();
            }
            
            if (rowIdx != 0 && menu != null) {
                boolean onLeft = offsetX < 0;
                String target = (String)(onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);
                int offsetX_ = offsetX;
                MutableObject<GuiEventListener> toAdd = new MutableObject((Object)null);
                event.getListenersList().stream().filter((w) -> {
                    return w instanceof AbstractWidget;
                }).map((w) -> {
                    return (AbstractWidget)w;
                }).filter((w) -> {
                    return w.getMessage().getString().equals(target);
                }).findFirst().ifPresent((w) -> {
                    toAdd.setValue(new OpenMetallurgicaMenuButton(w.x + offsetX_ + (onLeft ? -20 : w.getWidth()), w.y));
                });
                if (toAdd.getValue() != null) {
                    event.addListener((GuiEventListener)toAdd.getValue());
                }
            }
            
        }
    }
    
    public static class MenuRows {
        public static final MenuRows MAIN_MENU = new MenuRows(Arrays.asList(new SingleMenuRow("menu.singleplayer"), new SingleMenuRow("menu.multiplayer"), new SingleMenuRow("fml.menu.mods", "menu.online"), new SingleMenuRow("narrator.button.language", "narrator.button.accessibility")));
        public static final MenuRows INGAME_MENU = new MenuRows(Arrays.asList(new SingleMenuRow("menu.returnToGame"), new SingleMenuRow("gui.advancements", "gui.stats"), new SingleMenuRow("menu.sendFeedback", "menu.reportBugs"), new SingleMenuRow("menu.options", "menu.shareToLan"), new SingleMenuRow("menu.returnToMenu")));
        protected final List<?> leftButtons;
        protected final List<?> rightButtons;
        
        public MenuRows(List<SingleMenuRow> variants) {
            this.leftButtons = variants.stream().map((r) -> r.left).collect(Collectors.toList());
            this.rightButtons = variants.stream().map((r) -> r.right).collect(Collectors.toList());
        }
    }
    
    public static class SingleMenuRow {
        public final String left;
        public final String right;
        
        public SingleMenuRow(String left, String right) {
            this.left = I18n.get(left, new Object[0]);
            this.right = I18n.get(right, new Object[0]);
        }
        
        public SingleMenuRow(String center) {
            this(center, center);
        }
    }
}
