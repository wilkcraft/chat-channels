package com.chatChannels.chat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.chatChannels.ChatChannels;
import com.chatChannels.discord.DiscordWebhook;

import io.papermc.paper.event.player.AsyncChatEvent;

public class ChatListener implements Listener {
  @EventHandler
  public void onChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String channel = ChannelManager.getChannel(player);

    FileConfiguration config = ChatChannels.getInstance().getConfig();

    String plainMessage = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
        .serialize(event.message());

    // ✅ GLOBAL = no cancela chat, pero sí manda a Discord
    if (channel.equalsIgnoreCase("global")) {

      String webhookUrl = config.getString("channels.global.webhook");

      try {
        if (webhookUrl != null && !webhookUrl.isEmpty()) {
          DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
          webhook.setContent(player.getName() + ": " + plainMessage);
          webhook.execute();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      return;
    }

    // ❗ resto de canales
    if (!ChannelManager.channelExists(channel)) {
      player.sendMessage("Invalid channel.");
      event.setCancelled(true);
      return;
    }

    String prefix = config.getString("channels." + channel + ".prefix");
    String webhookUrl = config.getString("channels." + channel + ".webhook");

    if (channel.equalsIgnoreCase("staff") && !player.isOp()) {
      player.sendMessage("You cannot use this channel.");
      event.setCancelled(true);
      return;
    }

    event.setCancelled(true);

    String message = prefix + " " + player.getName() + ": " + plainMessage;

    for (Player p : Bukkit.getOnlinePlayers()) {
      if (ChannelManager.getChannel(p).equals(channel)) {
        p.sendMessage(message);
      }
    }

    // Discord
    try {
      if (webhookUrl != null && !webhookUrl.isEmpty()) {
        DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
        webhook.setContent(player.getName() + ": " + plainMessage);
        webhook.execute();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
