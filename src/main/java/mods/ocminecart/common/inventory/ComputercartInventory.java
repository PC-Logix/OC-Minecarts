package mods.ocminecart.common.inventory;

import mods.ocminecart.common.minecart.ComputerCart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ComputercartInventory extends Inventory {
	
	private ComputerCart cart;
	
	public ComputercartInventory(ComputerCart cart){
		this.cart=cart;
	}
	
	@Override
	public int getSizeInventory() {
		return cart.getInventorySpace();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.cart.isUseableByPlayer(p_70300_1_);
	}

	@Override
	protected void slotChanged(int slot) {
		if(!this.cart.worldObj.isRemote){
			this.cart.machine().signal("inventory_changed",slot);
		}
	}
	
	public Iterable<ItemStack> removeOverflowItems(int size){
		List<ItemStack> list = new ArrayList<ItemStack>();
		for(int i=size;i<this.getSizeInventory();i+=1){
			if(this.getStackInSlot(i) != null){
				list.add(this.getStackInSlot(i));
				this.setInventorySlotContents(i, null);
			}
		}
		return list;
	}

	@Override
	public int getMaxSizeInventory() {
		return 80;
	}

	@Override
	protected boolean ignoreNullStacks() {
		return false;
	}

}
