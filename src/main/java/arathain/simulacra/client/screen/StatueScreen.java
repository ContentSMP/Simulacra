package arathain.simulacra.client.screen;

import arathain.simulacra.Simulacra;
import arathain.simulacra.entity.StatueEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StatueScreen extends HandledScreen<StatueScreenHandler> {
	private static final Identifier INVENTORY_LOCATION = new Identifier(Simulacra.MODID, "textures/gui/statue_gui.png");
	private final StatueEntity statue;
	private float mouseX = 0;
	private float mouseY = 0;
	public StatueScreen(StatueScreenHandler menu, PlayerInventory inv, Text title) {
		super(menu, inv, title);
		this.statue = menu.getStatueProper();
		this.passEvents = false;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

		InventoryScreen.drawEntity(i + 32, j + 62, 17, (float)(i + 51) - this.mouseX, (float)(j + 75 - 50) - this.mouseY, this.statue);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
