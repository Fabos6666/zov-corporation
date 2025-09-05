package ru.zov_corporation.api.feature.command;

import java.util.UUID;

public interface IBaritoneChatControl {
    String FORCE_COMMAND_PREFIX = String.format("<<%s>>", UUID.randomUUID());
}
