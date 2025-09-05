package ru.zov_corporation.implement.features.modules.movement;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.math.Vec3d;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.ValueSetting;
import ru.zov_corporation.common.util.entity.MovingUtil;
import ru.zov_corporation.implement.events.player.MoveEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AbilitiesFly extends Module {
    ValueSetting speedSetting = new ValueSetting("Speed", "Select fly speed").setValue(2.0F).range(0.5F, 4.0F);

    public AbilitiesFly() {
        super("AbilitiesFly", "Abilities Fly", ModuleCategory.MOVEMENT);
        setup(speedSetting);
    }

    @EventHandler
    public void onMove(MoveEvent e) {
        if (mc.player != null && mc.player.getAbilities().flying) {
            float speed = speedSetting.getValue();
            float y = mc.player.isSneaking() ? -speed : mc.player.jumping ? speed : 0;
            double[] motion = MovingUtil.calculateDirection(speed);
            e.setMovement(new Vec3d(motion[0], y, motion[1]));
        }
    }
}
