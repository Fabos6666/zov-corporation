package ru.zov_corporation.implement.features.modules.misc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.BooleanSetting;
import ru.zov_corporation.api.feature.module.setting.implement.ValueSetting;
import ru.zov_corporation.common.util.math.MathUtil;
import ru.zov_corporation.common.util.entity.MovingUtil;
import ru.zov_corporation.common.util.other.Instance;
import ru.zov_corporation.common.util.render.Render3DUtil;
import ru.zov_corporation.implement.events.packet.PacketEvent;
import ru.zov_corporation.implement.events.player.*;
import ru.zov_corporation.implement.events.render.CameraPositionEvent;
import ru.zov_corporation.implement.events.render.WorldRenderEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebugCamera extends Module {
    public static DebugCamera getInstance() {
        return Instance.get(DebugCamera.class);
    }

    private final ValueSetting speedSetting = new ValueSetting("Speed", "Select debug camera speed  ").setValue(2.0F).range(0.5F, 5.0F);
    private final BooleanSetting freezeSetting = new BooleanSetting("Freeze", "You freeze in place").setValue(false);
    public Vec3d pos, prevPos;

    public DebugCamera() {
        super("DebugCamera", "Debug Camera", ModuleCategory.MISC);
        setup(speedSetting, freezeSetting);
    }

    
    @Override
    public void activate() {
        prevPos = pos = new Vec3d(mc.getEntityRenderDispatcher().camera.getPos().toVector3f());
        super.activate();
    }

    
    @EventHandler
    public void onPacket(PacketEvent e) {
        switch (e.getPacket()) {
            case PlayerMoveC2SPacket move when freezeSetting.isValue() -> e.cancel();
            case PlayerRespawnS2CPacket respawn -> setState(false);
            case GameJoinS2CPacket join -> setState(false);
            default -> {}
        }
    }

    
    @EventHandler
    public void onWorldRender(WorldRenderEvent e) {
        Render3DUtil.drawBox(mc.player.getBoundingBox().offset(MathUtil.interpolate(mc.player).subtract(mc.player.getPos())), -1, 1);
    }

    
    @EventHandler
    public void onMove(MoveEvent e) {
        if (freezeSetting.isValue()) {
            e.setMovement(Vec3d.ZERO);
        }
    }

    
    @EventHandler
    public void onInput(InputEvent e) {
        float speed = speedSetting.getValue();
        double[] motion = MovingUtil.calculateDirection(e.forward(), e.sideways(), speed);

        prevPos = pos;
        pos = pos.add(motion[0], e.getInput().jump() ? speed : e.getInput().sneak() ? -speed : 0, motion[1]);

        e.inputNone();
    }

    
    @EventHandler
    public void onCameraPosition(CameraPositionEvent e) {
        e.setPos(MathUtil.interpolate(prevPos, pos));
        mc.options.setPerspective(Perspective.FIRST_PERSON);
    }
}
