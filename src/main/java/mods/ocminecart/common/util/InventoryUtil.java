package mods.ocminecart.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

public class InventoryUtil {

	public static int dropItemInventoryWorld(ItemStack stack, World world, BlockPos pos, EnumFacing access, int num) {
		TileEntity entity = world.getTileEntity(pos);
		if (entity instanceof IInventory) {
			return putInventory(stack, (IInventory) entity, num, access);
		}
		return -1;
	}

	public static int spaceforItem(ItemStack stack, IInventory inv, int[] access){
		int space = 0;
		int maxstack = Math.min((stack==null) ? 64 : stack.getMaxStackSize(), inv.getInventoryStackLimit());
		for(int i=0;i<access.length;i+=1){
			ItemStack slot = inv.getStackInSlot(access[i]);
			if(slot==null)
				space+=maxstack;
			else if(stack != null && slot.isItemEqual(stack)){
				space+=Math.max(0, maxstack-slot.stackSize);
			}
		}
		return space;
	}
	
	/*
	 * Try inserting an item stack into a player inventory. If that fails, drop it into the world.
	 * Copy from li.cil.oc.util.InventoryUtils  Line 308
	 */
	public static void addToPlayerInventory(ItemStack stack, EntityPlayer player) {
		if (stack != null) {
			if (player.inventory.addItemStackToInventory(stack)) {
				player.inventory.markDirty();
				if (player.openContainer != null) {
					player.openContainer.detectAndSendChanges();
				}
			}
			if (stack.stackSize > 0) {
				player.dropItem(stack, true, true);
			}
		}
	}
}
