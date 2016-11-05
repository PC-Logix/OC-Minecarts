package mods.ocminecart.common;

import mods.ocminecart.Settings;
import mods.ocminecart.client.SlotIcons;
import mods.ocminecart.common.entityextend.RemoteExtenderRegister;
import mods.ocminecart.common.items.interfaces.ItemEntityInteract;
import mods.ocminecart.common.recipe.event.CraftingHandler;
import mods.ocminecart.network.ModNetwork;
import mods.ocminecart.network.message.ConfigSyncMessage;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EventHandler {
	
	int ticks=0; // 40 Server Ticks/sec but we want only 20 
	
	public static void initHandler(){
		EventHandler handler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onItemIconRegister(TextureStitchEvent event) {
		SlotIcons.register(event.getMap());
	}
	
	@SubscribeEvent
	public void onEntityClick(PlayerInteractEvent.EntityInteract event) {
		ItemStack stack = event.getEntityPlayer().inventory.getCurrentItem();
		if(stack!=null && stack.getItem() instanceof ItemEntityInteract){
			if(((ItemEntityInteract) stack.getItem()).onEntityClick(event.getEntityPlayer(), event.getTarget(),
					stack, ItemEntityInteract.Type.RIGHT_CLICK))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityHit(AttackEntityEvent event) {
		ItemStack stack = event.entityPlayer.inventory.getCurrentItem();
		if(stack!=null && stack.getItem() instanceof ItemEntityInteract){
			if(((ItemEntityInteract) stack.getItem()).onEntityClick(event.entityPlayer, event.target,
					stack, ItemEntityInteract.Type.LEFT_CLICK))
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
		if(event.entity instanceof EntityMinecart){
			RemoteExtenderRegister.addRemote((EntityMinecart) event.entity);
		}
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		if(event.phase == TickEvent.Phase.START) return;
		RemoteExtenderRegister.serverTick();
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
		NBTTagCompound config = new NBTTagCompound();
		config.setIntArray("remoterange", Settings.RemoteRange);
		ModNetwork.channel.sendTo(new ConfigSyncMessage(config), (EntityPlayerMP)event.player);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event){
		if(event.getWorld().isRemote && event.getWorld().provider.getDimension()==0) return;
		RemoteExtenderRegister.reinit();
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event){
		if(event.getWorld().isRemote && event.getWorld().provider.getDimension()==0) return;
		RemoteExtenderRegister.reinit();
	}
}
