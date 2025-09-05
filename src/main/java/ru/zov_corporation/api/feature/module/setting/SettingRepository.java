package ru.zov_corporation.api.feature.module.setting;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.zov_corporation.common.trait.Setupable;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingRepository implements Setupable {
    List<Setting> settings = Lists.newArrayList();

    @Override
    public final void setup(Setting... setting) {
        settings.addAll(Arrays.asList(setting));
    }

    public Setting get(String name) {
        return settings.stream()
                .filter(setting -> setting.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Setting> settings() {
        return settings;
    }
}
