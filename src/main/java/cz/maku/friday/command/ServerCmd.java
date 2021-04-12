package cz.maku.friday.command;

import com.google.common.collect.Lists;
import cz.maku.friday.Friday;
import cz.maku.friday.server.Server;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.net.InetSocketAddress;
import java.util.List;

public class ServerCmd extends Command implements TabExecutor {

    public ServerCmd() {
        super("server");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(sender.getName());
            if (args.length < 1) {
                ServerInfo s = pp.getServer().getInfo();
                pp.sendMessage("§8[§9Server§8] §7You are on §3" + s.getName() + " §7with §b" + s.getPlayers().size() + " §7online players.");
                return;
            }
            if (args[0].equalsIgnoreCase("test")) {
                if (pp.hasPermission("friday.server.create")) {
                    if (args.length < 2) {
                        pp.sendMessage("§cInvalid server name.");
                        return;
                    }
                    String serverName = args[1];
                    if (serverName.equalsIgnoreCase("Basic")) {
                        Server server = new Server("Basic", 20, false, new InetSocketAddress(25588), "basic");
                        Friday.getInstance().getServerManager().addServer(server, false);
                    } else if (serverName.equalsIgnoreCase("Dev")) {
                        Server server = new Server("Dev", 5, true, new InetSocketAddress(25599), "dev");
                        Friday.getInstance().getServerManager().addServer(server, false);
                    } else {
                        pp.sendMessage("§cInvalid server name. Provide 'Basic' or 'Dev'.");
                    }
                } else {
                    pp.sendMessage("§cYou're not allowed to do this.");
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (pp.hasPermission("friday.server.create")) {
                    if (args.length < 2) {
                        pp.sendMessage("§cInvalid server name.");
                        pp.sendMessage("§c/server create <name> <port> <defaultMotd>");
                        return;
                    }
                    if (args.length < 3) {
                        pp.sendMessage("§cInvalid port.");
                        pp.sendMessage("§c/server create <name> <port> <defaultMotd>");
                        return;
                    }
                    if (args.length < 4) {
                        pp.sendMessage("§cInvalid motd.");
                        pp.sendMessage("§c/server create <name> <port> <defaultMotd>");
                        return;
                    }
                    if (!args[2].matches("\\d+")) {
                        pp.sendMessage("§cInvalid port. Must be number!");
                        pp.sendMessage("§c/server create <name> <port> <defaultMotd>");
                        return;
                    }
                    String serverName = args[1];
                    int port = Integer.parseInt(args[2]);
                    String motd = args[3];
                    Friday.getInstance().getServerManager().addServer(serverName, new InetSocketAddress(port), motd, false);
                    pp.sendMessage("§aServer '" + serverName + "' has been created. You can show informations about this server with '/server info " + serverName + "'.");
                } else {
                    pp.sendMessage("§cYou're not allowed to do this.");
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (pp.hasPermission("friday.server.list")) {
                    pp.sendMessage("§8[§9Server§8] §7Players: §3" + ProxyServer.getInstance().getPlayers().size());
                    pp.sendMessage("§8[§9Server§8] §7Servers:");
                    ProxyServer.getInstance().getServers().forEach((name, info) -> {
                        String players = "";
                        for (ProxiedPlayer p : info.getPlayers()) {
                            players += p.getName() + ", ";
                        }
                        pp.sendMessage(" §3" + name + " §8- §7Players: §b" + info.getPlayers().size());
                        pp.sendMessage("    §7Players list: §b" + players);
                        pp.sendMessage("");
                    });
                } else {
                    pp.sendMessage("§cYou're not allowed to do this.");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                if (args.length < 2) {
                    pp.sendMessage("§cPlease enter server name.");
                    return;
                }
                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(args[1]);
                if (serverInfo == null) {
                    pp.sendMessage("§cServer " + args[1] + " doesn't exist.");
                    return;
                }
                pp.sendMessage("§8■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■");
                pp.sendMessage("§8■ §7Name: §3" + serverInfo.getName());
                pp.sendMessage("§8■ §7Online players: §3" + serverInfo.getPlayers().size());
                pp.sendMessage("§8■ §7Address: §3" + serverInfo.getAddress().getHostString() + ":" + serverInfo.getAddress().getPort());
                pp.sendMessage("§8■ §7Motd: §3" + serverInfo.getMotd());
                pp.sendMessage("§8■ §7Restricted: §3" + serverInfo.isRestricted());
                pp.sendMessage("§8■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■ ■");
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (pp.hasPermission("friday.server.delete")) {

                    if (args.length < 2) {
                        pp.sendMessage("§cPlease enter server name.");
                        return;
                    }
                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(args[1]);
                    if (serverInfo == null) {
                        pp.sendMessage("§cServer " + args[1] + " doesn't exist.");
                        return;
                    }
                    if (serverInfo.getName().equalsIgnoreCase(pp.getPendingConnection().getListener().getFallbackServer())) {
                        pp.sendMessage("§cYou can't delete fallback server.");
                        return;
                    }
                    Friday.getInstance().getServerManager().removeServer(args[1]);
                    pp.sendMessage("§aServer was successfully deleted.");
                } else {
                    pp.sendMessage("§cYou're not allowed to do this.");
                }
            } else {
                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(args[0]);
                if (serverInfo == null) {
                    pp.sendMessage("§cServer " + args[0] + " doesn't exist.");
                    return;
                }
                pp.sendMessage("§8[§9Server§8] §7Connecting to §3" + args[0] + "§7...");

                pp.connect(serverInfo);
            }
        } else {

        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = Lists.newArrayList();
        list.clear();
        ProxyServer.getInstance().getServers().forEach((name, info) -> list.add(name));
        return list;
    }
}
