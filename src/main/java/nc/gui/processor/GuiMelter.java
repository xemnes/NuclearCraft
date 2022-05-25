package nc.gui.processor;

import java.io.IOException;

import nc.container.ContainerTile;
import nc.container.processor.ContainerMachineConfig;
import nc.container.processor.ContainerMelter;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.GuiItemRenderer;
import nc.gui.element.NCButton;
import nc.gui.element.NCToggleButton;
import nc.init.NCItems;
import nc.network.PacketHandler;
import nc.network.gui.EmptyTankPacket;
import nc.network.gui.OpenTileGuiPacket;
import nc.network.gui.OpenSideConfigGuiPacket;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.tile.processor.TileItemFluidProcessor;
import nc.util.Lang;
import nc.util.NCUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiMelter extends GuiItemFluidProcessor {
	
	public GuiMelter(EntityPlayer player, TileItemFluidProcessor tile) {
		this(player, tile, new ContainerMelter(player, tile));
	}
	
	private GuiMelter(EntityPlayer player, TileItemFluidProcessor tile, ContainerTile container) {
		super("melter", player, tile, container);
		xSize = 176;
		ySize = 166;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawEnergyTooltip(tile, mouseX, mouseY, 8, 6, 16, 74);
		renderButtonTooltips(mouseX, mouseY);
	}
	
	public void renderButtonTooltips(int mouseX, int mouseY) {
		drawFluidTooltip(tile.getTanks().get(0), mouseX, mouseY, 112, 31, 24, 24);
		
		drawTooltip(Lang.localise("gui.container.machine_side_config"), mouseX, mouseY, 27, 63, 18, 18);
		drawTooltip(Lang.localise("gui.container.redstone_control"), mouseX, mouseY, 47, 63, 18, 18);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(112, 31, 176, 57, 24, 24);

		drawUpgradeRenderers();
	}
	
	protected void drawUpgradeRenderers() {
		new GuiItemRenderer(132, ySize - 102, 0.2F, NCItems.upgrade, 0).draw();
		new GuiItemRenderer(152, ySize - 102, 0.2F, NCItems.upgrade, 1).draw();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if (tile.defaultProcessPower != 0) {
			int e = (int) Math.round(74D*tile.getEnergyStorage().getEnergyStored()/tile.getEnergyStorage().getMaxEnergyStored());
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFFC6C6C6, 0xFF8B8B8B);
		
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, getCookProgressScaled(37), 16);
		
		drawBackgroundExtras();
	}
	
	protected void drawBackgroundExtras() {
		GuiFluidRenderer.renderGuiTank(tile.getTanks().get(0), guiLeft + 112, guiTop + 31, zLevel, 24, 24);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.EmptyTank(0, guiLeft + 112, guiTop + 31, 24, 24));
		
		buttonList.add(new NCButton.MachineConfig(1, guiLeft + 27, guiTop + 63));
		buttonList.add(new NCToggleButton.RedstoneControl(2, guiLeft + 47, guiTop + 63, tile));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			for (int i = 0; i < 1; i++) if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
				return;
			}
			if (guiButton.id == 1) {
				PacketHandler.instance.sendToServer(new OpenSideConfigGuiPacket(tile));
			}
			else if (guiButton.id == 2) {
				tile.setRedstoneControl(!tile.getRedstoneControl());
				PacketHandler.instance.sendToServer(new ToggleRedstoneControlPacket(tile));
			}
		}
	}
	
	public static class SideConfig extends GuiMelter {
		
		public SideConfig(EntityPlayer player, TileItemFluidProcessor tile) {
			super(player, tile, new ContainerMachineConfig(player, tile));
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			if (isEscapeKeyDown(keyCode)) {
				PacketHandler.instance.sendToServer(new OpenTileGuiPacket(tile));
			}
			else super.keyTyped(typedChar, keyCode);
		}
		
		@Override
		public void renderButtonTooltips(int mouseX, int mouseY) {
			drawTooltip(TextFormatting.BLUE + Lang.localise("gui.container.input_item_config"), mouseX, mouseY, 55, 34, 18, 18);
			drawTooltip(TextFormatting.RED + Lang.localise("gui.container.output_tank_config"), mouseX, mouseY, 111, 30, 26, 26);
			drawTooltip(TextFormatting.DARK_BLUE + Lang.localise("gui.container.upgrade_config"), mouseX, mouseY, 131, 63, 18, 18);
			drawTooltip(TextFormatting.YELLOW + Lang.localise("gui.container.upgrade_config"), mouseX, mouseY, 151, 63, 18, 18);
		}
		
		@Override
		protected void drawUpgradeRenderers() {}
		
		@Override
		protected void drawBackgroundExtras() {};
		
		@Override
		public void initButtons() {
			buttonList.add(new NCButton.SorptionConfig.ItemInput(0, guiLeft + 55, guiTop + 34));
			buttonList.add(new NCButton.SorptionConfig.FluidOutput(1, guiLeft + 111, guiTop + 30));
			buttonList.add(new NCButton.SorptionConfig.SpeedUpgrade(2, guiLeft + 131, guiTop + 63));
			buttonList.add(new NCButton.SorptionConfig.EnergyUpgrade(3, guiLeft + 151, guiTop + 63));
		}
		
		@Override
		protected void actionPerformed(GuiButton guiButton) {
			if (tile.getWorld().isRemote) {
				if (guiButton.id == 0) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input(this, tile, 0));
				}
				else if (guiButton.id == 1) {
					FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output(this, tile, 0));
				}
				else if (guiButton.id == 2) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.SpeedUpgrade(this, tile, 1));
				}
				else if (guiButton.id == 3) {
					FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.EnergyUpgrade(this, tile, 2));
				}
			}
		}
	}
}
