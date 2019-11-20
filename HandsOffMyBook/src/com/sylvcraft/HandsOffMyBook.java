package com.sylvcraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import com.sylvcraft.events.PlayerInteract;
import com.sylvcraft.events.PlayerTakeLecternBook;

import com.sylvcraft.commands.homb;

public class HandsOffMyBook extends JavaPlugin {
  public List<Location> interdictedLecterns = new ArrayList<Location>();
  public Map<String, List<UUID>> markingLectern = new HashMap<String, List<UUID>>();
  
  @Override
  @SuppressWarnings("unchecked")
  public void onEnable() {
    saveDefaultConfig();
    markingLectern.put("add", new ArrayList<UUID>());
    markingLectern.put("del", new ArrayList<UUID>());
    interdictedLecterns = (List<Location>)getConfig().get("lecterns");
    if (interdictedLecterns == null) interdictedLecterns = new ArrayList<Location>(); 
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new PlayerTakeLecternBook(this), this);
    pm.registerEvents(new PlayerInteract(this), this);
    getCommand("homb").setExecutor(new homb(this));
  }

  String tokenizeLocation(Location loc) {
    String target = loc.getWorld().getName().toLowerCase() + "_";
    target += loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    return target;
  }
  
  public boolean modifyLectern(Location loc, String action) {
    if (action.equalsIgnoreCase("add")) {
      if (interdictedLecterns.contains(loc)) return false;
      interdictedLecterns.add(loc);
    } else {
      if (!interdictedLecterns.contains(loc)) return false;
      interdictedLecterns.remove(loc);
    }
    getConfig().set("lecterns", interdictedLecterns);
    saveConfig();
    return true;
  }
  
  public void enumerateLecterns(CommandSender sender) {
    Map<String, String> data = new HashMap<String, String>();
    if (interdictedLecterns.size() == 0) {
      msg("list-none", sender);
      return;
    }
    
    msg("list-header", sender);
    for (Location loc : interdictedLecterns) {
      data.put("%location%", loc.getWorld().getName() + " @ " + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ());
      msg("list-data", sender, data);
    }
    msg("list-footer", sender);
  }
  
  public void msg(String msgCode, CommandSender sender) {
    if (getConfig().getString("messages." + msgCode) == null) return;
    msgTransmit(getConfig().getString("messages." + msgCode), sender);
  }

  public void msg(String msgCode, CommandSender sender, Map<String, String> data) {
    if (getConfig().getString("messages." + msgCode) == null) return;
    String tmp = getConfig().getString("messages." + msgCode, msgCode);
    for (Map.Entry<String, String> mapData : data.entrySet()) {
      tmp = tmp.replace(mapData.getKey(), mapData.getValue());
    }
    msgTransmit(tmp, sender);
  }
  
  public void msgTransmit(String msg, CommandSender sender) {
    for (String m : (msg + " ").split("%br%")) {
      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
    }
  }
}