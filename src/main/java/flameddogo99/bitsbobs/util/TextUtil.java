package flameddogo99.bitsbobs.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

public class TextUtil {
  public static Text getFormattedName(ServerPlayerEntity target) {
    return getFormattedName(target, null, null);
  }
  public static Text getFormattedName(ServerPlayerEntity target, HoverEvent hoverEvent) {
    return getFormattedName(target, hoverEvent, null);
  }
  public static Text getFormattedName(ServerPlayerEntity target, ClickEvent clickEvent) {
    return getFormattedName(target, null, clickEvent);
  }
  public static Text getFormattedName(ServerPlayerEntity target, HoverEvent hoverEvent, ClickEvent clickEvent) {
    Text displayName = target.getDisplayName();
    if(displayName == null) {
      return target.getName();
    } else {
      return displayName.copy().setStyle(displayName.getStyle().withHoverEvent(hoverEvent).withClickEvent(clickEvent));
    }
  }
}
