package ru.zov_corporation.api.feature.command.exception;

import net.minecraft.util.Formatting;
import ru.zov_corporation.api.feature.command.ICommand;
import ru.zov_corporation.api.feature.command.argument.ICommandArgument;
import ru.zov_corporation.common.QuickLogger;

import java.util.List;

public interface ICommandException extends QuickLogger {

    String getMessage();

    default void handle(ICommand command, List<ICommandArgument> args) {
        logDirect(
                this.getMessage(),
                Formatting.RED
        );
    }
}
