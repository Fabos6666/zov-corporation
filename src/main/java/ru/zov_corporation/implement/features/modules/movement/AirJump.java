package ru.zov_corporation.implement.features.modules.movement;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.SelectSetting;
import ru.zov_corporation.common.util.entity.PlayerIntersectionUtil;
import ru.zov_corporation.common.util.other.StopWatch;
import ru.zov_corporation.implement.events.player.JumpEvent;
import ru.zov_corporation.implement.events.player.PostTickEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirJump extends Module {
    StopWatch stopWatch = new StopWatch();

    SelectSetting modeSetting = new SelectSetting("Mode", "Selects the type of mode")
            .value("Polar Block Collision");

    public AirJump() {
        super("AirJump", "Air Jump", ModuleCategory.MOVEMENT);
        setup(modeSetting);
    }

    @EventHandler
    public void onJump(JumpEvent e) {
        stopWatch.reset();
    }

    @EventHandler
    public void onPostTick(PostTickEvent e) {
        if (modeSetting.isSelected("Polar Block Collision")) {
            if (mc.options.jumpKey.isPressed()) return;
            Box playerBox = mc.player.getBoundingBox().expand(-1e-3);
            Box box = new Box(playerBox.minX, playerBox.minY, playerBox.minZ, playerBox.maxX, playerBox.minY + 0.5, playerBox.maxZ);
            if (stopWatch.finished(400) && PlayerIntersectionUtil.isBox(box, this::hasCollision)) {
                box = new Box(playerBox.minX, playerBox.minY + 1, playerBox.minZ, playerBox.maxX, playerBox.maxY, playerBox.maxZ);
                if (PlayerIntersectionUtil.isBox(box, this::hasCollision)) {
                    mc.player.setOnGround(true);
                    mc.player.velocity.y = 0.6;
                } else {
                    mc.player.setOnGround(true);
                    mc.player.jump();
                }
            }
        }
    }


    private boolean hasCollision(BlockPos blockPos) {
        return !mc.world.getBlockState(blockPos).getCollisionShape(mc.world, blockPos).isEmpty();
    }
}
