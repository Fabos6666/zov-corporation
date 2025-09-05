package ru.zov_corporation.common.trait;

import ru.zov_corporation.api.feature.module.setting.Setting;

public interface Setupable {
    void setup(Setting... settings);
}