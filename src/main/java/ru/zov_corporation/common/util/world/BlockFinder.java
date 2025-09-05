package ru.zov_corporation.common.util.world;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.block.entity.BlockEntity;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.common.QuickImports;
import ru.zov_corporation.core.Main;
import ru.zov_corporation.implement.events.block.BlockEntityProgressEvent;
import ru.zov_corporation.implement.events.render.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockFinder implements QuickImports {
    public List<BlockEntity> blockEntities = new ArrayList<>();
    public static BlockFinder INSTANCE = new BlockFinder();

    public BlockFinder() {
        Main.getInstance().getEventManager().register(this);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        blockEntities.clear();
    }

    @EventHandler
    public void onBlockEntityProgress(BlockEntityProgressEvent e) {
        BlockEntity blockEntity = e.blockEntity();
        switch (e.type()) {
            case BlockEntityProgressEvent.Type.ADD -> {
                if (!blockEntities.stream().map(BlockEntity::getPos).toList().contains(blockEntity.getPos())) {
                    blockEntities.add(blockEntity);
                }
            }
            case BlockEntityProgressEvent.Type.REMOVE -> blockEntities.remove(blockEntity);
        }
    }
}