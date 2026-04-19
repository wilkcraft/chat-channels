package com.chatChannels;

import com.chatChannels.chat.ChatListener;
import com.chatChannels.commands.ChannelCommand;
import com.chatChannels.commands.ChannelTabCompleter;
import com.chatChannels.utils.VaultHook;

import org.bukkit.plugin.java.JavaPlugin;

public class ChatChannels extends JavaPlugin {

  private static ChatChannels instance;

  @Override
  public void onEnable() {
    instance = this;

    saveDefaultConfig();

    String token = getConfig().getString("discord.token");

    if (token != null && !token.isEmpty()) {
      com.chatChannels.discord.DiscordBot.startBot(this, token);
    } else {
      getLogger().info("Discord bot not enabled (no token provided).");
    }

    VaultHook.setup();
    getServer().getPluginManager().registerEvents(new ChatListener(), this);

    getCommand("channel").setExecutor(new ChannelCommand());
    getCommand("channel").setTabCompleter(new ChannelTabCompleter());

    getLogger().info("Chat-Channels enabled!");
  }

  public static ChatChannels getInstance() {
    return instance;
  }
}
