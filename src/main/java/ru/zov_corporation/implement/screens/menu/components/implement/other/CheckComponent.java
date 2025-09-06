package ru.zov_corporation.implement.screens.menu.components.implement.other;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import ru.zov_corporation.api.system.animation.Animation;
import ru.zov_corporation.api.system.animation.implement.DecelerateAnimation;
import ru.zov_corporation.api.system.shape.ShapeProperties;
import ru.zov_corporation.common.util.color.ColorUtil;
import ru.zov_corporation.common.util.math.MathUtil;
import ru.zov_corporation.common.util.render.ScissorManager;
import ru.zov_corporation.core.Main;
import ru.zov_corporation.implement.screens.menu.components.AbstractComponent;

import static ru.zov_corporation.api.system.animation.Direction.BACKWARDS;
import static ru.zov_corporation.api.system.animation.Direction.FORWARDS;

@Setter
@Accessors(chain = true)
public class CheckComponent extends AbstractComponent {
    private boolean state;
    private Runnable runnable;

    private final Animation alphaAnimation = new DecelerateAnimation()
            .setMs(300)
            .setValue(255);

    private final Animation stencilAnimation = new DecelerateAnimation()
            .setMs(200)
            .setValue(8);


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrix = context.getMatrices();

        alphaAnimation.setDirection(state ? FORWARDS : BACKWARDS);
        stencilAnimation.setDirection(state ? FORWARDS : BACKWARDS);

        int stateColor = state ? ColorUtil.getClientColor() : ColorUtil.getGuiRectColor(1);
        int outlineStateColor = state ? ColorUtil.getClientColor() : ColorUtil.getOutline();

        int opacity = alphaAnimation.getOutput().intValue();

        rectangle.render(ShapeProperties.create(matrix, x, y, 8, 8)
                .round(1.5F).thickness(2).softness(0.5F).outlineColor(outlineStateColor).color(MathUtil.applyOpacity(stateColor, opacity)).build());

        ScissorManager scissor = Main.getInstance().getScissorManager();
        scissor.push(matrix.peek().getPositionMatrix(), x, y, stencilAnimation.getOutput().intValue(), 8);

        image.setTexture("textures/check.png").render(ShapeProperties.create(matrix, x + 2, y + 2.5, 4, 3).color(MathUtil.applyOpacity(0xFFFFFFFF, opacity)).build());

        scissor.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (MathUtil.isHovered(mouseX, mouseY, x, y, 8, 8) && button == 0) {
            runnable.run();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
