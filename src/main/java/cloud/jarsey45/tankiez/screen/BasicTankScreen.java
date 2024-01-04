package cloud.jarsey45.tankiez.screen;

import cloud.jarsey45.tankiez.Tankiez;
import cloud.jarsey45.tankiez.screen.renderer.FluidTankRenderer;
import cloud.jarsey45.tankiez.util.MouseUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BasicTankScreen extends AbstractContainerScreen<BasicTankMenu> {
	private static final ResourceLocation TEXTURE =
					new ResourceLocation(Tankiez.MOD_ID, "textures/gui/basic_tank_gui.png");
	private FluidTankRenderer renderer;

	public BasicTankScreen(BasicTankMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	protected void init() {
		super.init();
//		this.inventoryLabelY = -200;
//		this.titleLabelY = -200;

		assignFluidRenderer();
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		renderFluidTooltips(guiGraphics, mouseX, mouseY, x + 44, y + 12);
	}

	//TODO: remove magic numbers
	private void renderFluidTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
		guiGraphics.fill(x, y, 44, 12, FastColor.ABGR32.color(1,1, 0, 0));
		if(isMouseOverArea(mouseX, mouseY, x, y + 2, 15, 15))
			guiGraphics.renderTooltip(this.font, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL),
							Optional.empty(), mouseX - x, mouseY - y);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		//render under glass texture, must be rendered before transparent gui
		renderer.render(guiGraphics, x + 44, y + 14, menu.getFluidStack());

		//render gui
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);

		guiGraphics.blit(TEXTURE, x, y, 0, -2, imageWidth, imageHeight + 2);

		//render progress
		renderProgressBubbles(guiGraphics, x, y);
	}

	private void renderProgressBubbles(GuiGraphics guiGraphics, int x, int y) {
		//...
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	//fluid
	private void assignFluidRenderer() {
		renderer = new FluidTankRenderer(64000, true, 16, 16);
	}


	//helpers
	private boolean isMouseOverArea(int mouseX, int mouseY, int x, int y, int offsetX, int offsetY) {
		return MouseUtils.isMouseOver(mouseX, mouseY, x, y, offsetX, offsetY);
	}
}
