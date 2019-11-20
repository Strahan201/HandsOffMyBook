package com.sylvcraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import com.sylvcraft.HandsOffMyBook;

public class homb implements TabExecutor {
  HandsOffMyBook plugin;
  
  public homb(HandsOffMyBook instance) {
    plugin = instance;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 1) return getMatchedAsType(args[0], new ArrayList<String>(Arrays.asList("add","del","list")));
    return null;
  }
  
  List<String> getMatchedAsType(String typed, List<String> values) {
    List<String> ret = new ArrayList<String>();
    for (String element : values) if (element.startsWith(typed)) ret.add(element);
    return ret;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    try {
      if (args.length == 0) {
        showHelp(sender);
        return true;
      }

      switch (args[0].toLowerCase()) {
      case "add":
        if (!sender.hasPermission("homb.add")) {
          plugin.msg("access-denied", sender); 
          return true;
        }
        
        markingLectern(sender, "add");
        break;
        
      case "del":
        if (!sender.hasPermission("homb.del")) {
          plugin.msg("access-denied", sender); 
          return true;
        }
        
        markingLectern(sender, "del");
        break;
        
      case "list":
        if (!sender.hasPermission("homb.list")) {
          plugin.msg("access-denied", sender); 
          return true;
        }
        
        plugin.enumerateLecterns(sender);
        break;
        
      default:
        showHelp(sender);
        break;
      }

      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  void markingLectern(CommandSender sender, String action) {
    if (!(sender instanceof Player)) {
      plugin.msg("player-only", sender);
      return;
    }
    
    Player p = (Player)sender;
    for (String key : new String[] {"add","del"}) {
      if (plugin.markingLectern.get(key).contains(p.getUniqueId())) {
        plugin.msg("already-marking", sender);
        return;
      }
    }
    
    plugin.markingLectern.get(action).add(p.getUniqueId());
    plugin.msg("marking", sender);
  }
  
  void showHelp(CommandSender sender) {
    int displayed = 0;
    if (sender.hasPermission("homb.add")) { plugin.msg("help-add", sender); displayed++; }
    if (sender.hasPermission("homb.del")) { plugin.msg("help-del", sender); displayed++; }
    if (sender.hasPermission("homb.list")) { plugin.msg("help-list", sender); displayed++; }
    if (displayed == 0) plugin.msg("access-denied", sender);
  }
}
