package ru.zov_corporation.implement.events.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import ru.zov_corporation.api.event.events.Event;

public record BlockBreakingEvent(BlockPos blockPos, Direction direction) implements Event {}
