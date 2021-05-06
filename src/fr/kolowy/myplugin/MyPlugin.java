package fr.kolowy.myplugin;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import fr.kolowy.myplugin.commands.CommandTrade;
import fr.kolowy.myplugin.commands.TradeListener;

public class MyPlugin extends JavaPlugin {
    private static Economy econ = null;

	
	@Override
    public void onEnable() {
		System.out.println("Plugin On");
		TradeListener trader = new TradeListener();
		if (!setupEconomy()) {
			System.out.println(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		getCommand("trade").setExecutor(new CommandTrade(trader));


		getServer().getPluginManager().registerEvents(new MyPluginListner(), this);
		getServer().getPluginManager().registerEvents(trader, this);

    }
	
    @Override
    public void onDisable() {
    	System.out.println("Plugin Off");
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }

}
