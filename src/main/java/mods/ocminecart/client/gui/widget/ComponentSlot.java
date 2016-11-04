package mods.ocminecart.client.gui.widget;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Objects;

public abstract class ComponentSlot extends Slot{
	
	protected EntityPlayer player;
	protected Container container;
	protected String slot;
	protected int tier;
	
	public ComponentSlot(IInventory inventory, int id, int x,int y, EntityPlayer player, Container container, int tier, String type) {
		super(inventory, id, x, y);
		this.container = container;
		this.player = player;
	}

	@Override
	public boolean canBeHovered() {
		return !slot.equals(li.cil.oc.api.driver.item.Slot.None) && super.canBeHovered() && tier!=-1;
	}
	
	public boolean isItemValid(ItemStack stack){
		return this.inventory.isItemValidForSlot(this.slotNumber, stack);
	}
	
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack){
		super.onPickupFromSlot(player, stack);
		for (Object slot : container.inventorySlots) {
			if (slot instanceof ComponentSlot) {
				((ComponentSlot) slot).clearIfInvalid(player);
			}
		}
	}
	
	public void onSlotChanged(){
		super.onSlotChanged();
		for (Object slot : container.inventorySlots) {
			if (slot instanceof ComponentSlot) {
				((ComponentSlot) slot).clearIfInvalid(player);
			}
		}
	}
	
	protected abstract void clearIfInvalid(EntityPlayer player);
}
