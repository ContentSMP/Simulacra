package arathain.simulacra.client.screen;

import arathain.simulacra.Simulacra;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.option.SoundSliderWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public abstract class AccessibleSliderWidget extends SliderWidget {
	private static final Identifier BUTTON_LOCATION = new Identifier(Simulacra.MODID, "textures/gui/slider.png");
	public AccessibleSliderWidget(int x, int y, int width, int height, Text text, double value) {
		super(x, y, width, height, text, value);
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
		int j = this.active ? 16777215 : 10526880;
		drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

	}

	@Override
	protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BUTTON_LOCATION);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)), this.y-1, 0, 0, 8, 16, 8, 16);
	}

	@Override
	public void updateMessage() {

	}

	public double getValue() {
		return value;
	}
	public void setValue(double val) {
		this.value = val;
	}
}
