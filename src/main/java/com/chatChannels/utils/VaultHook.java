package com.chatChannels.utils;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

  private static Chat chat = null;
  private static boolean enabled = false;

  public static void setup() {

    if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
      Bukkit.getLogger().info("[Chat-Channels] Vault not found, prefixes disabled.");
      return;
    }

    RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);

    if (chatProvider != null) {
      chat = chatProvider.getProvider();
      enabled = true;
      Bukkit.getLogger().info("[Chat-Channels] Vault hooked successfully!");
    } else {
      Bukkit.getLogger().warning("[Chat-Channels] Vault found but no chat provider.");
    }
  }

  public static String getPrefix(Player player) {
    if (!enabled || chat == null)
      return "";
    return chat.getPlayerPrefix(player);
  }

  public static String getSuffix(Player player) {
    if (!enabled || chat == null)
      return "";
    return chat.getPlayerSuffix(player);
  }

  public static boolean isEnabled() {
    return enabled;
  }
}
