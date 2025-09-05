package ru.zov_corporation.implement.features.modules.movement;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.block.Blocks;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Hand;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.event.types.EventType;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.MultiSelectSetting;
import ru.zov_corporation.api.feature.module.setting.implement.SelectSetting;
import ru.zov_corporation.common.util.entity.*;
import ru.zov_corporation.common.util.other.Instance;
import ru.zov_corporation.common.util.other.StopWatch;
import ru.zov_corporation.common.util.task.scripts.Script;
import ru.zov_corporation.implement.events.item.UsingItemEvent;
import ru.zov_corporation.implement.events.player.TickEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoSlow extends Module {
    public static NoSlow getInstance() {
        return Instance.get(NoSlow.class);
    }

    private final StopWatch notifWatch = new StopWatch();
    private final Script script = new Script();
    private boolean finish;

    public final MultiSelectSetting slowTypeSetting = new MultiSelectSetting("Target Type", "Filters the entire list of targets by type").value("Using Item", "Web");
    public final SelectSetting itemMode = new SelectSetting("Item Mode", "Select bypass mode").value("Grim New", "Grim Old").visible(() -> slowTypeSetting.isSelected("Using Item"));
    public final SelectSetting webMode = new SelectSetting("Web Mode", "Select bypass mode").value("Grim").visible(() -> slowTypeSetting.isSelected("Web"));

    public NoSlow() {
        super("NoSlow", "No Slow", ModuleCategory.MOVEMENT);
        setup(slowTypeSetting, itemMode, webMode);
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (slowTypeSetting.isSelected("Web") && PlayerIntersectionUtil.isPlayerInBlock(Blocks.COBWEB)) {
            double[] speed = MovingUtil.calculateDirection(0.64);
            mc.player.addVelocity(speed[0], 0, speed[1]);
            mc.player.velocity.y = mc.options.jumpKey.isPressed() ? 1.2 : mc.options.sneakKey.isPressed() ? -2 : 0;
        }
        if (PlayerInventoryComponent.script.isFinished() && MovingUtil.hasPlayerMovement()) {
            script.update();
        }
    }

    @EventHandler
    public void onUsingItem(UsingItemEvent e) {
        if (slowTypeSetting.isSelected("Using Item")) {
            Hand first = mc.player.getActiveHand();
            Hand second = first.equals(Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND;
            switch (e.getType()) {
                case EventType.ON -> {
                    switch (itemMode.getSelected()) {
                        case "Grim Old" -> {
                            if (mc.player.getOffHandStack().getUseAction().equals(UseAction.NONE) || mc.player.getMainHandStack().getUseAction().equals(UseAction.NONE)) {
                                PlayerIntersectionUtil.interactItem(first);
                                PlayerIntersectionUtil.interactItem(second);
                                e.cancel();
                            }
                        }
                        case "Grim New" -> {
                            if (mc.player.getItemUseTime() < 7) {
                                PlayerInventoryUtil.updateSlots();
                                PlayerInventoryUtil.closeScreen(true);
                            } else e.cancel();
                        }
                        case "FunTime" -> {
                            if (finish) e.cancel();
                        }
                    }
                }
                case EventType.POST -> {
                    while (!script.isFinished()) script.update();
                }
            }
        }
    }
}
