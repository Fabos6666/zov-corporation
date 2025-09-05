package ru.zov_corporation.api.feature.command.datatypes;

import ru.zov_corporation.api.feature.command.argument.IArgConsumer;

public interface IDatatypeContext {
    IArgConsumer getConsumer();
}
