package ru.zov_corporation.api.feature.command.manager;

import net.minecraft.util.Pair;
import ru.zov_corporation.api.feature.command.ICommand;
import ru.zov_corporation.api.feature.command.argument.ICommandArgument;
import ru.zov_corporation.api.feature.command.registry.Registry;

import java.util.List;
import java.util.stream.Stream;

public interface ICommandManager {
    Registry<ICommand> getRegistry();

    ICommand getCommand(String name);

    boolean execute(String string);

    boolean execute(Pair<String, List<ICommandArgument>> expanded);

    Stream<String> tabComplete(Pair<String, List<ICommandArgument>> expanded);

    Stream<String> tabComplete(String prefix);
}
