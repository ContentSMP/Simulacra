package arathain.simulacra.client.screen;

import arathain.simulacra.Simulacra;
import arathain.simulacra.entity.StatueEntity;
import arathain.simulacra.network.SyncRetainPacket;
import arathain.simulacra.network.SyncStatueRotPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.EulerAngle;

import java.text.DecimalFormat;
import java.util.function.Function;

public class StatueScreen extends HandledScreen<StatueScreenHandler> {
	private static Part selected = Part.HEAD;
	private static final Identifier INVENTORY_LOCATION = new Identifier(Simulacra.MODID, "textures/gui/statue_gui.png");
	private final StatueEntity statue;
	private float mouseX = 0;
	private float mouseY = 0;
	private AccessibleSliderWidget sliderX, sliderY, sliderZ;
	public StatueScreen(StatueScreenHandler menu, PlayerInventory inv, Text title) {
		super(menu, inv, title);
		this.statue = menu.getStatueProper();
		this.passEvents = false;
	}

	@Override
	protected void init() {
		super.init();
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.addDrawableChild(this.sliderX = makeWidget(i+81, j+19));
		this.addDrawableChild(this.sliderY = makeWidget(i+81, j+37));
		this.addDrawableChild(this.sliderZ = makeWidget(i+81, j+55));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Part part : Part.values()) {
			if (selected != part && part.isHoveringOver(mouseX - this.x, mouseY - this.y)) {
				selected = part;
				this.updateSliders();
				MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK));
				return true;
			}
		}
		if(isHoveringOverRetain(mouseX - this.x, mouseY - this.y)) {
			SyncRetainPacket.send(this.statue, !this.statue.getRetain());
			System.out.println(this.statue.getRetain());
			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK));
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	public boolean isHoveringOverRetain(double mouseX, double mouseY) {
		return mouseX >= 148 && mouseX < 148 + 12 && mouseY >= 59 && mouseY < 59 + 8;
	}

	private void updateSliders() {
		EulerAngle rot = selected.getRot(this.statue);
		float pitch = rot.getWrappedPitch() + 180;
		float yaw = rot.getWrappedYaw() + 180;
		float roll = rot.getWrappedRoll() + 180;
		if (selected == Part.LEFT_ARM || selected == Part.LEFT_LEG)
			roll = 360 - roll;
		this.sliderX.setValue(pitch % 360 / 360.0F);
		this.sliderX.updateMessage();
		this.sliderY.setValue(yaw % 360 / 360.0F);
		this.sliderY.updateMessage();
		this.sliderZ.setValue(roll % 360 / 360.0F);
		this.sliderZ.updateMessage();
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
		if(this.statue.getRetain()) {
			this.drawTexture(matrices, i + 148, j + 57, 176, 34, 12, 11);
			System.out.println("soop");
		}

		InventoryScreen.drawEntity(i + 22, j + 62, 17, (float)(i + 51) - this.mouseX, (float)(j + 75 - 50) - this.mouseY, this.statue);
	}
	private AccessibleSliderWidget makeWidget(int x, int y) {
		return new AccessibleSliderWidget(x, y, 55, 14, Text.literal(""), 0) {
			@Override
			public void updateMessage() {

			}

			@Override
			protected void applyValue() {
				SyncStatueRotPacket.send(statue, selected.ordinal(), new EulerAngle((float) sliderX.getValue() * 360, (float) sliderY.getValue() * 360, (float) sliderZ.getValue() * 360));
			}
		};
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		super.render(matrices, mouseX, mouseY, delta);
		for (Part part : Part.values())
			if (selected == part) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
				this.drawTexture(matrices, this.x + part.xOffset, this.y + part.yOffset, part.u, part.v, part.width, part.height);
			}
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	enum Part {
		HEAD(StatueEntity::getHeadRot, 150, 22, 184, 0, 8, 8),
		BODY(StatueEntity::getBodyRot, 150, 31, 184, 9, 8, 12),
		RIGHT_ARM(StatueEntity::getRightArmRot, 146, 31, 193, 9, 3, 12),
		LEFT_ARM(StatueEntity::getLeftArmRot, 159, 31, 180, 9, 3, 12),
		RIGHT_LEG(StatueEntity::getRightLegRot, 150, 44, 184, 22, 3, 12),
		LEFT_LEG(StatueEntity::getLeftLegRot,  155, 44, 189, 22, 3, 12);
		private final Function<StatueEntity, EulerAngle> getRot;
		private final int xOffset;
		private final int yOffset;
		private final int u;
		private final int v;
		private final int width;
		private final int height;

		Part(Function<StatueEntity, EulerAngle> getRot, int xOffset, int yOffset, int u, int v, int width, int height) {
			this.getRot = getRot;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.u = u;
			this.v = v;
			this.width = width;
			this.height = height;
		}

		public boolean isHoveringOver(double mouseX, double mouseY) {
			return mouseX >= this.xOffset && mouseX < this.xOffset + this.width && mouseY >= this.yOffset && mouseY < this.yOffset + this.height;
		}

		public EulerAngle getRot(StatueEntity mannequin) {
			return this.getRot.apply(mannequin);
		}
	}
}
