package ru.zov_corporation.api.system.discord;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Identifier;
import ru.zov_corporation.api.system.discord.utils.*;
import ru.zov_corporation.common.QuickImports;
import ru.zov_corporation.common.util.other.BufferUtil;
import ru.zov_corporation.common.util.other.StringUtil;
import ru.zov_corporation.core.Main;

import java.io.IOException;

@Setter
@Getter
public class DiscordManager implements QuickImports {
    private final DiscordDaemonThread discordDaemonThread = new DiscordDaemonThread();
    private boolean running = true;
    private DiscordInfo info = new DiscordInfo("Unknown","","");
    private Identifier avatarId;

    public void init() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().ready((user) -> {
            Main.getInstance().getDiscordManager().setInfo(new DiscordInfo(user.username,"https://cdn.discordapp.com/avatars/" + user.userId + "/" + user.avatar + ".png",user.userId));
            DiscordRichPresence richPresence = new DiscordRichPresence.Builder()
                    .setStartTimestamp(System.currentTimeMillis() / 1000)
                    .setDetails("Role: " + StringUtil.getUserRole())
                    .setLargeImage("https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExM2xzOTZvc3N0NjNnZThyZTJtMHpmdmg5djFsNnJqd2V6cTA1NzVheiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/L1aSMzjfTdAybVHoLP/giphy.gif")
                    .setButtons(RPCButton.create("Телеграм", "https://t.me/zovclient"), RPCButton.create("Дискорд", "https://discord.gg/TPdfGKs7B3")).build();
            DiscordRPC.INSTANCE.Discord_UpdatePresence(richPresence);
        }).build();

        DiscordRPC.INSTANCE.Discord_Initialize("1413479021303566438", handlers, true, "");
        discordDaemonThread.start();
    }

    public void stopRPC() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
        this.running = false;
    }

    public void load() throws IOException {
        if (avatarId == null && !info.avatarUrl.isEmpty()) {
            avatarId = BufferUtil.registerDynamicTexture("avatar-", BufferUtil.getHeadFromURL(info.avatarUrl));
        }
    }

    private class DiscordDaemonThread extends Thread {
        @Override
        public void run() {
            this.setName("Discord-RPC");

            try {
                while (Main.getInstance().getDiscordManager().isRunning()) {
                    DiscordRPC.INSTANCE.Discord_RunCallbacks();
                    load();
                    Thread.sleep(15000);
                }
            } catch (Exception exception) {
                stopRPC();
            }
            super.run();
        }
    }


    public record DiscordInfo(String userName, String avatarUrl, String userId) {}
}