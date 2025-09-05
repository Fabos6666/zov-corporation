package ru.zov_corporation.api.feature.command.datatypes;

import ru.zov_corporation.api.feature.command.exception.CommandException;

public interface IDatatypePost<T, O> extends IDatatype {
    T apply(IDatatypeContext datatypeContext, O original) throws CommandException;
}
