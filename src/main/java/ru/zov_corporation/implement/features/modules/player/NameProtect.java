package ru.zov_corporation.implement.features.modules.player;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.BooleanSetting;
import ru.zov_corporation.api.feature.module.setting.implement.TextSetting;
import ru.zov_corporation.api.repository.friend.FriendUtils;
import ru.zov_corporation.implement.events.render.TextFactoryEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NameProtect extends Module {
    TextSetting nameSetting = new TextSetting("Name", "Nickname that will be replaced with yours").setText("Protect").setMax(16);
    BooleanSetting friendsSetting = new BooleanSetting("Friends","Hides friends' nicknames").setValue(true);

    public NameProtect() {
        super("NameProtect","Name Protect", ModuleCategory.PLAYER);
        setup(nameSetting, friendsSetting);
    }

    @EventHandler
    public void onTextFactory(TextFactoryEvent e) {
        e.replaceText(mc.getSession().getUsername(), nameSetting.getText());
        if (friendsSetting.isValue()) FriendUtils.getFriends().forEach(friend -> e.replaceText(friend.getName(), nameSetting.getText()));
    }
}
