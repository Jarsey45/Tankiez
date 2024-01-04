package cloud.jarsey45.tankiez.items;

import cloud.jarsey45.tankiez.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DiazuliWrenchItem extends Item {
	public DiazuliWrenchItem() {
		super(new Properties().stacksTo(1));
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack wrench = player.getItemInHand(hand);

		if (level.isClientSide())
			return InteractionResultHolder.success(wrench);

		System.out.println("Clicked");

		BlockPos targetBlockPos = BlockUtils.getLookingAt(player);
		BlockState targetState = level.getBlockState(targetBlockPos);
		if (targetState.isAir())
			return InteractionResultHolder.pass(wrench);

		if (player.isShiftKeyDown()) {
			targetState.rotate(level, targetBlockPos, Rotation.COUNTERCLOCKWISE_90);
		} else {
			targetState.rotate(level, targetBlockPos, Rotation.CLOCKWISE_90);
		}

		System.out.println(targetState);
		return InteractionResultHolder.success(wrench);
	}


}
