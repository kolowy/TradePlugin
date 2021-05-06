package fr.kolowy.myplugin.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kolowy.myplugin.MyPlugin;
import net.milkbowl.vault.economy.Economy;

public class CommandTrade  implements CommandExecutor {

    HashMap<Player,Player> requestTrade = new HashMap<Player,Player>();
    
    TradeListener tradeList;
    
    public CommandTrade(TradeListener listner) {
    	tradeList = listner;
    }
    Economy economy = MyPlugin.getEconomy();
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			
		    if(cmd.getName().equalsIgnoreCase("trade")) {
				if(args.length == 0){
					player.sendMessage("La commande est : /trade <player>");
				}
				
				//accept trade
				if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("yes") ){
		            if(requestTrade.containsKey(player)){
						System.out.println("player contain ok");
		                Player tradeWith = requestTrade.get(player);
		                if(Bukkit.getOnlinePlayers().contains(tradeWith)){
							System.out.println("online ok");
		                	if(player.getLocation().distance(tradeWith.getLocation()) <= 20) {
		    					System.out.println("distance ok");
		                		Inventory tradeInv = Bukkit.createInventory(null, 36, "TRADE INVENTORY");
		                		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		                        //ItemMeta metartp = (ItemMeta) glass.getItemMeta();
		                        //metartp.setDisplayName("0");
		                        //glass.setItemMeta(metartp);
		                        ItemStack wool = new ItemStack(Material.REDSTONE_BLOCK, 1);
		                        ItemMeta meta = (ItemMeta) wool.getItemMeta();
		                        meta.setDisplayName("Accept");
		                        wool.setItemMeta(meta);
		                		ItemStack help = new ItemStack(Material.APPLE, 1);
		                		ItemMeta help_meta = (ItemMeta) help.getItemMeta();
		                		help_meta.setDisplayName("§cHELP :");
		                		help_meta.setLore(Arrays.asList("§aÉmeraude : Votre argent","§aDiamant : Argent donné au trade", "§aIron : Donner 10$ de plus", "§aGold : Donner 100$ de plus"));
		                		help.setItemMeta(help_meta);

		                        tradeInv.setItem(4, wool);
		                        tradeInv.setItem(13, glass);
		                        tradeInv.setItem(22, glass);
		                        tradeInv.setItem(31, help);
		                        
		                        //p1 money
		                        ItemStack player_money = new ItemStack(Material.EMERALD, 1);
		                		ItemMeta player_money_meta = (ItemMeta) player_money.getItemMeta();
		                		player_money_meta.setDisplayName("Bank de " + player + " " + economy.getBalance(player));
		                		player_money.setItemMeta(player_money_meta);
		                        tradeInv.setItem(27, player_money);
		                        ItemStack money = new ItemStack(Material.DIAMOND, 1);
		                		ItemMeta money_meta = (ItemMeta) money.getItemMeta();
		                		money_meta.setDisplayName("0");
		                		money.setItemMeta(money_meta);
		                        tradeInv.setItem(28, money);
		                        ItemStack dix = new ItemStack(Material.IRON_INGOT, 1);
		                		ItemMeta dix_meta = (ItemMeta) dix.getItemMeta();
		                		dix_meta.setDisplayName("+10");
		                		dix.setItemMeta(dix_meta);
		                        tradeInv.setItem(29, dix);
		                        ItemStack cent = new ItemStack(Material.GOLD_INGOT, 1);
		                		ItemMeta cent_meta = (ItemMeta) cent.getItemMeta();
		                		cent_meta.setDisplayName("+100");
		                		cent.setItemMeta(cent_meta);
		                        tradeInv.setItem(30, cent);
		                        
		                        //p2 money
		                		player_money_meta.setDisplayName(tradeWith + " " + economy.getBalance(player));
		                		player_money.setItemMeta(player_money_meta);
		                        tradeInv.setItem(32, player_money);
		                        money_meta.setDisplayName("0");
		                		money.setItemMeta(money_meta);
		                        tradeInv.setItem(33, money);
		                        tradeInv.setItem(34, dix);
		                        tradeInv.setItem(35, cent);


		                        player.openInventory(tradeInv);
		                        player.openInventory(tradeInv);
		                        tradeWith.openInventory(tradeInv);
		                        tradeList.addPlayersToTradelist(player, tradeWith);
	                            requestTrade.remove(player);
		                	} else {
		                    	player.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.WHITE + " est trop loin de vous");
	                            requestTrade.remove(player);
		                    }
		                } else {
		                	player.sendMessage(tradeWith + " n'est plus en ligne...");
		                    requestTrade.remove(player);
		                }
		            } else {
		            	player.sendMessage("Aucun trade en cours !");
		            }
		            
		        //deny trade
				} else if(args[0].equals("ignore") || args[0].equals("no") ){
		            if(requestTrade.containsKey(player)){
		            	player.sendMessage("La demande à été supprimée");
		                requestTrade.remove(player);
		
		            } else {
		            	player.sendMessage("Vous n'avez aucun trade en attente.");
		            }
		
		        } else {
		            Player tradeWith = Bukkit.getPlayer(args[0]);
		            if(tradeWith == player) {
		                player.sendMessage("Vous ne pouvez pas trade avec vous meme");
		                return true;
		            }
		            if(Bukkit.getOnlinePlayers().contains(tradeWith)){
		            	if(player.getLocation().distance(tradeWith.getLocation()) <= 20) {
		                    if (player.isSleeping()) {
		                        player.sendMessage("Vous ne pouvez pas trade en dormant");
		                        return true;
		                    }
		                    player.sendMessage("Vous avez envoyé une demande de trade à : " + ChatColor.YELLOW + args[0]);
		                    requestTrade.put(tradeWith, player);
		                    tradeWith.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " veut trade avec vous");
		            	} else {
		                	player.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.WHITE + " est trop loin de vous");
		                }
		            } else {
		            	player.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.WHITE + " n'est pas en ligne, ou n'existe pas");
		            }
		        }
		        return true;
	
			}
		}
		return false;
	    
	}

}
