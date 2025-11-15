package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import flameddogo99.bitsbobs.command.suggestion.*;
import flameddogo99.bitsbobs.util.TextUtil;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class MessageCommands {

  private static final String COMMAND_MESSAGE_INITIATOR = "message";
  private static final String COMMAND_REPLY_INITIATOR = "reply";
  private static final String COMMAND_GROUP_INITIATOR = "group";
  private static final String COMMAND_MESSAGE_GROUP_INITIATOR = "message-group";

  private static final HashMap<UUID, UUID> replyMap = new HashMap<>();


  public static LiteralArgumentBuilder<ServerCommandSource> COMMAND_MESSAGE =
    CommandManager.literal(COMMAND_MESSAGE_INITIATOR).then(
            CommandManager.argument("player", EntityArgumentType.player())
                    .suggests(new OtherPlayerSuggestionProvider())
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
                                  .suggests(new AccessibleGroupProvider(true, true))
                                  .executes(MessageCommands::joinGroupCommand)
                          )
          ).then(
                  CommandManager.literal("leave")
                          .executes(MessageCommands::leaveGroupCommand)
          ).then(
                  CommandManager.literal("invite")
                          .then(CommandManager.literal("add")
                                  .then(CommandManager.argument("player", EntityArgumentType.player())
                                          .suggests(new OutsideGroupSuggestionProvider())
                                          .executes(MessageCommands::inviteGroupCommand)
                                  )
                          )
                          .then(CommandManager.literal("remove")
                                  .then(CommandManager.argument("player", EntityArgumentType.player())
                                          .suggests(new InvitePlayerSuggestionProvider())
                                          .executes(MessageCommands::removeInviteCommand)
                                  )
                          )

          );
  public static LiteralArgumentBuilder<ServerCommandSource> COMMAND_MESSAGE_GROUP =
          CommandManager.literal(COMMAND_MESSAGE_GROUP_INITIATOR).then(
                  CommandManager.argument("message", MessageArgumentType.message())
                          .executes(MessageCommands::messageGroupCommand)
          );
  public static void sendMessage(ServerPlayerEntity source, ServerPlayerEntity target, Text message) {
    replyMap.put(target.getUuid(), source.getUuid());
    Text sourceName = TextUtil.getFormattedName(
            source,
            new HoverEvent.ShowText(Text.translatable("bitsbobs.text.command.message.hover", source.getName())),
            new ClickEvent.SuggestCommand("/" + COMMAND_MESSAGE_INITIATOR + " " + source.getName().getString() + " ")
    );
    source.sendMessage(Text.translatable("bitsbobs.text.command.message.send", TextUtil.getFormattedName(target), message));
    target.sendMessage(Text.translatable("bitsbobs.text.command.message.receive", sourceName, message));
  }
  public static void sendGroupMessage(PlayerManager playerManager, Text message, MessageGroup group) {
    for(UUID targetUUID : group.getMembers()) {
      ServerPlayerEntity target = playerManager.getPlayer(targetUUID);
      if(target == null) {
        MessageGroupManager.leaveGroup(targetUUID);
        continue;
      }
      target.sendMessage(message);
    }
  }
  public static void sendInviteMessage(ServerPlayerEntity target, MessageGroup group) {
    String commandString = "/" + COMMAND_GROUP_INITIATOR + " " + "join" + " " + group.getName();
    Text command = Text.literal(commandString)
            .styled(style -> style
                    .withColor(Formatting.YELLOW)
                    .withClickEvent(new ClickEvent.SuggestCommand(commandString))
            );
    target.sendMessage(Text.translatable("bitsbobs.text.command.group.invite", group.getName(), command));
  }

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
    if(MessageGroupManager.getGroup(name) != null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.duplicate_group", source.getName()));
      return 0;
    }
    MessageGroup sourceGroup = MessageGroupManager.getGroup(source.getPlayer());
    if(sourceGroup != null) {
      Text groupMessage = Text.translatable("bitsbobs.text.command.group.player_left", sourceGroup.getName(), TextUtil.getFormattedName(source.getPlayer()));
      sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, sourceGroup);
    }

    MessageGroupManager.addGroup(name, isPrivate);
    MessageGroupManager.joinGroup(source.getPlayer(), name);
    source.sendMessage(Text.translatable("bitsbobs.text.command.group.create", name));

    return 1;
  }
  private static int joinGroupCommand(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName()));
      return 0;
    }
    String name = StringArgumentType.getString(context, "name");
    MessageGroup targetGroup = MessageGroupManager.getGroup(name);
    if(targetGroup == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.unknown_group", name));
      return 0;
    }
    if(targetGroup.getIsPrivate() && !targetGroup.hasInvite(source.getPlayer().getUuid())) {
      String commandString = "/" + COMMAND_GROUP_INITIATOR + " invite add " + source.getPlayer().getNameForScoreboard();
      Text command = Text.literal(commandString)
              .styled(style -> style
                      .withColor(Formatting.YELLOW)
                      .withClickEvent(new ClickEvent.SuggestCommand(commandString))
              );
      Text groupMessage = Text.translatable("bitsbobs.text.command.group.request", targetGroup.getName(), TextUtil.getFormattedName(source.getPlayer()), command);
      sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, targetGroup);
      source.sendError(Text.translatable("bitsbobs.text.command.group.unknown_group", targetGroup.getName()));
      return 0;
    }
    MessageGroup sourceGroup = MessageGroupManager.getGroup(source.getPlayer());
    if(sourceGroup != null && Objects.equals(sourceGroup.getName(), name)) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.cannot_rejoin", source.getName()));
      return 0;
    }
    if(sourceGroup != null) {
      Text groupMessage = Text.translatable("bitsbobs.text.command.group.player_left", sourceGroup.getName(), TextUtil.getFormattedName(source.getPlayer()));
      sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, sourceGroup);
    }

    Text groupMessage = Text.translatable("bitsbobs.text.command.group.player_joined", targetGroup.getName(), TextUtil.getFormattedName(source.getPlayer()));
    sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, targetGroup);

    MessageGroupManager.joinGroup(source.getPlayer(), name);
    source.sendMessage(Text.translatable("bitsbobs.text.command.group.joined", name));
    return 1;
  }
  private static int leaveGroupCommand(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName()));
      return 0;
    }
    MessageGroup group = MessageGroupManager.getGroup(source.getPlayer());
    if(group == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_leave"));
      return 0;
    }
    Text groupMessage = Text.translatable("bitsbobs.text.command.group.player_left", group.getName(), TextUtil.getFormattedName(source.getPlayer()));
    sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, group);

    MessageGroupManager.leaveGroup(source.getPlayer());
    return 1;
  }


  private static int messageGroupCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.message_group.invalid_source", source.getName(), COMMAND_MESSAGE_GROUP_INITIATOR));
      return 0;
    }
    MessageGroup group = MessageGroupManager.getGroup(source.getPlayer());
    if(group == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.message_group.no_group"));
      return 0;
    }
    Text message = MessageArgumentType.getMessage(context, "message");
    Text groupMessage = Text.translatable("bitsbobs.text.command.message_group.message", group.getName(), TextUtil.getFormattedName(source.getPlayer()), message);
    sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, group);
    return 1;
  }

  private static int inviteGroupCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName(), COMMAND_MESSAGE_GROUP_INITIATOR));
      return 0;
    }
    MessageGroup group = MessageGroupManager.getGroup(source.getPlayer());
    if(group == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.no_group"));
      return 0;
    }
    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
    if(group.hasMember(target.getUuid())) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.duplicate_player"));
      return 0;
    }
    Text groupMessage = Text.translatable("bitsbobs.text.command.group.player_invited", group.getName(), TextUtil.getFormattedName(source.getPlayer()),TextUtil.getFormattedName(target));
    sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, group);

    MessageGroupManager.addInvite(target.getUuid(), group);
    sendInviteMessage(target, group);
    return 1;
  }
  private static int removeInviteCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.invalid_source", source.getName(), COMMAND_MESSAGE_GROUP_INITIATOR));
      return 0;
    }
    MessageGroup group = MessageGroupManager.getGroup(source.getPlayer());
    if(group == null) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.no_group"));
      return 0;
    }
    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
    if(!group.hasInvite(target.getUuid())) {
      source.sendError(Text.translatable("bitsbobs.text.command.group.no_uninvite", TextUtil.getFormattedName(target)));
      return 0;
    }
    Text groupMessage = Text.translatable("bitsbobs.text.command.group.remove_invite", group.getName(), TextUtil.getFormattedName(source.getPlayer()), TextUtil.getFormattedName(target));
    sendGroupMessage(source.getServer().getPlayerManager(), groupMessage, group);

    MessageGroupManager.removeInvite(target, group);
    return 1;
  }
}
