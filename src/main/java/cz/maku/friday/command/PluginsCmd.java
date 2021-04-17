package cz.maku.friday.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.concurrent.atomic.AtomicInteger;

public class PluginsCmd extends Command {

    public PluginsCmd() {
        super("bplugins", "friday.plugins", "bpl");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer pp = (ProxiedPlayer) sender;
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        StringBuilder output = new StringBuilder("§fBungee Plugins & Modules (" + pm.getPlugins().size() + "): ");
        AtomicInteger i = new AtomicInteger();
        pm.getPlugins().forEach(plugin -> {
            i.getAndIncrement();
            if (i.get() == pm.getPlugins().size()) {
                output.append("§a").append(plugin.getDescription().getName()).append("§r");
                pp.sendMessage(output.toString());
            } else {
                output.append("§a").append(plugin.getDescription().getName()).append("§f, ");
            }
        });
    }
}
