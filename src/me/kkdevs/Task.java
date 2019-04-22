package me.kkdevs;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.Player;
import cn.nukkit.Server;

public class Task extends PluginTask < EconomyJob > {

 public Task(EconomyJob owner) {
  super(owner);
 }

 @Override
 public void onRun(int arg0) {
  owner.getServer();
  for (Player player: Server.getInstance().getOnlinePlayers().values()) {
   owner.onBar(player);
  }
 }

}