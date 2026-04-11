package com.chatChannels.discord;

import com.chatChannels.ChatChannels;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {

  private static JDA jda;

  public static void startBot(ChatChannels plugin, String token) {
    try {
      plugin.getLogger().info("Starting Discord bot...");

      jda = JDABuilder.createLight(token,
          GatewayIntent.GUILD_MESSAGES,
          GatewayIntent.MESSAGE_CONTENT)
          .addEventListeners(new DiscordListener())
          .build()
          .awaitReady();

      plugin.getLogger().info("Discord bot connected!");

    } catch (Exception e) {
      plugin.getLogger().warning("Failed to start Discord bot.");
      e.printStackTrace();
    }
  }

  public static TextChannel getChannelById(String id) {
    if (jda == null)
      return null;
    return jda.getTextChannelById(id);
  }

  public static void shutdown() {
    if (jda != null) {
      jda.shutdownNow();
      jda = null;
    }
  }
}
