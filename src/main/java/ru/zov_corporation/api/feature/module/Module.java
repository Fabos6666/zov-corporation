package ru.zov_corporation.api.feature.module;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import ru.zov_corporation.api.system.animation.Animation;
import ru.zov_corporation.api.system.animation.Direction;
import ru.zov_corporation.api.system.animation.implement.DecelerateAnimation;
import ru.zov_corporation.api.system.sound.SoundManager;
import ru.zov_corporation.core.Main;
import ru.zov_corporation.api.feature.module.setting.SettingRepository;
import ru.zov_corporation.api.event.EventManager;
import ru.zov_corporation.common.QuickImports;
import ru.zov_corporation.implement.features.draggables.Notifications;
import ru.zov_corporation.implement.features.modules.render.Hud;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Module extends SettingRepository implements QuickImports {
    String name;
    String visibleName;
    ModuleCategory category;
    Animation animation = new DecelerateAnimation().setMs(150).setValue(1);

    public Module(String name, ModuleCategory category) {
        this.name = name;
        this.category = category;
        this.visibleName = name;
    }

    public Module(String name, String visibleName, ModuleCategory category) {
        this.name = name;
        this.visibleName = visibleName;
        this.category = category;
    }

    @NonFinal
    int key = GLFW.GLFW_KEY_UNKNOWN,type = 1;

    @NonFinal
    public boolean state;

    public void switchState() {
        setState(!state);
    }

    public void setState(boolean state) {
        animation.setDirection(state ? Direction.FORWARDS : Direction.BACKWARDS);
        if (state != this.state) {
            this.state = state;
            handleStateChange();
        }
    }

    private void handleStateChange() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.world != null) {
            if (state) {
                if (Hud.getInstance().notificationSettings.isSelected("Module Switch")) {
                    Notifications.getInstance().addList("Модуль " + Formatting.GREEN + visibleName + Formatting.RESET + " - включен!", 2000, SoundManager.ENABLE_MODULE);
                }
                activate();
            } else {
                if (Hud.getInstance().notificationSettings.isSelected("Module Switch")) {
                    Notifications.getInstance().addList("Модуль " + Formatting.RED + visibleName + Formatting.RESET + " - выключен!", 2000, SoundManager.DISABLE_MODULE);
                }
                deactivate();
            }
        }
        toggleSilent(state);
    }

    private void toggleSilent(boolean activate) {
        EventManager eventManager = Main.getInstance().getEventManager();
        if (activate) {
            eventManager.register(this);
        } else {
            eventManager.unregister(this);
        }
    }

    public void activate() {
    }

    public void deactivate() {
    }
}
