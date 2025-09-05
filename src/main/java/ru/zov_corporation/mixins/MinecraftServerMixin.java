package ru.zov_corporation.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.zov_corporation.core.Main;
import ru.zov_corporation.api.file.exception.FileProcessingException;
import ru.zov_corporation.common.util.logger.LoggerUtil;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo ci) {
        if (Main.getInstance().isInitialized()) {
            try {
                Main.getInstance().getFileController().saveFiles();
            } catch (FileProcessingException e) {
                LoggerUtil.error("Error occurred while saving files: " + e.getMessage());
            }
        }
    }
}
