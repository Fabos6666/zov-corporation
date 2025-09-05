package ru.zov_corporation.implement.events.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.DrawContext;
import ru.zov_corporation.api.event.events.Event;
import ru.zov_corporation.api.system.draw.DrawEngine;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DrawEvent implements Event {
    DrawContext drawContext;
    DrawEngine drawEngine;
    float partialTicks;
}
