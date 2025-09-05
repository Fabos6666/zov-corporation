package ru.zenith.implement.features.draggables;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import ru.zenith.api.feature.draggable.AbstractDraggable;
import ru.zenith.api.system.font.FontRenderer;
import ru.zenith.api.system.font.Fonts;
import ru.zenith.api.system.shape.ShapeProperties;
import ru.zenith.common.util.color.ColorUtil;
import ru.zenith.common.util.math.MathUtil;
import ru.zenith.common.util.other.StringUtil;
import ru.zenith.core.Main;

public class Watermark extends AbstractDraggable {
    private int fpsCount = 0;

    public Watermark() {
        super("Watermark", 10, 10, 92, 16,true);
    }

    @Override
    public void tick() {
        fpsCount = (int) MathUtil.interpolate(fpsCount, mc.getCurrentFps());
    }

    @Override
    public void drawDraggable(DrawContext e) {
        MatrixStack matrix = e.getMatrices();
        FontRenderer font = Fonts.getSize(15, Fonts.Type.DEFAULT);

        String offset = "      ";
        String name = Main.getInstance().getClientInfoProvider().clientName() + offset;
        String version = StringUtil.getUserRole() + offset;
        String fps = fpsCount + " FPS";

        blur.render(ShapeProperties.create(matrix, getX(), getY(), getWidth(), getHeight())
                .round(3).softness(1).thickness(2).outlineColor(ColorUtil.getOutline()).color(ColorUtil.getRect(0.7F)).build());
        font.drawGradientString(matrix, name, getX() + 5, getY() + 6.5F, ColorUtil.fade(0), ColorUtil.fade(100));
        font.drawString(matrix, version + fps, getX() + font.getStringWidth(name) + 5, getY() + 6.5F, ColorUtil.getText());
        rectangle.render(ShapeProperties.create(matrix, getX() + font.getStringWidth(name),getY() + 4,0.5F,getHeight() - 8).color(ColorUtil.getOutline(0.75F,0.5f)).build());
        rectangle.render(ShapeProperties.create(matrix, getX() + font.getStringWidth(name + version),getY() + 4,0.5F,getHeight() - 8).color(ColorUtil.getOutline(0.75F,0.5f)).build());
        setWidth((int) (font.getStringWidth(name + version + fps) + 9));
    }
}
