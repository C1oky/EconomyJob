package me.kkdevs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import me.onebone.economyapi.EconomyAPI;
import me.kkdevs.Task;

public class EconomyJob extends PluginBase implements Listener {

 private Config breaks;
 private Config places;
 private Config player;
 private EconomyAPI api;

 @Override
 public void onEnable() {

  CheckerAPI.checkAndRun(this);

  this.getServer().getPluginManager().registerEvents(this, this);
  if (!new File("plugins/EconomyJob/works").exists()) {
   new File("plugins/EconomyJob/works").mkdirs();
  }
  if (!new File("plugins/EconomyJob/works/break.yml").exists()) {
   InputStream breaks = this.getClass().getClassLoader().getResourceAsStream("me/kkdevs/works/break.yml");
   try {
    Utils.writeFile("plugins/EconomyJob/works/break.yml", breaks);
   } catch (IOException e) {
    throw new RuntimeException(e);
   }
  }
  if (!new File("plugins/EconomyJob/works/place.yml").exists()) {
   InputStream places = this.getClass().getClassLoader().getResourceAsStream("me/kkdevs/works/place.yml");
   try {
    Utils.writeFile("plugins/EconomyJob/works/place.yml", places);
   } catch (IOException e) {
    throw new RuntimeException(e);
   }
  }
  this.breaks = new Config("plugins/EconomyJob/works/break.yml", Config.YAML);
  this.places = new Config("plugins/EconomyJob/works/place.yml", Config.YAML);
  this.player = new Config("plugins/EconomyJob/players.yml", Config.YAML);
  this.api = EconomyAPI.getInstance();
 }

 @EventHandler
 public void onBreak(BlockBreakEvent e) {
  Player p = e.getPlayer();
  Block b = e.getBlock();
  boolean work = (boolean) this.player.get(p.getName());

  try {
   if (work == true) {
    int money = (int) this.breaks.get(b.getId() + ":" + b.getDamage());
    if (money > 0) {
     if (p.isSurvival()) {
      this.api.addMoney(p, money);
      p.sendPopup(TextFormat.GREEN + "+" + money);
     } else {}
    } else {
     this.api.reduceMoney(p, money);
    }
   }
  } catch (Exception ex) {}
 }

 @EventHandler
 public void onPlace(BlockPlaceEvent e) {
  Player p = e.getPlayer();
  Block b = e.getBlock();
  boolean work = (boolean) this.player.get(p.getName());

  try {
   if (work == true) {
    int money = (int) this.places.get(b.getId() + ":" + b.getDamage());
    if (money > 0) {
     if (p.isSurvival()) {
      this.api.addMoney(p, money);
      p.sendPopup(TextFormat.GREEN + "+" + money);
     } else {}
    } else {
     this.api.reduceMoney(p, money);
    }
   }
  } catch (Exception ex) {}
 }

 @EventHandler
 public void onJoin(PlayerJoinEvent e) {
  Player p = e.getPlayer();
  this.player.set(p.getName(), false);
 }

 @Override
 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
  if (command.getName().equals("job")) {
   try {
    if (!(sender instanceof Player)) {
     this.getServer().getLogger().error("Please run this command in-game.");
     return true;
    }

    if (args.length == 0) {
     sender.sendMessage(" §e-§a> §fBuild ànd mine to get paid. \n §e-§a>§f Getting stàrted: /job start");
    }

    if (args[0].equals("start")) {
     boolean work = (boolean) this.player.get(sender.getName());
     if (work == false) {
      sender.sendMessage(TextFormat.GREEN + "Stàrt work");
      this.player.set(sender.getName(), true);
      this.getServer().getScheduler().scheduleRepeatingTask(new Task(this), 25);
     } else {
      sender.sendMessage(" §e-§a> §fDî you work");
     }
    }

   } catch (Exception e) {}
   return true;
  }
  return true;
 }

 public void onBar(CommandSender sender) {
  try {
   boolean work = (boolean) this.player.get(sender.getName());

   if (work == true) {
    if (((Player) sender).isSurvival()) {
     ((Player) sender).sendPopup(" §e-§a> §fYou work §a<§e- \n §fmoney: §6" + this.api.myMoney((Player) sender));
    } else {
     ((Player) sender).sendPopup(" §e-§a> §fYou work §a<§e- \n §l§cGet to survival mode!");
    }
   }
  } catch (Exception ex) {}
 }

}