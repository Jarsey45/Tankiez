package cloud.jarsey45.tankiez.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ClassPathUtils;

public class BlockUtils {

    public static BlockPos getLookingAt(Player player) {
        Level level = player.level;

        double range = player.getAttackRange();

        Vec3 angle = player.getLookAngle();
        Vec3 start = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 end = new Vec3(
                player.getX() + angle.x * range,
                player.getY() + player.getEyeHeight() + angle.y * range,
                player.getZ() + angle.z * range
        );
        ClipContext ctx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
        return level.clip(ctx).getBlockPos();
    }

    public static BlockEntity getBlockEntity(LevelAccessor level, BlockPos pos) {
        return level.getBlockEntity(pos);
    }

}
