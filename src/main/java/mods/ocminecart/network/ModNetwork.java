package mods.ocminecart.network;

import mods.ocminecart.OCMinecart;
import mods.ocminecart.network.message.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;

public class ModNetwork {
	
	public static SimpleNetworkWrapper channel;
	
	public static void init(){
		channel= NetworkRegistry.INSTANCE.newSimpleChannel(OCMinecart.MODID.toLowerCase());
		int id=-1;
		
		channel.registerMessage(GuiButtonClick.Handler.class, GuiButtonClick.class, id++, Side.SERVER);
		channel.registerMessage(ComputercartInventoryUpdate.Handler.class, ComputercartInventoryUpdate.class, id++, Side.CLIENT);
		channel.registerMessage(EntitySyncRequest.Handler.class, EntitySyncRequest.class, id++, Side.SERVER);
		channel.registerMessage(EntitySyncData.Handler.class, EntitySyncData.class, id++, Side.CLIENT);
		channel.registerMessage(UpdateRunning.Handler.class, UpdateRunning.class, id++, Side.CLIENT);
		channel.registerMessage(ItemUseMessage.Handler.class, ItemUseMessage.class, id++, Side.CLIENT);
		channel.registerMessage(ConfigSyncMessage.Handler.class, ConfigSyncMessage.class, id++, Side.CLIENT);
	}
	
	public static void sendToNearPlayers(IMessage msg, TileEntity entity){
		sendToNearPlayers(msg,entity.xCoord, entity.yCoord, entity.zCoord, entity.getWorldObj());		
	}
	
	public static void sendToNearPlayers(IMessage msg, double x, double y, double z, World world){
		ServerConfigurationManager manager = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		Iterator playerList = manager.playerEntityList.iterator();
		while(playerList.hasNext()){
			EntityPlayerMP player = (EntityPlayerMP) playerList.next();
			int serverview = manager.getViewDistance()*16;
			if(player.getDistance(x, y, z)<=serverview && world.provider.dimensionId == player.dimension){
				channel.sendTo(msg, player);
			}
		}
	}
}
