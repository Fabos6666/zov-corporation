package ru.zov_corporation.api.system.logger.implement;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import ru.zov_corporation.api.system.logger.Logger;

public class ConsoleLogger implements Logger {
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger("ZOV_CORPORATION");

    @Override
    public void log(Object message) {
        logger.info("[ZE{}NI{}TH] {}", Formatting.BLUE, Formatting.RED, message);
    }

    @Override
    public void minecraftLog(Text... components) {

    }
}
