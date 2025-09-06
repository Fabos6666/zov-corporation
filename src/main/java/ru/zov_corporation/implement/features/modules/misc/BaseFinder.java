package ru.zov_corporation.implement.features.modules.misc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import ru.zov_corporation.api.event.EventHandler;
import ru.zov_corporation.api.feature.module.Module;
import ru.zov_corporation.api.feature.module.ModuleCategory;
import ru.zov_corporation.api.feature.module.setting.implement.ValueSetting;
import ru.zov_corporation.common.util.other.StopWatch;
import ru.zov_corporation.common.util.render.Render3DUtil;
import ru.zov_corporation.implement.events.render.WorldRenderEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseFinder extends Module {
    
    final ValueSetting range = new ValueSetting("Радиус", "Радиус поиска баз")
            .range(10, 200)
            .setValue(50);
    
    final ValueSetting scanSpeed = new ValueSetting("Скорость", "Скорость сканирования")
            .range(1, 20)
            .setValue(5);

    final List<BlockPos> potentialBases = new ArrayList<>();
    final StopWatch timerUtil = new StopWatch();
    
    int currentX, currentY, currentZ;
    int startX, endX, startY, endY, startZ, endZ;
    boolean isScanning = false;
    
    public BaseFinder() {
        super("BaseFinder", "Base Finder", ModuleCategory.MISC);
        setup(range, scanSpeed);
    }

    @Override
    public void activate() {
        timerUtil.reset();
        isScanning = false;
        potentialBases.clear();
        super.activate();
    }

    @Override
    public void deactivate() {
        isScanning = false;
        potentialBases.clear();
        super.deactivate();
    }

    @EventHandler
    public void onWorldRender(WorldRenderEvent event) {
        if (mc.player == null || mc.world == null) return;
        
        // Рендерим только потенциальные базы
        for (BlockPos pos : potentialBases) {
            drawBox(pos, Color.red.getRGB());
        }
        
        // Начинаем новое сканирование если предыдущее завершено
        if (!isScanning && timerUtil.finished(2000)) {
            startScan();
        }
        
        // Продолжаем текущее сканирование
        if (isScanning) {
            continueScan();
        }
    }

    private void startScan() {
        if (mc.player == null || mc.world == null) return;
        
        int radius = range.getInt();
        BlockPos playerPos = mc.player.getBlockPos();
        
        startX = playerPos.getX() - radius;
        endX = playerPos.getX() + radius;
        startY = Math.max(mc.world.getBottomY(), playerPos.getY() - 20);
        endY = Math.min(mc.world.getTopYInclusive(), playerPos.getY() + 20);
        startZ = playerPos.getZ() - radius;
        endZ = playerPos.getZ() + radius;
        
        currentX = startX;
        currentY = startY;
        currentZ = startZ;
        
        isScanning = true;
        timerUtil.reset();
    }

    private void continueScan() {
        if (mc.player == null || mc.world == null) {
            isScanning = false;
            return;
        }
        
        int blocksPerTick = scanSpeed.getInt();
        int scanned = 0;
        
        while (scanned < blocksPerTick && isScanning) {
            BlockPos pos = new BlockPos(currentX, currentY, currentZ);
            Block block = mc.world.getBlockState(pos).getBlock();
            
            // Проверяем, может ли это быть база
            if (isPotentialBase(block)) {
                // Добавляем только если блока еще нет в списке
                if (!potentialBases.contains(pos)) {
                    potentialBases.add(pos);
                }
            }
            
            scanned++;
            
            // Переходим к следующему блоку
            currentX++;
            if (currentX > endX) {
                currentX = startX;
                currentZ++;
                if (currentZ > endZ) {
                    currentZ = startZ;
                    currentY++;
                    if (currentY > endY) {
                        // Сканирование завершено
                        isScanning = false;
                        sendScanCompleteMessage();
                        break;
                    }
                }
            }
        }
    }

    private boolean isPotentialBase(Block block) {
        // Блоки, которые могут указывать на базу
        return block == Blocks.CHEST ||
               block == Blocks.ENDER_CHEST ||
               block == Blocks.BARREL ||
               block == Blocks.SHULKER_BOX ||
               block == Blocks.ANVIL ||
               block == Blocks.ENCHANTING_TABLE ||
               block == Blocks.END_PORTAL_FRAME ||
               block == Blocks.BEACON ||
               block == Blocks.DISPENSER ||
               block == Blocks.DROPPER ||
               block == Blocks.HOPPER ||
               block == Blocks.FURNACE ||
               block == Blocks.BLAST_FURNACE ||
               block == Blocks.SMOKER ||
               block == Blocks.CRAFTING_TABLE ||
               block == Blocks.BREWING_STAND ||
               block == Blocks.CAULDRON ||
               block == Blocks.IRON_DOOR ||
               block == Blocks.IRON_TRAPDOOR ||
               block == Blocks.IRON_BARS ||
               block == Blocks.OBSIDIAN ||
               block == Blocks.CRYING_OBSIDIAN ||
               block == Blocks.RESPAWN_ANCHOR ||
               block == Blocks.LODESTONE ||
               block == Blocks.LECTERN ||
               block == Blocks.SMITHING_TABLE ||
               block == Blocks.CARTOGRAPHY_TABLE ||
               block == Blocks.FLETCHING_TABLE ||
               block == Blocks.LOOM ||
               block == Blocks.STONECUTTER ||
               block == Blocks.GRINDSTONE;
    }

    private void sendScanCompleteMessage() {
        if (mc.player != null) {
            int baseCount = potentialBases.size();
            String message = "§8[§6ZOV§8] §7BaseFinder: §aСканирование завершено! Найдено потенциальных баз: §e" + baseCount;
            mc.player.sendMessage(Text.literal(message), false);
        }
    }

    public void drawBox(BlockPos blockPos, int color) {
        Render3DUtil.drawBox(new Box(blockPos), color, 1);
    }
}
