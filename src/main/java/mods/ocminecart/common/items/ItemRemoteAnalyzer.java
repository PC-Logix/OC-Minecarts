package mods.ocminecart.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.assemble.util.TooltipUtil;
import mods.ocminecart.common.entityextend.RemoteExtenderRegister;
import mods.ocminecart.common.items.interfaces.ItemEntityInteract;
import mods.ocminecart.network.ModNetwork;
import mods.ocminecart.network.message.ItemUseMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemRemoteAnalyzer extends Item implements ItemEntityInteract, ItemUseMessage.IMPUsageItem{
	
	public ItemRemoteAnalyzer(){
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName(OCMinecart.MODID+".remoteanalyzer");
	}
	
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player){ return true; }
	
	//Called in the EventHandler
	public boolean onEntityClick(EntityPlayer p, Entity e, ItemStack s, Type t, EnumHand hand){
		if(e instanceof EntityMinecart){
			if(p.worldObj.isRemote) return true;
			if(RemoteExtenderRegister.hasRemote((EntityMinecart) e) &&
					RemoteExtenderRegister.getExtender((EntityMinecart) e).isEnabled()){
				if(t==Type.RIGHT_CLICK){
					RemoteExtenderRegister.getExtender((EntityMinecart) e).onAnalyzeModule(p);
				
					NBTTagCompound usedat = new NBTTagCompound();
					usedat.setString("address", RemoteExtenderRegister.getExtender((EntityMinecart) e).getAddress());
					ModNetwork.sendToNearPlayers(new ItemUseMessage(p.getEntityId(), hand,usedat), e.posX, e.posY, e.posZ, e.worldObj);
				}
				else if(t==Type.LEFT_CLICK){
					if(RemoteExtenderRegister.getExtender((EntityMinecart) e).editableByPlayer(p,false))
						p.openGui(OCMinecart.instance, 2, e.worldObj, e.getEntityId(), -10, 0);
					else{
						NBTTagCompound usedat = new NBTTagCompound();
						usedat.setInteger("type", 1);
						ModNetwork.channel.sendTo(new ItemUseMessage(p.getEntityId(), hand,usedat), (EntityPlayerMP) p);
					}
				}
			}
			else if(RemoteExtenderRegister.hasRemote((EntityMinecart) e))
				p.addChatComponentMessage(new TextComponentString(ChatFormatting.LIGHT_PURPLE+"No Module found."));
			else
				p.addChatComponentMessage(new TextComponentString(ChatFormatting.LIGHT_PURPLE+"No Module installable."));
			return true;
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void onMPUsage(EntityPlayer p, EnumHand hand, NBTTagCompound data){
		if(p!=Minecraft.getMinecraft().thePlayer) return;
		if(data.hasKey("type") && data.getInteger("type")==1){
			p.addChatComponentMessage(new TextComponentString(ChatFormatting.RED+
					I18n.translateToLocal("chat."+OCMinecart.MODID+".owneronly")));
		}
		else if(p.isSneaking() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			GuiScreen.setClipboardString(data.getString("address"));
			p.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("chat."+OCMinecart.MODID+".clipboard")));
		}
		p.swingArm(hand);
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		if(!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())){
			String key = GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
			String formkey = "[" + ChatFormatting.WHITE + key + ChatFormatting.GRAY + "]";
			list.add(I18n.translateToLocalFormatted("tooltip."+OCMinecart.MODID+".moreinfo", formkey));
		}
		else{
			list.addAll(TooltipUtil.trimString(I18n.translateToLocal("tooltip."+OCMinecart.MODID+".remoteanalyzer.desc")));
		}
	}
}
