package mods.ocminecart.network;

import mods.ocminecart.OCMinecart;
import mods.ocminecart.network.message.*;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork {
	
	public static SimpleNetworkWrapper channel;
	
	public static void init(){
		channel= NetworkRegistry.INSTANCE.newSimpleChannel(OCMinecart.MODID.toLowerCase());
		int id=-1;
		
		channel.registerMessage(GuiButtonClick.Handler.class, GuiButtonClick.class, id++, Side.SERVER);
		channel.registerMessage(ComputercartInventoryUpdate.Handler.class, ComputercartInventoryUpdate.class, id++, Side.CLIENT);
		channel.registerMessage(EntitySyncRequest.Handler.class, EntitySyncRequest.class, id++, Side.SERVER);
		channel.registerMessage(EntitySyncData.Handler.class, EntitySyncData.class, id++, Side.CLIENT);
		channel.registerMessage(TileSyncRequest.Handler.class, TileSyncRequest.class, id++, Side.SERVER);
		channel.registerMessage(TileSyncResponse.Handler.class, TileSyncResponse.class, id++, Side.CLIENT);
		channel.registerMessage(UpdateRunning.Handler.class, UpdateRunning.class, id++, Side.CLIENT);
		channel.registerMessage(ItemUseMessage.Handler.class, ItemUseMessage.class, id++, Side.CLIENT);
		channel.registerMessage(ConfigSyncMessage.Handler.class, ConfigSyncMessage.class, id++, Side.CLIENT);
	}

	public static void sendToNearPlayers(IMessage msg, BlockPos pos, World world){
		sendToNearPlayers(msg, pos.getX(), pos.getY(), pos.getZ(), world);
	}

	public static void sendToNearPlayers(IMessage msg, double x, double y, double z, World world){
		PlayerList manager = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
		int serverview = manager.getViewDistance()*16;
		channel.sendToAllAround(msg, new NetworkRegistry.TargetPoint(world.provider.getDimension(), x, y, z, serverview+1));
	}
}
