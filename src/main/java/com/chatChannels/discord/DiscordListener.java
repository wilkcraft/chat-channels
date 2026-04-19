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
      Component message = ColorUtils.color(
          "&5&lDiscord&r &b" + discordName + "&r&f: " + event.getMessage().getContentDisplay());

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
}
