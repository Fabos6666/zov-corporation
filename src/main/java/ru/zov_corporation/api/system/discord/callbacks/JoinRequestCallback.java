package ru.zov_corporation.api.system.discord.callbacks;

import com.sun.jna.Callback;
import ru.zov_corporation.api.system.discord.utils.DiscordUser;

public interface JoinRequestCallback extends Callback {
    void apply(DiscordUser var1);
}