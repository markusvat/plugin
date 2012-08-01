package com.buddy251markusvat.credit;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.iCo6.system.Account;

public class Main extends JavaPlugin {
	private final static String h = "§6[Bank] §f";
	private final static String e = "§6[Bank] §4";
	@Override
	public void onEnable() {
		getCommand("credit").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
				if(arg0 instanceof Player) {
					Player p = (Player)arg0;
					Account acc = new Account(p.getName());
					if(arg3.length != 1) {
						p.sendMessage(e + "Der Befehl muss lauten §e/credit <GeldAnzahl>");
						return true;
					}
					for(String l : getConfig().getStringList("save")) {
						if(l.split(" ")[0].equals(p.getName())) {
							p.sendMessage(e + "Du hast bereits ein Credit von " + l.split(" ")[1] + " wovon du noch " + l.split(" ")[2] + " zurück Zahlen musst!");
							return true;
						}
					}
					double geld = 0;
					try {
						geld = Double.parseDouble(arg3[0]);
					} catch(NumberFormatException e) {
						p.sendMessage(e + "Der Befehl muss lauten §e/credit <GeldAnzahl(Zahl)>");
						return true;
					}
					if(geld > getConfig().getDouble("limit")) {
						p.sendMessage(e + "Dieser Betrag ist zuviel! Das Limit ist bei §e" + getConfig().getDouble("limit") + "DP§4!");
						return true;
					}
					p.sendMessage(h + "Du hast einen Kredit aufgenommen. Du musst es in "+getConfig().getInt("ruckzahlung")+"Tagen zurückzahlen!");
					
					acc.getHoldings().add(geld);
					acc.getHoldings().showBalance(arg0);
					
					List<String> w = getConfig().getStringList("save");
					w.add(p.getName() + " " + arg3[0] + " " + arg3[0] + " " + getConfig().getInt("ruckzahlung"));
					getConfig().set("save", w);
					saveConfig();
					return true;
				} else {
					arg0.sendMessage("Ingame Command!");
				}
				return true;
			}
		});
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				ArrayList<String> ne = new ArrayList<String>();
				for(String s : getConfig().getStringList("save")) {
					int old = Integer.parseInt(s.split(" ")[3]);
					old--;
//					if(old < 5 && old > 1) {
//						Player p = Bukkit.getPlayer(s.split(" ")[0]);
//						if(p != null) {
//							p.sendMessage(e + "Dein Kredit läuft in " + old + " Minuten aus!");
//							p.sendMessage(e + "Bitte bezahle " + s.split(" ")[1] + "DP mit §e/paycredit");
//						}
//					}
					if(old == 0) {
						Player p = Bukkit.getPlayer(s.split(" ")[0]);
						Account acc = new Account(s.split(" ")[0]);
						acc.getHoldings().subtract(Integer.parseInt(s.split(" ")[2]));
						if(p != null) {
							acc.getHoldings().showBalance(p);
							p.sendMessage(e + "Dein Kredit ist ausgelaufen!");
						}
						continue;
					}
					StringBuilder n = new StringBuilder();
					for(int i = 0; i != 2; i++) {
						n.append(s.split(" ")[i] + " ");
					}
					n.append(old + new String());
					ne.add(n.toString());
				}
				getConfig().set("save", ne);
				saveConfig();
			}
		}, 1200l, 1200l);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@Override
	public void onDisable() {
		
	}
}
