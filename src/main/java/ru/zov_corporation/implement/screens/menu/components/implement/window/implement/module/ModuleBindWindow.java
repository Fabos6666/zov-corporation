package ru.zov_corporation.implement.screens.menu.components.implement.window.implement.module;

import lombok.RequiredArgsConstructor;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.implement.screens.menu.components.implement.window.implement.AbstractBindWindow;

@RequiredArgsConstructor
public class ModuleBindWindow extends AbstractBindWindow {
    private final Module module;

    @Override
    protected int getKey() {
        return module.getKey();
    }

    @Override
    protected void setKey(int key) {
        module.setKey(key);
    }

    @Override
    protected int getType() {
        return module.getType();
    }

    @Override
    protected void setType(int type) {
        module.setType(type);
    }
}