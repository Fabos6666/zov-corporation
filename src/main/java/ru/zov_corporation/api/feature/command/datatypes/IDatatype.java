package ru.zov_corporation.api.feature.command.datatypes;

import ru.zov_corporation.api.feature.command.exception.CommandException;
import ru.zov_corporation.common.QuickImports;

import java.util.stream.Stream;

public interface IDatatype extends QuickImports {
    Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException;
}
