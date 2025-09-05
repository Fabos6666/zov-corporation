package ru.zov_corporation.implement.features.commands;

import ru.zov_corporation.api.feature.command.ICommandSystem;
import ru.zov_corporation.api.feature.command.argparser.IArgParserManager;
import ru.zov_corporation.implement.features.commands.argparser.ArgParserManager;

public enum CommandSystem implements ICommandSystem {
    INSTANCE;

    @Override
    public IArgParserManager getParserManager() {
        return ArgParserManager.INSTANCE;
    }
}
