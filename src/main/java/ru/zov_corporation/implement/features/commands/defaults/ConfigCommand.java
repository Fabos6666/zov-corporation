package ru.zov_corporation.implement.features.commands.defaults;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ru.zov_corporation.api.file.FileRepository;
import ru.zov_corporation.core.Main;
import ru.zov_corporation.core.client.ClientInfoProvider;
import ru.zov_corporation.api.feature.command.Command;
import ru.zov_corporation.api.feature.command.argument.IArgConsumer;
import ru.zov_corporation.api.feature.command.datatypes.ConfigFileDataType;
import ru.zov_corporation.api.feature.command.exception.CommandException;
import ru.zov_corporation.api.feature.command.helpers.Paginator;
import ru.zov_corporation.api.feature.command.helpers.TabCompleteHelper;
import ru.zov_corporation.api.file.FileController;
import ru.zov_corporation.api.file.exception.FileProcessingException;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.setting.Setting;
import ru.zov_corporation.api.feature.module.setting.implement.*;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static ru.zov_corporation.api.feature.command.IBaritoneChatControl.FORCE_COMMAND_PREFIX;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigCommand extends Command {
    FileController fileController;
    ClientInfoProvider clientInfoProvider;
    Main main;

    protected ConfigCommand(Main main) {
        super("config", "cfg");
        this.fileController = main.getFileController();
        this.clientInfoProvider = main.getClientInfoProvider();
        this.main = main;
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        String arg = args.hasAny() ? args.getString().toLowerCase(Locale.US) : "list";
        args.requireMax(1);
        
        if (arg.contains("reset")) {
            // Сразу сбрасываем все модули и настройки
            resetAllToDefaults();
            logDirect("Все модули и настройки сброшены к значениям по умолчанию!", Formatting.GREEN);
            return;
        }
        if (arg.contains("load")) {
            String name = args.getString();
            if (new File(clientInfoProvider.configsDir(), name + ".json").exists()) {
                try {
                    var fileRepository = new FileRepository();
                    fileRepository.setup(Main.getInstance());
                    var fileController = new FileController(fileRepository.getClientFiles(), clientInfoProvider.filesDir(), clientInfoProvider.configsDir());
                    fileController.loadFile(name + ".json");
                    logDirect(String.format("Конфигурация %s загружена!", name));
                } catch (FileProcessingException e) {
                    logDirect(String.format("Ошибка при загрузке конфига! Детали: %s", e.getCause().getMessage()), Formatting.RED);
                }
            } else {
                logDirect(String.format("Конфигурация %s не найдена!", name));
            }
        }
        if (arg.contains("save")) {

            String name = args.getString();

            try {
                fileController.saveFile(name + ".json");
                logDirect(String.format("Конфигурация %s сохранена!", name));
                System.out.println("loaded");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.printf("error %s%n", e.getCause().getMessage());
                logDirect(String.format("Ошибка при сохранении конфига! Детали: %s", e.getCause().getMessage()), Formatting.RED);
            }
        }
        if (arg.contains("list")) {
            Paginator.paginate(
                    args, new Paginator<>(
                            getConfigs()),
                    () -> logDirect("Список конфигов:"),
                    config -> {
                        MutableText namesComponent = Text.literal(config);
                        namesComponent.setStyle(namesComponent.getStyle().withColor(Formatting.WHITE));
                        return namesComponent;
                    },
                    FORCE_COMMAND_PREFIX + label
            );
        }
        if (arg.contains("dir")) {
            try {
                new ProcessBuilder("explorer", clientInfoProvider.configsDir().getAbsolutePath()).start();
            } catch (IOException e) {
                logDirect("Папка с конфигурациями не найдена!" + e.getMessage());
            }
        }
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        if (args.hasAny()) {
            String arg = args.getString();
            if (args.hasExactlyOne()) {
                if (arg.equalsIgnoreCase("load")) {
                    return args.tabCompleteDatatype(ConfigFileDataType.INSTANCE);
                } else if (arg.equalsIgnoreCase("save")) {
                    return args.tabCompleteDatatype(ConfigFileDataType.INSTANCE);
                }
            } else {
                return new TabCompleteHelper()
                        .sortAlphabetically()
                        .prepend("load", "save", "list", "dir", "reset")
                        .filterPrefix(arg)
                        .stream();
            }
        }
        return Stream.empty();
    }


    @Override
    public String getShortDesc() {
        return "Позволяет взаимодействовать с конфигами в чите";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "С помощью этой команды можно загружать/сохранять конфиги и сбрасывать настройки",
                "",
                "Использование:",
                "> config load <name> - Загружает конфиг.",
                "> config save <name> - Сохраняет конфиг.",
                "> config list - Возвращает список конфигов",
                "> config dir - Открывает папку с конфигами.",
                "> config reset - Сбрасывает все модули и настройки к значениям по умолчанию"
        );
    }
    
    public List<String> getConfigs() {
        List<String> configs = new ArrayList<>();
        File[] configFiles = Main.getInstance().getClientInfoProvider().configsDir().listFiles();

        if (configFiles != null) {
            for (File configFile : configFiles) {
                if (configFile.isFile() && configFile.getName().endsWith(".json")) {
                    String configName = configFile.getName().replace(".json", "");
                    configs.add(configName);
                }
            }
        }

        return configs;
    }
    
    /**
     * Сбрасывает все модули и настройки к значениям по умолчанию
     */
    private void resetAllToDefaults() {
        // Отключаем все модули
        for (Module module : main.getModuleRepository().modules()) {
            module.setState(false);
            module.setKey(GLFW.GLFW_KEY_UNKNOWN);
            module.setType(1);
        }
        
        // Сбрасываем все настройки к значениям по умолчанию
        for (Module module : main.getModuleRepository().modules()) {
            for (Setting setting : module.settings()) {
                resetSettingToDefault(setting);
            }
        }
    }
    
    /**
     * Сбрасывает настройку к значению по умолчанию
     */
    private void resetSettingToDefault(Setting setting) {
        if (setting instanceof BooleanSetting booleanSetting) {
            booleanSetting.setValue(false);
        } else if (setting instanceof ValueSetting valueSetting) {
            valueSetting.setValue(valueSetting.getMin());
        } else if (setting instanceof ColorSetting colorSetting) {
            colorSetting.setColor(0xFFFFFFFF); // Белый цвет по умолчанию
        } else if (setting instanceof BindSetting bindSetting) {
            bindSetting.setKey(GLFW.GLFW_KEY_UNKNOWN);
        } else if (setting instanceof TextSetting textSetting) {
            textSetting.setText("");
        } else if (setting instanceof SelectSetting selectSetting) {
            if (!selectSetting.getList().isEmpty()) {
                selectSetting.setSelected(selectSetting.getList().get(0));
            }
        } else if (setting instanceof MultiSelectSetting multiSelectSetting) {
            multiSelectSetting.setSelected(new ArrayList<>());
        } else if (setting instanceof GroupSetting groupSetting) {
            groupSetting.setValue(false);
            for (Setting subSetting : groupSetting.getSubSettings()) {
                resetSettingToDefault(subSetting);
            }
        }
    }
}
