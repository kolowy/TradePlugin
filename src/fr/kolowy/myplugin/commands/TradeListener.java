package fr.kolowy.myplugin.commands;
 
import java.util.HashMap;
import java.util.List;
 
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kolowy.myplugin.MyPlugin;
 
public class TradeListener implements Listener {
    
    public HashMap<Player,Player> tradingPlayers = new HashMap<Player,Player>();
    
    public void addPlayersToTradelist(Player p1, Player p2){
        tradingPlayers.put(p1, p2);
    }
    
    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e){
        if(e.getInventory().getTitle().equals("TRADE INVENTORY")){
            Player p = (Player) e.getWhoClicked();
            if(e.getSlotType().toString() != "QUICKBAR" && e.getRawSlot() < e.getInventory().getSize()) {
            	Economy economy = MyPlugin.getEconomy();
	            if(tradingPlayers.containsKey(p)){ //player 1	
	                if(e.getSlot() <= 4 || 9 <= e.getSlot() && e.getSlot() <= 12 || 18 <= e.getSlot() && e.getSlot() <= 21 || 29 <= e.getSlot() && e.getSlot() <= 30){
	                    if(e.getSlot() == 4){
	                        accept(p,e.getCurrentItem());
	                        e.setCancelled(true);
	                    } else {
		                    if(e.getSlot() == 29 || e.getSlot() == 30) {
		                		String meta = (String) p.getOpenInventory().getItem(28).getItemMeta().getDisplayName();
		                    	String add = (String) e.getCurrentItem().getItemMeta().getDisplayName();
		                    	int i = Integer.parseInt(meta);
		                    	int toadd = Integer.parseInt(add);

		                    	System.out.println(meta);
		                    	if(i + toadd <= economy.getBalance(p)) {
			                        ItemMeta met = (ItemMeta) p.getOpenInventory().getItem(28).getItemMeta();
			                        met.setDisplayName(Integer.toString(i + toadd));
			                        p.getOpenInventory().getItem(28).setItemMeta(met);
			                        e.setCancelled(true);
		                    	}
		                        e.setCancelled(true);
		                    }
	                    	ItemStack item = e.getInventory().getItem(4);

	                        item.setType(Material.REDSTONE_BLOCK);
	                        ItemMeta meta = item.getItemMeta();
	                        meta.setDisplayName(null);
	                        item.setItemMeta(meta);
		                    
	                    }
	                } else {
	                    e.setCancelled(true);
	                }
	            } else { //player 2
	                if(4 <= e.getSlot() && e.getSlot() <= 8  || 14 <= e.getSlot() && e.getSlot() <= 17 || 23 <= e.getSlot() && e.getSlot() <= 26 || 34 <= e.getSlot() && e.getSlot() <= 35){
	                    if(e.getSlot() == 4){
	                        accept(p,e.getCurrentItem());
	                        e.setCancelled(true);
	                    } else {
	                    	if(e.getSlot() == 34 || e.getSlot() == 35) {
	                    		String meta = (String) p.getOpenInventory().getItem(33).getItemMeta().getDisplayName();
		                    	String add = (String) e.getCurrentItem().getItemMeta().getDisplayName();
		                    	int i = Integer.parseInt(meta);
		                    	int toadd = Integer.parseInt(add);

		                    	System.out.println(meta);
		                    	if(i + toadd <= economy.getBalance(p)) {
			                        ItemMeta met = (ItemMeta) p.getOpenInventory().getItem(33).getItemMeta();
			                        met.setDisplayName(Integer.toString(i + toadd));
			                        p.getOpenInventory().getItem(33).setItemMeta(met);
			                        e.setCancelled(true);
		                    	}
		                        e.setCancelled(true);
	                    	}
		                    ItemStack item = e.getInventory().getItem(4);
		                    item.setType(Material.REDSTONE_BLOCK);
		                    ItemMeta meta = item.getItemMeta();
		                    meta.setDisplayName(null);
		                    item.setItemMeta(meta);
		                   
	                    }
	                } else {
	                    e.setCancelled(true);
	                }
	                
	            }
            }
        }
    }
    public void accept(Player p, ItemStack item){
        if(item.getType().equals(Material.REDSTONE_BLOCK)){
            item.setType(Material.EMERALD_BLOCK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(p.getName());
            item.setItemMeta(meta);
        } else if (item.getType().equals(Material.EMERALD_BLOCK)){
            if(item.getItemMeta().getDisplayName().equals(p.getName())){
                item.setType(Material.REDSTONE_BLOCK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(null);
                item.setItemMeta(meta);
            } else {
                finishTrade(p.getOpenInventory().getTopInventory());
            }
        }
    }
    
    public void finishTrade(Inventory inv){
    	Economy economy = MyPlugin.getEconomy();

        List<HumanEntity> viewers = inv.getViewers();
        Player p1;
        Player p2;
       if(tradingPlayers.containsKey((Player) viewers.get(0))){
            p1 = (Player) viewers.get(0);
            p2 = (Player) viewers.get(1);
        } else {
            p1 = (Player) viewers.get(1);
            p2 = (Player) viewers.get(0);
        }
        p1.closeInventory();
        p2.closeInventory();
        tradingPlayers.remove(p1);
        for(int i = 0; i<36; i++){
        	if(i <= 3 || 9 <= i && i <= 12 || 18 <= i && i <= 21) {
        		System.out.println(i);
	            if(inv.getItem(i)!=null){
	            	System.out.println(i);
	                p2.getInventory().addItem(inv.getItem(i));
	            }
        	} else if(5 <= i && i <= 8  || 14 <= i && i <= 17 || 23 <= i && i <= 26) {
	            if(inv.getItem(i)!=null){
	                p1.getInventory().addItem(inv.getItem(i));
	            }
	        } else if(i == 28) {
        		String meta = (String) inv.getItem(i).getItemMeta().getDisplayName();
            	int met = Integer.parseInt(meta);
            	System.out.println("give " + met + "to " + p2);
            	economy.withdrawPlayer(p1, met);
            	economy.depositPlayer(p2, met);
        	} else if(i == 33) {
        		String meta = (String) inv.getItem(i).getItemMeta().getDisplayName();
            	int met = Integer.parseInt(meta);
            	System.out.println("give " + met + "to " + p1);
            	economy.withdrawPlayer(p2, met);
            	economy.depositPlayer(p1, met);
        	}
        }
    }
}