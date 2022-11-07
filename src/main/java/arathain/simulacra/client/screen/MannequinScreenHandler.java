package arathain.simulacra.client.screen;

import arathain.simulacra.entity.MannequinEntity;
import arathain.simulacra.entity.StatueEntity;
import arathain.simulacra.init.SimulacraScreenHandlers;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Arrays;

public class MannequinScreenHandler extends ScreenHandler {
	private MannequinEntity statueProper = null;
	private final SimpleInventory container;

	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
		this(syncId, playerInventory,
				playerInventory.player.getWorld().getEntityById(buf.readVarInt()) instanceof MannequinEntity soop ? soop : null
		);
	}
	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, MannequinEntity soop) {
		this(syncId, playerInventory, soop.inventory, soop);
	}

	public static MannequinScreenHandler mannequinMenu(int containerId, PlayerInventory inventory, MannequinEntity soup) {
		return new MannequinScreenHandler(containerId, inventory, soup.inventory, soup);
	}
	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, SimpleInventory inventory, MannequinEntity entity) {
		super(SimulacraScreenHandlers.MANNEQUIN_SCREEN_HANDLER_TYPE, syncId);
		this.container = entity.inventory;
		this.statueProper = entity;

		this.container.onOpen(playerInventory.player);

		for(int k = 0; k < 2; ++k) {
			for(int l = 0; l < 2; ++l) {

				int i = k + l * 2;
				this.addSlot(new Slot(inventory, i, 6 + l * 52, 23 + k * 26) {
					@Override
					public boolean canInsert(ItemStack stack) {
						int x = i;
						if(x == 0) {
							x = 5;
						}
						if(x == 1) {
							x = 4;
						}
						if(x == 2) {
							x = 0;
						}
						if(x == 3) {
							x = 1;
						}
						return super.canInsert(stack) && (MobEntity.getPreferredEquipmentSlot(stack) == Arrays.stream(EquipmentSlot.values()).toList().get(x) || (x == 0 || x ==1));
					}
				});
			}
		}



		// Player Inventory
		for(int i1 = 0; i1 < 3; ++i1) {
			for(int k1 = 0; k1 < 9; ++k1) {
				this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
			}
		}

		// Player Hotbar
		for(int j1 = 0; j1 < 9; ++j1) {
			this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
		}
	}

	public MannequinEntity getStatueProper() {
		return statueProper;
	}

	@Override
	public ItemStack quickTransfer(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			int i = this.container.size();
			if (index < i) {
				if (!this.insertItem(itemStack2, i, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(1).canInsert(itemStack2) && !this.getSlot(1).hasStack()) {
				if (!this.insertItem(itemStack2, 1, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(0).canInsert(itemStack2)) {
				if (!this.insertItem(itemStack2, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (i <= 2 || !this.insertItem(itemStack2, 2, i, false)) {
				int k;
				int j = i;
				int l = k = j + 27;
				int m = l + 9;
				if (index >= l && index < m ? !this.insertItem(itemStack2, j, k, false) : (index >= j && index < k ? !this.insertItem(itemStack2, l, m, false) : !this.insertItem(itemStack2, l, k, false))) {
					return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}
		return itemStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return statueProper != null;
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		container.onClose(player);
	}
}
