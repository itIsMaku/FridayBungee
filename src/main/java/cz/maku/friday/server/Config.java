package cz.maku.friday.server;

import cz.maku.friday.Friday;
import io.netty.channel.unix.DomainSocketAddress;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author tavonkelly
 */

public class Config {

    private static File file;
    private static Configuration bungeeConfig;
    private static boolean locked;

    static {
        setupConfig();
        if (locked) {
            ProxyServer.getInstance().getScheduler().schedule(Friday.getInstance(), Config::setupConfig, 5L, TimeUnit.SECONDS);
        }
    }

    public static String socketAddressToString(SocketAddress socketAddress) {
        String addressString;

        if (socketAddress instanceof DomainSocketAddress) {
            addressString = "unix:" + ((DomainSocketAddress) socketAddress).path();
        } else if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetAddress = (InetSocketAddress) socketAddress;

            addressString = inetAddress.getHostString() + ":" + inetAddress.getPort();
        } else {
            addressString = socketAddress.toString();
        }

        return addressString;
    }

    public static void addToConfig(ServerInfo serverInfo) {
        if (locked) {
            return;
        }

        bungeeConfig.set("servers." + serverInfo.getName() + ".motd", serverInfo.getMotd().replace(ChatColor.COLOR_CHAR, '&'));
        bungeeConfig.set("servers." + serverInfo.getName() + ".address", socketAddressToString(serverInfo.getSocketAddress()));
        bungeeConfig.set("servers." + serverInfo.getName() + ".restricted", serverInfo.isRestricted());
        saveConfig();
    }

    public static void removeFromConfig(ServerInfo serverInfo) {
        removeFromConfig(serverInfo.getName());
    }

    public static void removeFromConfig(String name) {
        if (locked) {
            return;
        }

        bungeeConfig.set("servers." + name, null);
        saveConfig();
    }

    private static void saveConfig() {
        if (locked) {
            return;
        }

        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(bungeeConfig, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setupConfig() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            file = new File(ProxyServer.getInstance().getPluginsFolder().getParentFile(), "config.yml");

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);

            bungeeConfig = YamlConfiguration.getProvider(YamlConfiguration.class).load(isr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ignored) {
            }
        }

        locked = bungeeConfig == null;
    }

}