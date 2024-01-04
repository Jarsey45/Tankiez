package cloud.jarsey45.tankiez.screen;

import cloud.jarsey45.tankiez.blocks.ModBlocks;
import cloud.jarsey45.tankiez.blocks.entity.BasicTankEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BasicTankMenu extends AbstractContainerMenu {
	public final BasicTankEntity blockEntity;
	private final Level level;
	private final ContainerData data;
	private FluidStack fluidStack;

	public BasicTankMenu(int id, Inventory inv, FriendlyByteBuf extra) {
		this(id, inv, inv.player.level().getBlockEntity(extra.readBlockPos()), new SimpleContainerData(2)); ///match entity data
	}

	public BasicTankMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
		super(ModMenuTypes.BASIC_TANK_MENU.get(), id);
		checkContainerSize(inventory, 4);
		blockEntity = (BasicTankEntity) entity;
		this.level = inventory.player.level();
		this.data = data;
		this.fluidStack = blockEntity.getFluidStack();

		addPlayerInventory(inventory);
		addPlayerHotbar(inventory);

		this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler ->
						this.addSlot(new SlotItemHandler(iItemHandler, 0, 80, 49))
		);

		addDataSlots(data);
	}

	public BasicTankEntity getBlockEntity() {
		return this.blockEntity;
	}


	// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
	// must assign a slot number to each of the slots used by the GUI.
	// For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	// THIS YOU HAVE TO DEFINE!
	private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must be the number of slots you have!

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int index) {
		Slot sourceSlot = slots.get(index);
		if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
							+ TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;  // EMPTY_ITEM
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
			if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			System.out.println("Invalid slotIndex:" + index);
			return ItemStack.EMPTY;
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {
			sourceSlot.set(ItemStack.EMPTY);
		} else {
			sourceSlot.setChanged();
		}
		sourceSlot.onTake(player, sourceStack);
		return copyOfSourceStack;
	}


	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.BASIC_TANK.get());
	}


	/* PLAYER GUI PLACEMENT */
	private void addPlayerInventory(Inventory playerInventory) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
			}
		}
	}

	private void addPlayerHotbar(Inventory playerInventory) {
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
		}
	}

	//Fluid Handling
	public void setFluid(FluidStack stack) {
		this.fluidStack = stack;
	}

	public FluidStack getFluidStack() {
		return this.fluidStack;
	}
}
