package com.chatChannels.commands;

import com.chatChannels.ChatChannels;
import com.chatChannels.chat.ChannelManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Set;

public class ChannelCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

      if (!(sender instanceof Player) || sender.isOp()) {

        ChatChannels plugin = ChatChannels.getInstance();

        // 📌 Get current token BEFORE reloading config
        String oldToken = plugin.getConfig().getString("discord.token");

        // 🔁 Reload configuration file
        plugin.reloadConfig();

        // 📌 Get new token AFTER reload
        String newToken = plugin.getConfig().getString("discord.token");

        // 🔍 Check if token has changed
        boolean tokenChanged = (oldToken == null && newToken != null) ||
            (oldToken != null && !oldToken.equals(newToken));

        if (tokenChanged) {

          // 🔴 Shutdown current Discord bot (if running)
          com.chatChannels.discord.DiscordBot.shutdown();

          // 🔁 Start bot again only if new token exists
          if (newToken != null && !newToken.isEmpty()) {
            com.chatChannels.discord.DiscordBot.startBot(plugin, newToken);
            sender.sendMessage("§aConfig and Discord bot reloaded.");
          } else {
            sender.sendMessage("§eConfig reloaded (Discord bot disabled).");
          }

        } else {
          // ✅ Token unchanged → no need to restart bot
          sender.sendMessage("§aConfig reloaded successfully.");
        }

      } else {
        sender.sendMessage("You don't have permission.");
      }

      return true;
    }

    if (!(sender instanceof Player player))
      return true;

    if (args.length == 0) {
      Set<String> channels = ChannelManager.getChannels();
      player.sendMessage("Available channels: " + String.join(", ", channels));
      return true;
    }

    String channel = args[0].toLowerCase();

    if (!ChannelManager.channelExists(channel)) {
      player.sendMessage("That channel does not exist.");
      return true;
    }

    if (channel.equals("staff") && !player.isOp()) {
      player.sendMessage("You don't have permission.");
      return true;
    }

    String currentChannel = ChannelManager.getChannel(player);

    if (currentChannel.equalsIgnoreCase(channel)) {
      player.sendMessage("§eYou are already in this channel.");
      return true;
    }

    ChannelManager.setChannel(player, channel);
    clearChat(player);
    player.sendMessage("§aYou are now in channel: §e" + channel + "§r");

    player.sendMessage("§7--- Chat history ---");

    for (net.kyori.adventure.text.Component msg : ChannelManager.getMessages(channel)) {
      player.sendMessage(msg);
    }

    return true;
  }

  public static void clearChat(Player player) {
    for (int i = 0; i < 100; i++) {
      player.sendMessage("");
    }
  }
}
