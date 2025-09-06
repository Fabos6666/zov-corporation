package ru.zov_corporation.implement.features.commands.defaults;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ru.zov_corporation.core.Main;
import ru.zov_corporation.api.feature.command.Command;
import ru.zov_corporation.api.feature.command.argument.IArgConsumer;
import ru.zov_corporation.api.feature.command.exception.CommandException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class KeyCommand extends Command {
    Main main;
    private final Random random = new Random();
    
    // Массив рандомных сообщений
    private final String[] messages = {
        "Ну, все получили? 👆",
        "Брат, забери ключ👆",
        "Аууу брат, лутай ключ 👆",
        "БРАТ ЗАЙДИ В СЕТЬ👆",
        "Ключ готов, братан! 👆",
        "Забирай ключ, не теряй! 👆",
        "Брат, ключ ждет тебя! 👆",
        "Время ключа пришло! 👆",
        "Ключ в сети, заходи! 👆",
        "Братан, ключ готов! 👆"
    };

    protected KeyCommand(Main main) {
        super("key");
        this.main = main;
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0); // Команда не принимает аргументы
        
        // Выбираем случайное сообщение
        String randomMessage = messages[random.nextInt(messages.length)];
        
        // Создаем сообщение с префиксом [ZOV] и случайным цветом
        MutableText message = Text.literal(randomMessage);
        Formatting[] colors = {Formatting.AQUA, Formatting.BLUE, Formatting.GREEN, 
                              Formatting.LIGHT_PURPLE, Formatting.YELLOW, Formatting.GOLD};
        Formatting randomColor = colors[random.nextInt(colors.length)];
        message.setStyle(message.getStyle().withColor(randomColor));
        
        logDirect(message);
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        return Stream.empty(); // Команда не имеет аргументов
    }

    @Override
    public String getShortDesc() {
        return "Получить рандомное сообщение о ключе";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "Команда выводит случайное мотивирующее сообщение о ключе",
                "",
                "Использование:",
                "> key - Выводит случайное сообщение с префиксом [ZOV]"
        );
    }
}
