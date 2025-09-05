package ru.zov_corporation.api.feature.command.datatypes;

import ru.zov_corporation.api.feature.command.exception.CommandException;

public interface IDatatypeFor<T> extends IDatatype  {
    T get(IDatatypeContext datatypeContext) throws CommandException;
}
