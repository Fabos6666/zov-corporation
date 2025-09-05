package ru.zov_corporation.implement.events.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.zov_corporation.api.event.events.Event;

@Getter
@AllArgsConstructor
public class RotationUpdateEvent implements Event {
    byte type;
}
