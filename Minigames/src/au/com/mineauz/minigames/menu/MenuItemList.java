package au.com.mineauz.minigames.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;

public class MenuItemList extends MenuItem{
	
	private Callback<String> value = null;
	private List<String> options = null;
	
	private InteractionInterface onChange;
	
	public MenuItemList(String name, Material displayItem, Callback<String> value, List<String> options) {
		super(name, displayItem);
		this.value = value;
		this.options = options;
		updateDescription();
	}
	
	public MenuItemList(String name, List<String> description, Material displayItem, Callback<String> value, List<String> options) {
		super(name, description, displayItem);
		this.value = value;
		this.options = options;
		updateDescription();
	}
	
	public void updateDescription(){
		List<String> description = null;
		int pos = options.indexOf(value.getValue());
		if (pos == -1)
			pos = 0;
		int before = pos - 1;
		int after = pos + 1;
		if(before == -1)
			before = options.size() - 1;
		if(after == options.size())
			after = 0;
		
		if(getDescription() != null){
			description = getDescription();
			if(getDescription().size() >= 3){
				String desc = ChatColor.stripColor(getDescription().get(1));
				
				if(options.contains(desc)){
					description.set(0, ChatColor.GRAY.toString() + options.get(before));
					description.set(1, ChatColor.GREEN.toString() + value.getValue());
					description.set(2, ChatColor.GRAY.toString() + options.get(after));
				}
				else{
					description.add(0, ChatColor.GRAY.toString() + options.get(before));
					description.add(1, ChatColor.GREEN.toString() + value.getValue());
					description.add(2, ChatColor.GRAY.toString() + options.get(after));
				}
			}
			else{
				description.add(0, ChatColor.GRAY.toString() + options.get(before));
				description.add(1, ChatColor.GREEN.toString() + value.getValue());
				description.add(2, ChatColor.GRAY.toString() + options.get(after));
			}
		}
		else{
			description = new ArrayList<String>();
			description.add(ChatColor.GRAY.toString() + options.get(before));
			description.add(ChatColor.GREEN.toString() + value.getValue());
			description.add(ChatColor.GRAY.toString() + options.get(after));
		}
		
		setDescription(description);
	}
	
	@Override
	public ItemStack onClick(MinigamePlayer player){
		int ind = options.lastIndexOf(value.getValue());
		ind++;
		if(ind == options.size())
			ind = 0;
		
		value.setValue(options.get(ind));
		updateDescription();
		
		onChange(player);
		if (onChange != null) {
			onChange.interact(player, null);
		}
		
		return getItem();
	}
	
	@Override
	public ItemStack onRightClick(MinigamePlayer player){
		int ind = options.lastIndexOf(value.getValue());
		ind--;
		if(ind == -1)
			ind = options.size() - 1;
		
		value.setValue(options.get(ind));
		updateDescription();
		
		onChange(player);
		if (onChange != null) {
			onChange.interact(player, null);
		}
		
		return getItem();
	}
	
	public void onChange(MinigamePlayer player) {}
	
	@Override
	public ItemStack onDoubleClick(MinigamePlayer player){
		beginManualEntry(player, "Enter the name of the option into chat for " + getName() + ", the menu will automatically reopen in 10s if nothing is entered.", 10);
		player.sendMessage("Possible Options: " + MinigameUtils.listToString(options));
		return null;
	}
	
	@Override
	public void checkValidEntry(MinigamePlayer player, String entry){
		for(String opt : options){
			if(opt.equalsIgnoreCase(entry)){
				value.setValue(opt);
				updateDescription();
				return;
			}
		}
		player.sendMessage("Could not find matching value!", "error");
	}
	
	public void setOnChange(InteractionInterface ii) {
		onChange = ii;
	}
}
