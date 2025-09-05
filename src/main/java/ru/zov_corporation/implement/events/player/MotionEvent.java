package ru.zov_corporation.implement.events.player;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.zov_corporation.api.event.events.callables.EventCancellable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotionEvent extends EventCancellable {
    double x, y, z;
    float yaw, pitch;
    boolean onGround;
}
