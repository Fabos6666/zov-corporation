package ru.zov_corporation.implement.events.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.zov_corporation.api.event.events.callables.EventCancellable;

@Getter
@Setter
@AllArgsConstructor
public class UsingItemEvent extends EventCancellable {
    byte type;
}
