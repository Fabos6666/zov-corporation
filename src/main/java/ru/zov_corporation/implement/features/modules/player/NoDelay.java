package ru.zov_corporation.implement.features.modules.player;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.setting.implement.MultiSelectSetting;
import ru.zov_corporation.common.util.other.Instance;
import ru.zov_corporation.implement.events.player.TickEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoDelay extends Module {
    public static NoDelay getInstance() {
        return Instance.get(NoDelay.class);
    }

    public MultiSelectSetting ignoreSetting = new MultiSelectSetting("Type", "Allows the actions you choose")
            .value("Jump", "Right Click", "Break CoolDown");

    public NoDelay() {
        super("NoDelay", "No Delay", ModuleCategory.PLAYER);
        setup(ignoreSetting);
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (ignoreSetting.isSelected("Break CoolDown")) mc.interactionManager.blockBreakingCooldown = 0;
        if (ignoreSetting.isSelected("Jump")) mc.player.jumpingCooldown = 0;
        if (ignoreSetting.isSelected("Right Click")) mc.itemUseCooldown = 0;
    }
}