package cloud.jarsey45.tankiez.blocks.entity;

import cloud.jarsey45.tankiez.networking.ModNetworking;
import cloud.jarsey45.tankiez.networking.packet.FluidSyncS2CPacket;
import cloud.jarsey45.tankiez.screen.BasicTankMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BasicTankEntity extends BlockEntity implements MenuProvider {
	private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return switch (slot) {
				case 0, 1 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
				case 2 -> false;
				default -> super.isItemValid(slot, stack);
			};
		}
	};

	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
	protected final ContainerData data;
	//used for fluid crafting
	private int currentProgress = 0, maxProgress = 120;

	public BasicTankEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BASIC_TANK.get(), pos, state);
		this.data = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> BasicTankEntity.this.currentProgress;
					case 1 -> BasicTankEntity.this.maxProgress;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> BasicTankEntity.this.currentProgress = value;
					case 1 -> BasicTankEntity.this.maxProgress = value;
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable("block.tankiez.basic_tank");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
		ModNetworking.sendToClients(new FluidSyncS2CPacket(this.getFluidStack(), worldPosition));
		System.out.printf("Fluid: %s, Capacity: %s, current: %s\n", this.getFluidStack().getDisplayName(), "64000", this.getFluidStack().getAmount());
		this.setFluid(new FluidStack(Fluids.WATER, this.getFluidStack().getAmount() >= 64000 ? 32000 : this.getFluidStack().getAmount() + 4000));
		System.out.printf("Fluid: %s, Capacity: %s, current: %s\n", this.getFluidStack().getDisplayName(), "64000", this.getFluidStack().getAmount());
		return new BasicTankMenu(id, inventory, this, this.data);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return lazyItemHandler.cast();
		}

		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return lazyFluidHandler.cast();
		}

		return super.getCapability(cap, side);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
		lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
		lazyFluidHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.putInt("basic_tank.progress", this.currentProgress);

		nbt = FLUID_TANK.writeToNBT(nbt);

		super.saveAdditional(nbt);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);

		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		currentProgress = nbt.getInt("basic_tank.progress");

		FLUID_TANK.readFromNBT(nbt);
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}

		Containers.dropContents(this.level, this.worldPosition, inventory);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BasicTankEntity entity) {
		if (entity.hasRecipe()) {
			entity.currentProgress++;
			setChanged(level, blockPos, blockState);

			if (entity.hasProgressFinished()) {
				entity.craftItem();
				entity.resetProgress();
			}
		} else {
			entity.resetProgress();
			setChanged(level, blockPos, blockState);
		}
	}

	private void resetProgress() {
		this.currentProgress = 0;
	}

	private boolean hasProgressFinished() {
		return currentProgress >= maxProgress;
	}

	private void craftItem() {

	}

	private boolean hasRecipe() {
		SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
		for (int i = 0; i < this.itemHandler.getSlots(); i++) {
			inventory.setItem(i, this.itemHandler.getStackInSlot(i));
		}

		//TODO: fluid crafting check

		return true;
	}


	//Fluid Handling
	private final FluidTank FLUID_TANK = new FluidTank(64000) {
		@Override
		protected void onContentsChanged() {
			setChanged();

			if(!level.isClientSide()) {
          ModNetworking.sendToClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
      }
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			return stack.getFluid() == Fluids.WATER || stack.getFluid() == Fluids.LAVA;
		}
	};

	public void setFluid(FluidStack stack) {
		this.FLUID_TANK.setFluid(stack);
	}

	public FluidStack getFluidStack() {
		return this.FLUID_TANK.getFluid();
	}


}
