package mods.ocminecart.common.items.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public interface ItemEntityInteract {
	
	public boolean onEntityClick(EntityPlayer p, Entity e, ItemStack s, Type t, EnumHand hand);
	
	enum Type{
		UNKNOWN,
		LEFT_CLICK,
		RIGHT_CLICK
	}
}
