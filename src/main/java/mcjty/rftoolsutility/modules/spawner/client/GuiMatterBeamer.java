package mcjty.rftoolsutility.modules.spawner.client;

import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.ImageChoiceLabel;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.varia.RedstoneMode;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsutility.RFToolsUtility;
import mcjty.rftoolsutility.modules.spawner.SpawnerModule;
import mcjty.rftoolsutility.modules.spawner.blocks.MatterBeamerTileEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import javax.annotation.Nonnull;
import java.awt.*;

public class GuiMatterBeamer extends GenericGuiContainer<MatterBeamerTileEntity, GenericContainer> {
    private static final int BEAMER_WIDTH = 180;
    private static final int BEAMER_HEIGHT = 152;

    private EnergyBar energyBar;

    private static final ResourceLocation iconLocation = ResourceLocation.fromNamespaceAndPath(RFToolsUtility.MODID, "textures/gui/matterbeamer.png");
    private static final ResourceLocation iconGuiElements = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "textures/gui/guielements.png");

    public GuiMatterBeamer(GenericContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, SpawnerModule.MATTER_BEAMER.block().get().getManualEntry());

        imageWidth = BEAMER_WIDTH;
        imageHeight = BEAMER_HEIGHT;
    }

    public static void register(RegisterMenuScreensEvent event) {
        event.register(SpawnerModule.CONTAINER_MATTER_BEAMER.get(), GuiMatterBeamer::new);
    }

    @Override
    public void init() {
        super.init();

        energyBar = new EnergyBar().vertical().hint(10, 7, 8, 54).showText(false);

        ImageChoiceLabel redstoneMode = initRedstoneMode();
        Panel toplevel = new Panel().background(iconLocation).layout(new PositionalLayout()).children(energyBar, redstoneMode);
        toplevel.setBounds(new Rectangle(leftPos, topPos, imageWidth, imageHeight));

        window = new Window(this, toplevel);
        window.bind(redstoneMode.getName(), getBE(), GenericTileEntity.VALUE_RSMODE.name());
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        drawWindow(graphics, partialTicks, mouseX, mouseY);
        updateEnergyBar(energyBar);
    }

    private ImageChoiceLabel initRedstoneMode() {
        ImageChoiceLabel redstoneMode = new ImageChoiceLabel()
                .name("redstone")
                .choice(RedstoneMode.REDSTONE_IGNORED.getDescription(), "Redstone mode:\nIgnored", iconGuiElements, 0, 0)
                .choice(RedstoneMode.REDSTONE_OFFREQUIRED.getDescription(), "Redstone mode:\nOff to activate", iconGuiElements, 16, 0)
                .choice(RedstoneMode.REDSTONE_ONREQUIRED.getDescription(), "Redstone mode:\nOn to activate", iconGuiElements, 32, 0);
        redstoneMode.hint(28, 44, 16, 16);
        return redstoneMode;
    }
}
