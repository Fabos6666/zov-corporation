package ru.zov_corporation.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import ru.zov_corporation.api.file.exception.FileProcessingException;
import ru.zov_corporation.api.repository.box.BoxESPRepository;
import ru.zov_corporation.api.repository.rct.RCTRepository;
import ru.zov_corporation.api.repository.way.WayRepository;
import ru.zov_corporation.api.system.discord.DiscordManager;
import ru.zov_corporation.api.feature.draggable.DraggableRepository;
import ru.zov_corporation.api.file.*;
import ru.zov_corporation.api.repository.macro.MacroRepository;
import ru.zov_corporation.api.event.EventManager;
import ru.zov_corporation.api.feature.module.ModuleProvider;
import ru.zov_corporation.api.feature.module.ModuleRepository;
import ru.zov_corporation.api.feature.module.ModuleSwitcher;
import ru.zov_corporation.api.system.sound.SoundManager;
import ru.zov_corporation.common.util.logger.LoggerUtil;
import ru.zov_corporation.common.util.render.ScissorManager;
import ru.zov_corporation.core.client.ClientInfo;
import ru.zov_corporation.core.client.ClientInfoProvider;
import ru.zov_corporation.core.listener.ListenerRepository;
import ru.zov_corporation.implement.features.commands.CommandDispatcher;
import ru.zov_corporation.implement.features.commands.manager.CommandRepository;
import ru.zov_corporation.implement.features.modules.combat.killaura.attack.AttackPerpetrator;
import ru.zov_corporation.implement.screens.menu.MenuScreen;

import java.io.File;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Main implements ModInitializer {

    @Getter
    static Main instance;
    EventManager eventManager = new EventManager();
    ModuleRepository moduleRepository;
    ModuleSwitcher moduleSwitcher;
    CommandRepository commandRepository;
    CommandDispatcher commandDispatcher;
    BoxESPRepository boxESPRepository = new BoxESPRepository(eventManager);
    MacroRepository macroRepository = new MacroRepository(eventManager);
    WayRepository wayRepository = new WayRepository(eventManager);
    RCTRepository RCTRepository = new RCTRepository(eventManager);
    ModuleProvider moduleProvider;
    DraggableRepository draggableRepository;
    DiscordManager discordManager;
    FileRepository fileRepository;
    FileController fileController;
    ScissorManager scissorManager = new ScissorManager();
    ClientInfoProvider clientInfoProvider;
    ListenerRepository listenerRepository;
    AttackPerpetrator attackPerpetrator = new AttackPerpetrator();
    boolean initialized;

    @Override
    public void onInitialize() {
        instance = this;

        initClientInfoProvider();
        initModules();
        initDraggable();
        initFileManager();
        initCommands();
        initListeners();
        initDiscordRPC();
        SoundManager.init();
        MenuScreen menuScreen = new MenuScreen();
        menuScreen.initialize();

        initialized = true;
    }

    private void initDraggable() {
        draggableRepository = new DraggableRepository();
        draggableRepository.setup();
    }

    private void initModules() {
        moduleRepository = new ModuleRepository();
        moduleRepository.setup();
        moduleProvider = new ModuleProvider(moduleRepository.modules());
        moduleSwitcher = new ModuleSwitcher(moduleRepository.modules(), eventManager);
    }

    private void initCommands() {
        commandRepository = new CommandRepository();
        commandDispatcher = new CommandDispatcher(eventManager);
    }


    private void initDiscordRPC() {
        discordManager = new DiscordManager();
        discordManager.init();
    }


    private void initClientInfoProvider() {
        File clientDirectory = new File(MinecraftClient.getInstance().runDirectory, "\\zov_corporation\\");
        File filesDirectory = new File(clientDirectory, "\\files\\");
        File moduleFilesDirectory = new File(filesDirectory, "\\config\\");
        clientInfoProvider = new ClientInfo("ZOV_CORPORATION", "FABOS", "ADMIN", clientDirectory, filesDirectory, moduleFilesDirectory);
    }

    private void initFileManager() {
        DirectoryCreator directoryCreator = new DirectoryCreator();
        directoryCreator.createDirectories(clientInfoProvider.clientDir(), clientInfoProvider.filesDir(), clientInfoProvider.configsDir());
        fileRepository = new FileRepository();
        fileRepository.setup(this);
        fileController = new FileController(fileRepository.getClientFiles(), clientInfoProvider.filesDir(), clientInfoProvider.configsDir());
        try {
            fileController.loadFiles();
        } catch (FileProcessingException e) {
            LoggerUtil.error("Error occurred while loading files: " + e.getMessage() + " " + e.getCause());
        }
    }


    private void initListeners() {
        listenerRepository = new ListenerRepository();
        listenerRepository.setup();
    }
}
