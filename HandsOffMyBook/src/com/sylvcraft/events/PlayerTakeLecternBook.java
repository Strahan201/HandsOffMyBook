package com.sylvcraft.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sylvcraft.HandsOffMyBook;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;


public class PlayerTakeLecternBook implements Listener {
  HandsOffMyBook plugin;
  
  public PlayerTakeLecternBook(HandsOffMyBook instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent e) {
    if (!plugin.interdictedLecterns.contains(e.getLectern().getLocation())) return;
    if (e.getPlayer().hasPermission("homb.bypass")) return;
    
    plugin.msg("hands-off", e.getPlayer());
    e.setCancelled(true);
  }
}