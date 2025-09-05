package ru.zov_corporation.api.feature.command.exception;

import ru.zov_corporation.api.feature.command.ICommand;
import ru.zov_corporation.api.feature.command.argument.ICommandArgument;
import ru.zov_corporation.common.QuickLogger;

import java.util.List;

public class CommandNotFoundException extends CommandException implements QuickLogger {

    public final String command;

    public CommandNotFoundException(String command) {
        super(String.format("Команда не найдена: %s", command));
        this.command = command;
    }

    @Override
    public void handle(ICommand command, List<ICommandArgument> args) {
       logDirect(getMessage());
    }
}
