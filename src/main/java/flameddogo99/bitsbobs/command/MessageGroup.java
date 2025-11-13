package flameddogo99.bitsbobs.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class MessageGroup {
  private String name;
  private boolean isPrivate;
  private final Collection<UUID> members;
  public MessageGroup(String name, boolean isPrivate) {
    this.name = name;
    this.isPrivate = isPrivate;
    members = new HashSet<>();
  }
  public void add(UUID member) {
    members.add(member);
  }
  public void remove(UUID member) {
    members.remove(member);
  }
  public int size() {
    return members.size();
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
  public void setPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }
}
