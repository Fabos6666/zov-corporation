package ru.zov_corporation.implement.events.container;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.screen.Screen;
import ru.zov_corporation.api.event.events.Event;


@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetScreenEvent implements Event {
    public Screen screen;
}
