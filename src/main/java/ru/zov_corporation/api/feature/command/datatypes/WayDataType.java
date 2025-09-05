package ru.zov_corporation.api.feature.command.datatypes;

import ru.zov_corporation.api.feature.command.exception.CommandException;
import ru.zov_corporation.api.feature.command.helpers.TabCompleteHelper;
import ru.zov_corporation.api.repository.way.Way;
import ru.zov_corporation.core.Main;

import java.util.List;
import java.util.stream.Stream;

public enum WayDataType implements IDatatypeFor<Way> {
    INSTANCE;

    @Override
    public Stream<String> tabComplete(IDatatypeContext datatypeContext) throws CommandException {
        Stream<String> ways = getWay().stream().map(Way::name);
        String context = datatypeContext.getConsumer().getString();
        return new TabCompleteHelper().append(ways).filterPrefix(context).sortAlphabetically().stream();
    }

    @Override
    public Way get(IDatatypeContext datatypeContext) throws CommandException {
        String text = datatypeContext.getConsumer().getString();
        return getWay().stream().filter(s -> s.name().equalsIgnoreCase(text)).findFirst().orElse(null);
    }

    private List<? extends Way> getWay() {
        return Main.getInstance().getWayRepository().wayList;
    }
}
