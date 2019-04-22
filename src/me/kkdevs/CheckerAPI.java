package me.kkdevs;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;

public class CheckerAPI {

 public static void checkAndRun(Plugin plugin) {
  Server server = plugin.getServer();

  if (server.getPluginManager().getPlugin("EconomyAPI") != null) return;

  plugin.getLogger().error("EconomyAPI not found!");

  plugin.getLogger().info("Downloading EconomyAPI...");

  String EconomyAPI = server.getFilePath() + "/plugins/EconomyAPI.jar";

  try {
   FileOutputStream fos = new FileOutputStream(EconomyAPI);
   fos.getChannel().transferFrom(Channels.newChannel(new URL("https://drive.google.com/uc?authuser=0&id=1x2INkZfLCs4teac882kEPEqhIOMDsMrv&export=download").openStream()), 0, Long.MAX_VALUE);
   fos.close();
  } catch (Exception e) {
   server.getLogger().logException(e);
   server.getPluginManager().disablePlugin(plugin);
   return;
  }

  plugin.getLogger().info("EconomyAPI downloaded successfully!");
  server.getPluginManager().loadPlugin(EconomyAPI);
  server.getPluginManager().enablePlugin(server.getPluginManager().getPlugin("EconomyAPI"));
 }
}
