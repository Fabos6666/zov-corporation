package ru.zov_corporation.api.repository.macro;

import ru.zov_corporation.api.event.EventManager;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.implement.events.keyboard.KeyEvent;
import ru.zov_corporation.common.QuickImports;
import ru.zov_corporation.common.QuickLogger;

import java.util.ArrayList;
import java.util.List;

public class MacroRepository implements QuickImports, QuickLogger {
    public MacroRepository(EventManager eventManager) {
        eventManager.register(this);
    }

    public List<Macro> macroList = new ArrayList<>();

    public void addMacro(String name, String message, int key) {
        macroList.add(new Macro(name, message, key));
    }

    public boolean hasMacro(String text) {
        return macroList.stream().anyMatch(macro -> macro.name().equalsIgnoreCase(text));
    }

    public void deleteMacro(String text) {
        macroList.removeIf(macro -> macro.name().equalsIgnoreCase(text));
    }

    public void clearList() {
        macroList.clear();
    }

    @EventHandler
    public void onKey(KeyEvent e) {
        if (mc.player != null && e.action() == 0 && mc.currentScreen == null) macroList.stream().filter(macro -> macro.key() == e.key())
                .findFirst().ifPresent(macro -> mc.player.networkHandler.sendChatMessage(macro.message()));
    }
}