package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public class OtherPlayerSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
  @Override
  public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    TreeSet<String> playerNames = new TreeSet<>(source.getPlayerNames());
    playerNames.remove(source.getName());
    for (String playerName : playerNames) {
      builder.suggest(playerName);
    }

    return builder.buildFuture();
  }
}
