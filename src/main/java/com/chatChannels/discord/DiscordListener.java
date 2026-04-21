package com.chatChannels.discord;

import com.chatChannels.ChatChannels;
import com.chatChannels.chat.ChannelManager;
import com.chatChannels.utils.ColorUtils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import net.kyori.adventure.text.Component;

public class DiscordListener extends ListenerAdapter {

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {

    // Ignore bots
    if (event.getAuthor().isBot())
      return;

    // Ignore empty messages (no text content)
    if (event.getMessage().getContentDisplay().isEmpty())
      return;

    FileConfiguration config = ChatChannels.getInstance().getConfig();

    String discordChannelId = event.getChannel().getId();

    // Loop all channels in config
    for (String channel : ChannelManager.getChannels()) {

      String configId = config.getString("channels." + channel + ".discord-channel-id");

      if (configId == null || configId.isEmpty())
        continue;

      if (!configId.equals(discordChannelId))
        continue;

      String discordName = event.getMember() != null
          ? event.getMember().getEffectiveName()
          : event.getAuthor().getName();

      // Format message (Discord -> Minecraft)
      // Default format (no role)
      String formatted = "&b" + discordName + "&r&f: " + event.getMessage().getContentDisplay();

      if (event.getMember() != null) {
        var roles = event.getMember().getRoles();

        var bestRole = roles.stream()
            .filter(role -> !role.isPublicRole()) // ignore @everyone
            .filter(role -> role.getColor() != null) // only roles with color
            .max((r1, r2) -> Integer.compare(r1.getPosition(), r2.getPosition())) // highest role
            .orElse(null);

        if (bestRole != null) {

          String roleName = bestRole.getName().toUpperCase();

          if (roleName.length() > 3) {
            roleName = roleName.substring(0, 3);
          }

          String roleColor = getMinecraftColor(
              bestRole.getColor().getRed(),
              bestRole.getColor().getGreen(),
              bestRole.getColor().getBlue());

          formatted = roleColor + "&l" + roleName + "&r &b" + discordName + "&r&f: "
              + event.getMessage().getContentDisplay();
        }
      }

      Component message = ColorUtils.color(formatted);

      // Send to Minecraft players in that channel
      Bukkit.getScheduler().runTask(ChatChannels.getInstance(), () -> {
        Bukkit.getOnlinePlayers().forEach(player -> {
          if (ChannelManager.getChannel(player).equals(channel)) {
            player.sendMessage(message);
          }
        });
      });
    }
  }

  private String getMinecraftColor(int r, int g, int b) {

    // Minecraft color palette
    int[][] colors = {
        { 0, 0, 0 }, // &0 black
        { 0, 0, 170 }, // &1 dark blue
        { 0, 170, 0 }, // &2 dark green
        { 0, 170, 170 }, // &3 dark aqua
        { 170, 0, 0 }, // &4 dark red
        { 170, 0, 170 }, // &5 dark purple
        { 255, 170, 0 }, // &6 gold
        { 170, 170, 170 }, // &7 gray
        { 85, 85, 85 }, // &8 dark gray
        { 85, 85, 255 }, // &9 blue
        { 85, 255, 85 }, // &a green
        { 85, 255, 255 }, // &b aqua
        { 255, 85, 85 }, // &c red
        { 255, 85, 255 }, // &d light purple
        { 255, 255, 85 }, // &e yellow
        { 255, 255, 255 } // &f white
    };

    String[] codes = {
        "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7",
        "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f"
    };

    double closestDistance = Double.MAX_VALUE;
    int closestIndex = 7; // default gray

    for (int i = 0; i < colors.length; i++) {
      int cr = colors[i][0];
      int cg = colors[i][1];
      int cb = colors[i][2];

      double distance = Math.pow(r - cr, 2) +
          Math.pow(g - cg, 2) +
          Math.pow(b - cb, 2);

      if (distance < closestDistance) {
        closestDistance = distance;
        closestIndex = i;
      }
    }

    return codes[closestIndex];
  }
}
