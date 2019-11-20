package com.sylvcraft.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sylvcraft.HandsOffMyBook;

public class PlayerInteract implements Listener {
  HandsOffMyBook plugin;

  public PlayerInteract(HandsOffMyBook plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (!plugin.markingLectern.get("add").contains(e.getPlayer().getUniqueId()) &&
        !plugin.markingLectern.get("del").contains(e.getPlayer().getUniqueId())) return;

    String action = plugin.markingLectern.get("add").contains(e.getPlayer().getUniqueId())?"add":"del";
    if (e.getClickedBlock() == null || e.getClickedBlock().getType() != Material.LECTERN) {
      plugin.markingLectern.get(action).remove(e.getPlayer().getUniqueId());
      plugin.msg("marking-aborted", e.getPlayer());
      return;
    }

    
    plugin.msg(plugin.modifyLectern(e.getClickedBlock().getLocation(), action)?action:action + "-failed", e.getPlayer());
    plugin.markingLectern.get(action).remove(e.getPlayer().getUniqueId());
  }
}
