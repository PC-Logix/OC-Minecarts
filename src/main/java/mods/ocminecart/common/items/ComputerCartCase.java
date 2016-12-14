package mods.ocminecart.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mods.ocminecart.OCMinecart;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.List;
import java.util.Map;

public class ComputerCartCase extends Item implements ModItems.IItemModelRegister{
	
	ComputerCartCase(){
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName(OCMinecart.MODID+".computercartcase");
		this.setMaxStackSize(1);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for(int i=0;i<=3;i+=1){
			list.add(new ItemStack(item,1,i));
		}
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		ChatFormatting color = ChatFormatting.RESET;
		switch(stack.getItemDamage()){
		case 0:	//Tier 1
			color = ChatFormatting.WHITE;
			break;
		case 1: //Tier 2
			color = ChatFormatting.YELLOW;
			break;
		case 2:  //Tier 3
			color = ChatFormatting.AQUA;
			break;
		case 3: //Creative
			color = ChatFormatting.LIGHT_PURPLE;
		}
		list.clear();
		list.add(color+this.getItemStackDisplayName(stack)+" "+ I18n.translateToLocal("tooltip."+OCMinecart.MODID+".tier"+(stack.getItemDamage()+1)));
	}

	@Override
	public void modelVariants(Map<Integer, ModelResourceLocation> models) {
		models.put(0, new ModelResourceLocation(OCMinecart.MODID+":computercartcase_1","inventory"));
		models.put(1, new ModelResourceLocation(OCMinecart.MODID+":computercartcase_2","inventory"));
		models.put(2, new ModelResourceLocation(OCMinecart.MODID+":computercartcase_3","inventory"));
		models.put(3, new ModelResourceLocation(OCMinecart.MODID+":computercartcase_4","inventory"));
	}
}
