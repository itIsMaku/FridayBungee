package cz.maku.friday;

import cz.maku.friday.command.PluginsCmd;
import cz.maku.friday.command.ServerCmd;
import cz.maku.friday.server.ServerManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class Friday extends Plugin {

    @Getter
    private static Friday instance;
    private ServerManager serverManager;

    @Override
    public void onEnable() {
        instance = this;
        serverManager = new ServerManager();
        getProxy().getPluginManager().registerCommand(this, new ServerCmd());
        getProxy().getPluginManager().registerCommand(this, new PluginsCmd());
    }

    @Override
    public void onDisable() {

    }
}
