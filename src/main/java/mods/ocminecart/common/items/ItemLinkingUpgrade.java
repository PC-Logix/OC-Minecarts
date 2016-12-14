package mods.ocminecart.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.assemble.util.TooltipUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLinkingUpgrade extends Item{
	
	public ItemLinkingUpgrade(){
		super();
		this.setMaxStackSize(64);
		this.setUnlocalizedName(OCMinecart.MODID+".linkingupgrade");
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
		
		if(!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())){
			String key = GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
			String formkey = "[" + ChatFormatting.WHITE + key + ChatFormatting.GRAY + "]";
			list.add(I18n.translateToLocalFormatted("tooltip."+OCMinecart.MODID+".moreinfo", formkey));
		}
		else{
			list.addAll(TooltipUtil.trimString(I18n.translateToLocal("tooltip."+OCMinecart.MODID+".linkingupgrade.desc")));
		}
	}
}
