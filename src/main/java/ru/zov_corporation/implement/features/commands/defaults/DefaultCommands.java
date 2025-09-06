/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.zov_corporation.implement.features.commands.defaults;

import ru.zov_corporation.core.Main;
import ru.zov_corporation.api.feature.command.ICommand;

import java.util.*;

public final class DefaultCommands {

    public static List<ICommand> createAll() {
        Main main = Main.getInstance();
        List<ICommand> commands = new ArrayList<>(Arrays.asList(
                new BoxESPCommand(main),
                new ConfigCommand(main),
                new MacroCommand(main),
                new HelpCommand(main),
                new BindCommand(main),
                new WayCommand(main),
                new RCTCommand(main),
                new FriendCommand(),
                new PrefixCommand(),
                new DebugCommand(),
                new KeyCommand(main)
        ));
        return Collections.unmodifiableList(commands);
    }
}
