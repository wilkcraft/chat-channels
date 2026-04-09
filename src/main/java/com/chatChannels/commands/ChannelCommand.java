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
        ChatChannels.getInstance().reloadConfig();
        sender.sendMessage("§aConfig reloaded successfully.");
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
    player.sendMessage("§aYou are now in channel: §e" + channel + "§r");

    return true;
  }
}
