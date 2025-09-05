package ru.zov_corporation.api.feature.draggable;

import net.minecraft.client.gui.DrawContext;
import ru.zov_corporation.implement.events.container.SetScreenEvent;
import ru.zov_corporation.implement.events.packet.PacketEvent;

public interface Draggable {
    boolean visible();

    void tick();

    void render(DrawContext context, int mouseX, int mouseY, float delta);

    void packet(PacketEvent e);

    void setScreen(SetScreenEvent screen);

    boolean mouseClicked(double mouseX, double mouseY, int button);

    boolean mouseReleased(double mouseX, double mouseY, int button);
}
