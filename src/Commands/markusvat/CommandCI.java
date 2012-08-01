package Commands.markusvat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCI {
	CommandSender sender;
	Command cmd;
	String[] arg;
	
	public CommandCI(CommandSender sender, Command cmd, String[] arg){
		this.sender=sender;
		this.cmd=cmd;
		this.arg=arg;
	}
	public boolean run(){
		((Player)sender).getInventory().clear();
		return true;
	}
}
