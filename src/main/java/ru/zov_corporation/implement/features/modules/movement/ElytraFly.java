package ru.zov_corporation.implement.features.modules.movement;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.SelectSetting;
import ru.zov_corporation.common.util.other.StopWatch;
import ru.zov_corporation.common.util.entity.PlayerIntersectionUtil;
import ru.zov_corporation.common.util.entity.PlayerInventoryUtil;
import ru.zov_corporation.common.util.task.scripts.Script;
import ru.zov_corporation.implement.events.player.FireworkEvent;
import ru.zov_corporation.implement.events.player.TickEvent;
import ru.zov_corporation.implement.features.modules.combat.killaura.rotation.AngleUtil;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElytraFly extends Module {
    StopWatch stopWatch = new StopWatch(), swapWatch = new StopWatch();
    Script script = new Script();

    SelectSetting flyModeSetting = new SelectSetting("Fly Mode", "Selects the type of mode")
            .value("FireWork Abuse");

    public ElytraFly() {
        super("ElytraFly", "Elytra Fly", ModuleCategory.MOVEMENT);
        setup(flyModeSetting);
    }

    @EventHandler
    public void onFireWork(FireworkEvent e) {
        stopWatch.reset();
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (flyModeSetting.isSelected("FireWork Abuse")) {
            Slot elytra = PlayerInventoryUtil.getSlot(Items.ELYTRA);
            Slot fireWork = PlayerInventoryUtil.getSlot(Items.FIREWORK_ROCKET);
            if (!mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA) && elytra != null && fireWork != null && script.isFinished()) {
                if (stopWatch.finished(100)) {
                    int ticks = mc.player.isOnGround() ? 2 : 0;
                    if (ticks != 0) mc.player.jump();
                    script.cleanup().addTickStep(ticks, () -> {
                        PlayerInventoryUtil.moveItem(elytra, 6, false);
                        PlayerIntersectionUtil.startFallFlying();
                        PlayerInventoryUtil.swapAndUse(Items.FIREWORK_ROCKET, AngleUtil.cameraAngle(), false);
                        PlayerInventoryUtil.moveItem(elytra, 6, false);
                        PlayerInventoryUtil.closeScreen(true);
                        stopWatch.setMs(-500);
                    });
                }
            }
        }
        script.update();
    }
}
