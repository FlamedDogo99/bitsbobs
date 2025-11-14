package flameddogo99.bitsbobs.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class AccessibleGroupProvider implements SuggestionProvider<ServerCommandSource> {
  private final boolean showPublic;
  private final boolean showInvites;
  public AccessibleGroupProvider(boolean showPublic, boolean showInvites) {
    this.showInvites = showInvites;
    this.showPublic = showPublic;
  }
  @Override
  public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
    ServerPlayerEntity source = context.getSource().getPlayer();
    TreeSet<String> sortedResults = new TreeSet<>();
    if(showInvites && source != null) {
      Collection<String> invites = MessageGroupManager.getInvites(source);
      sortedResults.addAll(invites);
    }
    if(showPublic) sortedResults.addAll(MessageGroupManager.getPublicGroups());
    for (String name : sortedResults) {
      builder.suggest(name);
    }
    return builder.buildFuture();
  }
}
