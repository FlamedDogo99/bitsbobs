package flameddogo99.bitsbobs.command;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MessageGroupManager {
  private static final HashMap<String, MessageGroup> nameToGroupMap = new HashMap<>();
  private static final HashMap<UUID, String> playerToNameMap = new HashMap<>();
  private static final HashMap<UUID, Collection<String>> playerToInvitesMap = new HashMap<>();

  public static MessageGroup getGroup(ServerPlayerEntity source) {
    String groupName = playerToNameMap.get(source.getUuid());
    return nameToGroupMap.get(groupName);
  }
  public static MessageGroup getGroup(String name) {
    return nameToGroupMap.get(name);
  }
  public static Collection<String> getInvites(ServerPlayerEntity source) {
    return playerToInvitesMap.getOrDefault(source.getUuid(), new HashSet<>());
  }
  public static Collection<String> getGroups() {
    return nameToGroupMap.keySet();
  }

  public static void addInvite(UUID playerUUID, MessageGroup group) {
    Collection<String> invites = playerToInvitesMap.getOrDefault(playerUUID, new HashSet<>());
    invites.add(group.getName());
    playerToInvitesMap.put(playerUUID, invites);
    group.invite(playerUUID);
  }
  public static void removeInvite(ServerPlayerEntity source, MessageGroup group) {
    Collection<String> invites = playerToInvitesMap.get(source.getUuid());
    if(invites != null) {
      invites.remove(group.getName());
    }
    group.removeInvite(source.getUuid());
  }

  public static void leaveGroup(ServerPlayerEntity player) {
    leaveGroup(player.getUuid());
  }
  public static void leaveGroup(UUID playerUUID) {
    String groupName = playerToNameMap.get(playerUUID);
    MessageGroup group = nameToGroupMap.get(groupName);
    if(group != null) {
      group.remove(playerUUID);
      if(group.size() == 0) removeGroup(group);
    }
  }
  public static void removeGroup(MessageGroup group) {
    for(UUID playerUUID : group.getMembers()) {
      playerToNameMap.remove(playerUUID);
    }
    for(UUID playerUUID : group.getInvites()) {
      playerToInvitesMap.remove(playerUUID);
      group.removeInvite(playerUUID);
    }
    nameToGroupMap.remove(group.getName());
  }
  public static void addGroup(String name, boolean isPrivate) {
    MessageGroup group = new MessageGroup(name, isPrivate);
    nameToGroupMap.put(name, group);
  }
  public static void joinGroup(ServerPlayerEntity player, String name) {
    MessageGroup group = nameToGroupMap.get(name);
    if(group != null) {
      leaveGroup(player);
      removeInvite(player, group);
      playerToNameMap.put(player.getUuid(), group.getName());
    }
  }

}
