package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ModCommands {
  public static void registerModCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
      dispatcher.register(MessageCommands.COMMAND_MESSAGE);
      dispatcher.register(MessageCommands.COMMAND_REPLY);
      dispatcher.register(MessageCommands.COMMAND_GROUP);
    });
  }
}
