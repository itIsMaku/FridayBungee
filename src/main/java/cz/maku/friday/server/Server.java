package cz.maku.friday.server;

import lombok.Getter;

import java.net.InetSocketAddress;

@Getter
@Deprecated
public class Server {

    private final String name;
    private final ServerId serverId;
    private final int maxPlayers;
    private final boolean whitelisted;
    private final String fullName;
    private final InetSocketAddress ip;
    private final String motd;

    public Server(String name, int maxPlayers, boolean whitelisted, InetSocketAddress ip, String motd) {
        this.name = name;
        this.serverId = new ServerId();
        this.maxPlayers = maxPlayers;
        this.whitelisted = whitelisted;
        this.fullName = name + "-" + serverId.getHex();
        this.ip = ip;
        this.motd = motd;
    }
}
