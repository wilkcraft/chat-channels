package com.chatChannels.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.chatChannels.ChatChannels;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.kyori.adventure.text.Component;

public class ChannelManager {

  private static final HashMap<UUID, String> playerChannel = new HashMap<>();

  private static final Map<String, List<Component>> channelMessages = new HashMap<>();

  public static String getChannel(Player player) {
    return playerChannel.getOrDefault(player.getUniqueId(), "global");
  }

  public static void setChannel(Player player, String channel) {
    playerChannel.put(player.getUniqueId(), channel);
  }

  public static boolean channelExists(String channel) {
    FileConfiguration config = ChatChannels.getInstance().getConfig();
    return config.getConfigurationSection("channels") != null &&
        config.getConfigurationSection("channels").getKeys(false).contains(channel);
  }

  public static Set<String> getChannels() {
    FileConfiguration config = ChatChannels.getInstance().getConfig();
    return config.getConfigurationSection("channels").getKeys(false);
  }

  public static void addMessage(String channel, Component message) {
    channelMessages.computeIfAbsent(channel, k -> new ArrayList<>()).add(message);
  }

  public static List<Component> getMessages(String channel) {
    return channelMessages.getOrDefault(channel, new ArrayList<>());
  }
}
