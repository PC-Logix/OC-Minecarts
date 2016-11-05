package mods.ocminecart.common.container.slots;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.Item;
import li.cil.oc.api.driver.item.Container;
import li.cil.oc.common.Tier;
import mods.ocminecart.client.SlotIcons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSlot extends Slot{
	
	private int tier;
	private String type;
	
	public ContainerSlot(IInventory inventory, int index, int xpos, int ypos, int tier, String type) {
		super(inventory, index, xpos, ypos);
		this.tier = tier;
		this.type = type;
	}
	
	public ContainerSlot(IInventory inventory, int index, int xpos, int ypos, ItemStack container) {
		super(inventory, index, xpos, ypos);
		
		Item driver = Driver.driverFor(container);
		if(driver!=null && (driver instanceof Container)){
			this.tier = ((Container) driver).providedTier(container);
			this.type = ((Container) driver).providedSlot(container);
		}
		else{
			this.tier = Tier.None();
			this.type = li.cil.oc.api.driver.item.Slot.None;
		}
	}

	public TextureAtlasSprite getBackgroundSprite(){
		 return SlotIcons.fromTier(this.tier);
	 }
	 
	 public String getSlotType(){
		 return this.type;
	 }
	 

	public boolean isItemValid(ItemStack stack){
		if(this.type == li.cil.oc.api.driver.item.Slot.None || this.tier == Tier.None()) return false;
		else if(this.type == li.cil.oc.api.driver.item.Slot.Any && this.tier == Tier.Any()) return true;
		
		Item drv = Driver.driverFor(stack);
		if(drv == null) return false;
		if((drv.slot(stack) == this.type || drv.slot(stack) == li.cil.oc.api.driver.item.Slot.Any) && drv.tier(stack)<=this.tier) return this.inventory.isItemValidForSlot(this.slotNumber, stack);
		
		return false;
	}

	@Override
	public boolean canBeHovered() {
		return type != li.cil.oc.api.driver.item.Slot.None && tier != Tier.None();
	}

	public int getTier() {
		return this.tier;
	}

}