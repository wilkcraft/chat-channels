package com.chatChannels.chat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.chatChannels.ChatChannels;
import com.chatChannels.discord.DiscordWebhook;
import com.chatChannels.utils.ColorUtils;
import com.chatChannels.utils.VaultHook;

import net.kyori.adventure.text.Component;

import io.papermc.paper.event.player.AsyncChatEvent;

public class ChatListener implements Listener {
  private String stripColors(String text) {
    if (text == null)
      return "";

    return text
        .replaceAll("(?i)&?#([0-9A-F]{6})", "") // &#FFFFFF o #FFFFFF
        .replaceAll("§x(§[0-9A-Fa-f]){6}", "") // formato interno MC
        .replaceAll("§.", "") // colores §
        .replaceAll("&[0-9A-FK-ORa-fk-or]", ""); // colores &
  }

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
          String prefix = VaultHook.getPrefix(player);
          String suffix = VaultHook.getSuffix(player);

          // ❌ Remove Minecraft colors for Discord
          String cleanPrefix = stripColors(prefix);
          String cleanSuffix = stripColors(suffix);

          String discordMessage = "**" + cleanPrefix + player.getName() + cleanSuffix + "**: " + plainMessage;

          webhook.setContent(discordMessage);
          webhook.setUsername(player.getName());
          webhook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
          webhook.execute();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      String rawPrefix = VaultHook.getPrefix(player);
      String rawSuffix = VaultHook.getSuffix(player);

      Component message = ColorUtils.color(
          rawPrefix + player.getName() + rawSuffix + "&f: " + plainMessage);

      ChannelManager.addMessage(channel, message);

      return;
    }

    // ❗ Other channels
    if (!ChannelManager.channelExists(channel)) {
      player.sendMessage("Invalid channel.");
      event.setCancelled(true);
      return;
    }

    String webhookUrl = config.getString("channels." + channel + ".webhook");

    if (channel.equalsIgnoreCase("staff") && !player.isOp()) {
      player.sendMessage("You cannot use this channel.");
      event.setCancelled(true);
      return;
    }

    event.setCancelled(true);

    // 🔥 BASE (SIN tocar)
    String rawPrefix = VaultHook.getPrefix(player);
    String rawSuffix = VaultHook.getSuffix(player);

    // 🧹 Clean for Discord (no color)
    String cleanPrefix = stripColors(rawPrefix);
    String cleanSuffix = stripColors(rawSuffix);

    // 🧹 For Minecraft (with color)
    Component message = ColorUtils.color(
        rawPrefix + player.getName() + rawSuffix + "&f: " + plainMessage);

    ChannelManager.addMessage(channel, message);

    for (Player p : Bukkit.getOnlinePlayers()) {
      if (ChannelManager.getChannel(p).equals(channel)) {
        p.sendMessage(message);
      }
    }

    // Discord
    try {
      if (webhookUrl != null && !webhookUrl.isEmpty()) {
        DiscordWebhook webhook = new DiscordWebhook(webhookUrl);

        String discordMessage = "**" + cleanPrefix + player.getName() + cleanSuffix + "**: " + plainMessage;

        webhook.setContent(discordMessage);
        webhook.setUsername(player.getName());
        webhook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        webhook.execute();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
