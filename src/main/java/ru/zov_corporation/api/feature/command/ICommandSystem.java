package ru.zov_corporation.api.feature.command;

import ru.zov_corporation.api.feature.command.argparser.IArgParserManager;

public interface ICommandSystem {
    IArgParserManager getParserManager();
}
