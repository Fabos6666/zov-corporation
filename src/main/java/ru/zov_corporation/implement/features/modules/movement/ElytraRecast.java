package ru.zov_corporation.implement.features.modules.movement;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.common.util.entity.MovingUtil;
import ru.zov_corporation.common.util.entity.PlayerIntersectionUtil;
import ru.zov_corporation.implement.events.player.InputEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElytraRecast extends Module {

    public ElytraRecast() {super("ElytraRecast", "Elytra Recast", ModuleCategory.MOVEMENT);}

    @EventHandler
    public void onInput(InputEvent e) {
        if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA) && MovingUtil.hasPlayerMovement()) {
            if (mc.player.isOnGround()) e.setJumping(true);
            else if (!mc.player.isGliding()) PlayerIntersectionUtil.startFallFlying();
        }
    }
}
