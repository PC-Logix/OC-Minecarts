package mods.ocminecart.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.Settings;
import mods.ocminecart.common.assemble.util.TooltipUtil;
import mods.ocminecart.common.entityextend.RemoteCartExtender;
import mods.ocminecart.common.entityextend.RemoteExtenderRegister;
import mods.ocminecart.common.items.interfaces.ItemEntityInteract;
import mods.ocminecart.network.ModNetwork;
import mods.ocminecart.network.message.ItemUseMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Random;

public class ItemCartRemoteModule extends Item implements ItemEntityInteract, ItemUseMessage.IMPUsageItem{

	public static int[] range;
	
	public ItemCartRemoteModule(){
		super();
		this.setMaxStackSize(64);
		this.setUnlocalizedName(OCMinecart.MODID+".remotemodule");
		this.setHasSubtypes(true);
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()) range = Settings.RemoteRange;
	}
	
	 public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player){ return true; }
	 
	 public String getItemStackDisplayName(ItemStack stack){
	    	return this.getDisplayString(stack, false);
	 }
	
	//Called in the EventHandler
	@Override
	public boolean onEntityClick(EntityPlayer p, Entity e, ItemStack s, Type t, EnumHand hand) {
		if((e instanceof EntityMinecart) && t == Type.RIGHT_CLICK){
			if(p.worldObj.isRemote) return true;
			int err = RemoteExtenderRegister.enableRemote((EntityMinecart) e, true);
			if(err==0){
				RemoteCartExtender ext = RemoteExtenderRegister.getExtender((EntityMinecart) e);
				if(ext!=null){
					ext.setRemoteItem(s);
					ext.setMaxWlanStrength(getRangeByTier(s.getItemDamage()));
					ext.setOwner(p.getUniqueID().toString());
				}
			}
			
			NBTTagCompound usedat = new NBTTagCompound();
			usedat.setDouble("posX", e.posX);
			usedat.setDouble("posY", e.posY);
			usedat.setDouble("posZ", e.posZ);
			usedat.setByte("error", (byte)err);
			ModNetwork.sendToNearPlayers(new ItemUseMessage(p.getEntityId(), hand ,usedat), e.posX, e.posY, e.posZ, e.worldObj);
			if(!p.capabilities.isCreativeMode && err == 0) s.stackSize--;
				
			return true;
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void onMPUsage(EntityPlayer p, EnumHand hand, NBTTagCompound data){
		World worldObj = p.worldObj;
		double posX = data.getDouble("posX");
		double posY = data.getDouble("posY");
		double posZ = data.getDouble("posZ");
		byte error = data.getByte("error");
		Random r = new Random();
		if(error == 0){
			p.swingArm(hand);
			if(p.equals(Minecraft.getMinecraft().thePlayer))
				p.addChatMessage(new TextComponentString(ChatFormatting.GREEN+ I18n.translateToLocal("chat."+OCMinecart.MODID+".moduleinstalled")));
		}
		else if(error == 1) p.addChatMessage(new TextComponentString(ChatFormatting.RED+I18n.translateToLocal("chat."+OCMinecart.MODID+".invalidcart")));
		else p.addChatMessage(new TextComponentString(ChatFormatting.RED+I18n.translateToLocal("chat."+OCMinecart.MODID+".hasmodule")));
		for(int i=0;i<100;i++){
			if(error == 0)
				worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, posX+(r.nextDouble()-0.5)*1.4, posY+(r.nextDouble()-0.5)*1.4, posZ+(r.nextDouble()-0.5)*1.4, 0, 0, 0);
			else
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX+(r.nextDouble()-0.5)*1.4, posY-0.3, posZ+(r.nextDouble()-0.5)*1.4, 0, 0, 0);
		}
	}
	
	public int getRangeByTier(int tier){
		switch(tier){
		case 1:
			return range[1];
		case 2:
			return range[2];
		default:
			return range[0];
		}
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for(int i=0;i<3;i+=1){
			list.add(new ItemStack(item,1,i));
		}
	}
	
	 public String getDisplayString(ItemStack stack,boolean hasColor){
	    	ChatFormatting color;
	    	String tier;
	    	tier=I18n.translateToLocal("tooltip."+OCMinecart.MODID+".tier"+(stack.getItemDamage()+1));
	    	switch(stack.getItemDamage()){
	    	case 0:
	    		color = ChatFormatting.WHITE;
	    		break;
	    	case 1:
	    		color = ChatFormatting.YELLOW;
	    		break;
	    	case 2:
	    		color = ChatFormatting.AQUA;
	    		break;
	    	default:
	    		color = ChatFormatting.DARK_RED;
	    		tier = "ERROR!";
	    		break;
	    	}
	    	if(!hasColor){
	    		color=ChatFormatting.RESET;
	    	}
	    	return color+super.getItemStackDisplayName(stack)+" "+tier;
	    }
	
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		
		list.clear();
		list.add(this.getDisplayString(stack, true));
		
		if(!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())){
			String key = GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
			String formkey = "[" + ChatFormatting.WHITE + key + ChatFormatting.GRAY + "]";
			list.add(I18n.translateToLocalFormatted("tooltip."+OCMinecart.MODID+".moreinfo", formkey));
		}
		else{
			list.addAll(TooltipUtil.trimString(I18n.translateToLocal("tooltip."+OCMinecart.MODID+".remotemodule.desc")));
			list.add("Max. Range: "+ChatFormatting.WHITE+getRangeByTier(stack.getItemDamage()));
		}
	}
}
