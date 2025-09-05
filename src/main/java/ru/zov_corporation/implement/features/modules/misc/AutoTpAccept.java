package ru.zov_corporation.implement.features.modules.misc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.BooleanSetting;
import ru.zov_corporation.api.repository.friend.FriendUtils;
import ru.zov_corporation.common.util.world.ServerUtil;
import ru.zov_corporation.implement.events.packet.PacketEvent;
import ru.zov_corporation.implement.events.player.TickEvent;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoTpAccept extends Module {
    private final String[] teleportMessages = new String[]{
            "has requested teleport",
            "просит телепортироваться",
            "хочет телепортироваться к вам",
            "просит к вам телепортироваться"
    };
    private boolean canAccept;

    private final BooleanSetting friendSetting = new BooleanSetting("Only Friends", "Will only accept requests from friends").setValue(false);

    public AutoTpAccept() {
        super("AutoTpAccept", "Auto Tp Accept", ModuleCategory.MISC);
        setup(friendSetting);
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof GameMessageS2CPacket m) {
            String message = m.content().getString();
            boolean validPlayer = !friendSetting.isValue() || FriendUtils.getFriends().stream().anyMatch(s -> message.contains(s.getName()));
            if (isTeleportMessage(message)) {
                canAccept = validPlayer;
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (!ServerUtil.isPvp() && canAccept) {
            mc.player.networkHandler.sendChatCommand("tpaccept");
            canAccept = false;
        }
    }

    
    private boolean isTeleportMessage(String message) {
        return Arrays.stream(this.teleportMessages).map(String::toLowerCase).anyMatch(message::contains);
    }
}