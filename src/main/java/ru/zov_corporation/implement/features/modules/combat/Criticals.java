package ru.zov_corporation.implement.features.modules.combat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.SelectSetting;
import ru.zov_corporation.common.util.math.MathUtil;
import ru.zov_corporation.common.util.entity.PlayerIntersectionUtil;
import ru.zov_corporation.common.util.other.Instance;
import ru.zov_corporation.implement.events.player.AttackEvent;
import ru.zov_corporation.implement.features.modules.combat.killaura.rotation.RotationController;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Criticals extends Module {
    public static Criticals getInstance() {
        return Instance.get(Criticals.class);
    }

    SelectSetting mode = new SelectSetting("Mode", "Select bypass mode").value("Grim");

    public Criticals() {
        super("Criticals", ModuleCategory.COMBAT);
        setup(mode);
    }

    @EventHandler
    public void onAttack(AttackEvent e) {
        if (mc.player.isTouchingWater()) return;
        if (mode.isSelected("Grim")) {
            if (!mc.player.isOnGround() && mc.player.fallDistance == 0) {
                PlayerIntersectionUtil.grimSuperBypass$$$(-(mc.player.fallDistance = MathUtil.getRandom(1e-5F, 1e-4F)), RotationController.INSTANCE.getRotation().random(1e-3F));
            }
        }
    }
}