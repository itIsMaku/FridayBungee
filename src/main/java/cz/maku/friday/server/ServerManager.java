package cz.maku.friday.server;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;

@Getter
public class ServerManager {

    public boolean serverExists(String fullName) {
        return ProxyServer.getInstance().getServers().containsKey(fullName);
    }

    public ServerInfo getServer(String serverName) {
        return ProxyServer.getInstance().getServers().get(serverName);
    }

    public void addServer(Server server, boolean restricted) {
        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(server.getFullName(), server.getIp(), server.getMotd(), restricted);
        Config.addToConfig(serverInfo);
        ProxyServer.getInstance().getServers().put(server.getFullName(), serverInfo);
    }

    public void addServer(String name, InetSocketAddress address, String motd, boolean restricted) {
        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, address, motd, restricted);
        Config.addToConfig(serverInfo);
        ProxyServer.getInstance().getServers().put(name, serverInfo);
    }

    public void removeServer(String fullName) {
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(fullName);

        if (serverInfo == null) {
            return;
        }
        for (ProxiedPlayer p : serverInfo.getPlayers()) {
            p.connect(ProxyServer.getInstance().getServerInfo(p.getPendingConnection().getListener().getFallbackServer()));
            p.sendMessage("§8[§9Server§8] §7Connecting to fallback server, because previous was deleted.");
        }
        ProxyServer.getInstance().getServers().remove(fullName);
        Config.removeFromConfig(fullName);
    }
}
