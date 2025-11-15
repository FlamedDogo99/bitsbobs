package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;


public class ModCommands {
  public static void registerModCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
      LiteralCommandNode<ServerCommandSource> message = dispatcher.register(MessageCommands.COMMAND_MESSAGE);
      dispatcher.register(CommandManager.literal("msg").redirect(message));
      dispatcher.register(CommandManager.literal("m").redirect(message));
      LiteralCommandNode<ServerCommandSource> reply = dispatcher.register(MessageCommands.COMMAND_REPLY);
      dispatcher.register(CommandManager.literal("r").redirect(reply));
      LiteralCommandNode<ServerCommandSource> group = dispatcher.register(MessageCommands.COMMAND_GROUP);
      dispatcher.register(CommandManager.literal("g").redirect(group));
      LiteralCommandNode<ServerCommandSource> message_group = dispatcher.register(MessageCommands.COMMAND_MESSAGE_GROUP);
      dispatcher.register(CommandManager.literal("mg").redirect(message_group));
    });
  }
}
