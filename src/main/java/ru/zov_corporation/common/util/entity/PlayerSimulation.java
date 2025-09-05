package ru.zov_corporation.common.util.entity;

import net.minecraft.util.math.Vec3d;

public interface PlayerSimulation {
    Vec3d pos();

    void tick();
}