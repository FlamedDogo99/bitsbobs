package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MessageCommands {
  private static final HashMap<UUID, UUID> replyMap = new HashMap<>();
  private static final HashMap<String, MessageGroup> nameGroupMap = new HashMap<>();
  private static final HashMap<UUID, MessageGroup> playerGroupMap = new HashMap<>();
  private static final String COMMAND_MESSAGE_INITIATOR = "m";
  private static final String COMMAND_REPLY_INITIATOR = "r";
  private static final String COMMAND_GROUP_INITIATOR = "g";

  public static LiteralArgumentBuilder<ServerCommandSource> COMMAND_MESSAGE =
    CommandManager.literal(COMMAND_MESSAGE_INITIATOR).then(
            CommandManager.argument("player", EntityArgumentType.player())
                    .suggests(new PlayerSuggestionProvider())
                    .then(
                            CommandManager.argument("message", MessageArgumentType.message())
                                    .executes(MessageCommands::messageCommand)

                    )
    );
  public static LiteralArgumentBuilder<ServerCommandSource> COMMAND_REPLY =
    CommandManager.literal(COMMAND_REPLY_INITIATOR).then(
            CommandManager.argument("message", MessageArgumentType.message())
                    .executes(MessageCommands::replyCommand)
    );
  public static LiteralArgumentBuilder<ServerCommandSource> COMMAND_GROUP =
          CommandManager.literal(COMMAND_GROUP_INITIATOR).then(
                  CommandManager.literal("create")
                          .then(CommandManager.argument("name", StringArgumentType.string())
                                  .then(CommandManager.argument("private", BoolArgumentType.bool())
                                          .suggests(new BooleanSuggestionProvider())
                                          .executes(MessageCommands::createGroupCommand)
                                  )

                          )
          ).then(
                  CommandManager.literal("join")
                          .then(CommandManager.argument("name", StringArgumentType.string())
                                  .suggests(new PublicGroupProvider(nameGroupMap.keySet()))
                                  .executes(MessageCommands::joinGroupCommand)
                          )
          ).then(
                  CommandManager.literal("leave")
                          .executes(MessageCommands::leaveGroupCommand)
          );
  private static int messageCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.message.invalid_source", source.getName()));
      return 0;
    }
    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
    Text message = MessageArgumentType.getMessage(context, "message");
    sendMessage(source.getPlayer(), target, message);
    return 1;
  }
  private static int replyCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.message.invalid_source", source.getName(), COMMAND_MESSAGE_INITIATOR));
      return 0;
    }
    UUID sourceUUID = source.getPlayer().getUuid();
    UUID targetUUID = replyMap.get(sourceUUID);
    ServerPlayerEntity target = source.getServer().getPlayerManager().getPlayer(targetUUID);
    if(target == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.reply.empty", source.getName()));
      return 0;
    }
    Text message = MessageArgumentType.getMessage(context, "message");
    sendMessage(source.getPlayer(), target, message);
    return 1;
  }
  private static int createGroupCommand(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName()));
      return 0;
    }
    boolean isPrivate = BoolArgumentType.getBool(context, "private");
    String name = StringArgumentType.getString(context, "name");
    if(nameGroupMap.containsKey(name)) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.duplicate_group", source.getName()));
      return 0;
    }

    UUID sourceUUID = source.getPlayer().getUuid();
    addGroup(name, isPrivate);
    joinGroup(sourceUUID, name);
    source.sendMessage(Text.translatable("bitsbobs.text.command.group.create", name));
    System.out.println("CREATE GROUP");
    System.out.println(playerGroupMap.toString());
    System.out.println(nameGroupMap.toString());
    System.out.println();

    return 1;
  }
  private static int joinGroupCommand(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName()));
      return 0;
    }
    String name = StringArgumentType.getString(context, "name");
    if(!nameGroupMap.containsKey(name)) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.unknown_group", source.getName()));
      return 0;
    }
    UUID sourceUUID = source.getPlayer().getUuid();
    if(Objects.equals(playerGroupMap.get(sourceUUID).getName(), name)) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.cannot_rejoin", source.getName()));
      return 0;
    }
    joinGroup(sourceUUID, name);
    source.sendMessage(Text.translatable("bitsbobs.text.command.group.joined", name));
    System.out.println("JOIN GROUP");
    System.out.println(playerGroupMap.toString());
    System.out.println(nameGroupMap.toString());
    System.out.println();
    return 1;
  }
  private static int leaveGroupCommand(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName()));
      return 0;
    }
    UUID sourceUUID = source.getPlayer().getUuid();
    leaveGroup(sourceUUID);
    source.sendMessage(Text.translatable("bitsbobs.text.command.group.left"));
    System.out.println("LEAVE GROUP");
    System.out.println(playerGroupMap.toString());
    System.out.println(nameGroupMap.toString());
    System.out.println();
    return 1;
  }
  private static void leaveGroup(UUID playerUUID) {
    MessageGroup group = playerGroupMap.get(playerUUID);
    if(group != null) {
      group.remove(playerUUID);
      if(group.size() == 0) removeGroup(group);
    }
  }
  private static void removeGroup(MessageGroup group) {
    for(UUID playerUUID : group.getMembers()) {
      playerGroupMap.remove(playerUUID);
    }
    nameGroupMap.remove(group.getName());
  }
  private static void addGroup(String name, boolean isPrivate) {
    MessageGroup group = new MessageGroup(name, isPrivate);
    nameGroupMap.put(name, group);
  }
  private static void joinGroup(UUID playerUUID, String name) {
    MessageGroup group = nameGroupMap.get(name);
    if(group != null) {
      leaveGroup(playerUUID);
      playerGroupMap.put(playerUUID, group);
    }
  }
  private static void sendMessage(ServerPlayerEntity source, ServerPlayerEntity target, Text message) {
    replyMap.put(target.getUuid(), source.getUuid());
    Text sourceHover = source.getDisplayName().copy().styled(style -> style.withHoverEvent(new HoverEvent.ShowText(
            Text.translatable("bitsbobs.text.command.message.hover", source.getName())
    )).withClickEvent(new ClickEvent.SuggestCommand(
            "/" + COMMAND_MESSAGE_INITIATOR + " " + source.getName().getString() + " "
    )));
    Text targetNoHover = target.getDisplayName().copy().setStyle(
            source.getDisplayName().getStyle().withHoverEvent(null).withClickEvent(null)
    );
    source.sendMessage(Text.translatable("bitsbobs.text.command.message.send", targetNoHover, message));
    target.sendMessage(Text.translatable("bitsbobs.text.command.message.receive", sourceHover, message));
  }
}
