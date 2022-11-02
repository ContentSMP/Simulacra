package arathain.simulacra.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.option.SoundSliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public abstract class AccessibleSliderWidget extends SliderWidget {
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
	public void updateMessage() {

	}

	public double getValue() {
		return value;
	}
	public void setValue(double val) {
		this.value = val;
	}
}
