package ru.zov_corporation.implement.features.modules.combat;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.implement.events.player.TickEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoPotion extends Module {
    public AutoPotion() {
        super("AutoPotion", "Auto Potion", ModuleCategory.COMBAT);
        setup();
    }

    @EventHandler
    public void onTick(TickEvent e) {

    }
}
