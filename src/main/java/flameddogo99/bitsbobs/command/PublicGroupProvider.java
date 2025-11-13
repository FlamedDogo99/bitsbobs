package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class PublicGroupProvider implements SuggestionProvider<ServerCommandSource> {
  private final Collection<String> groups;
  public PublicGroupProvider(Collection<String> groups) {
    this.groups = groups;
  }
  @Override
  public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
    for (String group : groups) {
      builder.suggest(group);
    }
    return builder.buildFuture();
  }
}
