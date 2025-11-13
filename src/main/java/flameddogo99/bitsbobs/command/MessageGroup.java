package flameddogo99.bitsbobs.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class MessageGroup {
  private String name;
  private boolean isPrivate;
  private final Collection<UUID> members;
  private final Collection<UUID> invites;
  public MessageGroup(String name, boolean isPrivate) {
    this.name = name;
    this.isPrivate = isPrivate;
    members = new HashSet<>();
    invites = new HashSet<>();
  }
  public void add(UUID member) {
    members.add(member);
  }
  public void invite(UUID playerUUID) {
    invites.add(playerUUID);
  }
  public void remove(UUID member) {
    members.remove(member);
  }
  public int size() {
    return members.size();
  }
  public boolean hasMember(UUID playerUUID) {
    return members.contains(playerUUID);
  }
  public boolean hasInvite(UUID playerUUID) {
    return members.contains(playerUUID);
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return this.name;
  }
  public Collection<UUID> getMembers() {
    return this.members;
  }
  public Collection<UUID> getInvites() {
    return this.invites;
  }
  public void removeInvite(UUID playerUUID) {
    this.invites.remove(playerUUID);
  }
  public void setPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }
  public boolean getIsPrivate() {
    return this.isPrivate;
  }
}
