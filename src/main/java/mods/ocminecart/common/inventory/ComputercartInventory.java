package mods.ocminecart.common.inventory;

import mods.ocminecart.common.minecart.ComputerCart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IntegerCache;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputercartInventory implements IItemHandlerModifiable, IInventory {
	
	private ComputerCart cart;
	private Map<Integer, ItemStack> slots = new HashMap<>();
	
	public ComputercartInventory(ComputerCart cart){
		this.cart=cart;
	}

	protected void slotChanged(int slot) {
		if(!this.cart.worldObj.isRemote){
			this.cart.machine().signal("inventory_changed",slot);
		}
	}
	
	public Iterable<ItemStack> removeOverflowItems(int size){
		List<ItemStack> list = new ArrayList<ItemStack>();
		for(int i=size;i<this.getMaxSizeInventory();i+=1){
			if(this.getStackInSlot(i) != null){
				list.add(this.getStackInSlot(i));
				this.setStackInSlot(i, null);
			}
		}
		return list;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if(slot > getSlots())
			return stack.copy();

		ItemStack inSlot = slots.get(slot);
		if (inSlot != null)
			inSlot = inSlot.copy();

		if(inSlot == null)
		{
			inSlot = stack.copy();
			stack.stackSize = 0;
		}
		else if(inSlot.isItemEqual(stack))
		{
			int insert = Math.max(0, Math.min(inSlot.getMaxStackSize() - inSlot.stackSize, stack.stackSize));
			stack.stackSize -= insert;
			inSlot.stackSize +=insert;
		}

		if(!simulate)
			slots.put(slot, inSlot);

		if(stack.stackSize <= 0)
			return null;

		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(slot > getSlots())
			return null;

		ItemStack inSlot = slots.get(slot);
		ItemStack ret;

		if (inSlot != null)
			inSlot = inSlot.copy();

		if(inSlot == null)
		{
			return null;
		}
		else
		{
			int extract = Math.min(amount, inSlot.stackSize);
			ret = inSlot.copy();
			ret.stackSize = extract;
			inSlot.stackSize -= extract;
		}

		if(inSlot.stackSize<=0)
			inSlot = null;

		if(!simulate)
			slots.put(slot, inSlot);

		return ret;
	}

	// IInventory

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		slots.put(slot, stack);
	}

	@Override
	public int getSlots() {
		return cart.getInventorySpace();
	}

	@Override
	public int getSizeInventory() {
		return getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots.get(slot);
	}

	@Nullable
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return extractItem(index, count, false);
	}

	@Nullable
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return extractItem(index, Integer.MAX_VALUE, false);
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		setStackInSlot(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return cart.isUseableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		slots.clear();
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	// Custom

    public int getMaxSizeInventory() {
        return 80;
    }

	public void readFromNBT(NBTTagList inventory) {
		for(int i=0;i<inventory.tagCount();i++)
		{
			NBTTagCompound tag = inventory.getCompoundTagAt(i);
			if(tag.hasKey("stack") && tag.hasKey("slot"))
				slots.put(tag.getInteger("slot"), ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack")));
		}
	}

	public NBTTagList writeToNBT() {
		NBTTagList list = new NBTTagList();
		for(Map.Entry<Integer, ItemStack> entry : slots.entrySet())
		{
			if(entry.getValue() == null)
				continue;
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("stack", entry.getValue().serializeNBT());
			tag.setInteger("slot", entry.getKey());
			list.appendTag(tag);
		}
		return list;
	}
}
