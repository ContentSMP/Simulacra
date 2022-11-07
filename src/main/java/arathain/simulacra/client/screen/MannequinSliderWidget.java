package arathain.simulacra.client.screen;

import arathain.simulacra.Simulacra;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class MannequinSliderWidget extends AccessibleSliderWidget {
	private static final Identifier BUTTON_LOCATION = new Identifier(Simulacra.MODID, "textures/gui/slider_wood.png");

	public MannequinSliderWidget(int x, int y, int width, int height, Text text, double value) {
		super(x, y, width, height, text, value);
	}
	@Override
	protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BUTTON_LOCATION);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)), this.y-1, 0, 0, 8, 16, 8, 16);
	}
}
