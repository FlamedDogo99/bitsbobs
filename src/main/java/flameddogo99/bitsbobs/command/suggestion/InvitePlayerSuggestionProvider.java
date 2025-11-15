package flameddogo99.bitsbobs.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import flameddogo99.bitsbobs.command.MessageGroupManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class InvitePlayerSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
  @Override
  public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    if(source.getPlayer() == null) return builder.buildFuture();
    for (UUID playerUUID : new TreeSet<>(MessageGroupManager.getGroup(source.getPlayer()).getInvites())) {
      ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerUUID);
      if(player != null) builder.suggest(player.getNameForScoreboard());
    }
    return builder.buildFuture();
  }
}
