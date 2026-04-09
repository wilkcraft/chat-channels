package com.chatChannels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.chatChannels.chat.ChannelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChannelTabCompleter implements TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    List<String> completions = new ArrayList<>();

    if (!(sender instanceof Player player))
      return completions;

    if (args.length == 1) {

      // 🔁 Reload only for OP
      if (player.isOp() && "reload".startsWith(args[0].toLowerCase())) {
        completions.add("reload");
      }

      Set<String> channels = ChannelManager.getChannels();

      for (String channel : channels) {

        if (channel.equalsIgnoreCase("staff") && !player.isOp()) {
          continue;
        }

        if (channel.toLowerCase().startsWith(args[0].toLowerCase())) {
          completions.add(channel);
        }
      }
    }

    return completions;
  }
}
