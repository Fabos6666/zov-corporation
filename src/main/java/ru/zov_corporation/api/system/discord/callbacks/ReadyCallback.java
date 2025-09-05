package ru.zov_corporation.api.system.discord.callbacks;

import com.sun.jna.Callback;
import ru.zov_corporation.api.system.discord.utils.DiscordUser;

public interface ReadyCallback extends Callback {
    void apply(DiscordUser var1);
}