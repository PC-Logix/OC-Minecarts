package mods.ocminecart.network.message;

import io.netty.buffer.ByteBuf;
import mods.ocminecart.common.items.ItemCartRemoteModule;
import mods.ocminecart.common.items.ItemRemoteAnalyzer;
import mods.ocminecart.common.items.ModItems;
import mods.ocminecart.network.SynchronizedMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemUseMessage implements IMessage {
	
	int id;
	int pentid;
	NBTTagCompound data;
	
	public ItemUseMessage(){}
	
	public ItemUseMessage(int id, int pentid, NBTTagCompound data){
		this.id = id;
		this.pentid = pentid;
		this.data = (data==null) ? new NBTTagCompound() : data;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.id = buf.readInt();
		this.pentid = buf.readInt();
		this.data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.id);
		buf.writeInt(this.pentid);
		ByteBufUtils.writeTag(buf, this.data);
	}
	
	public static class Handler extends SynchronizedMessageHandler<ItemUseMessage> {

		@Override
		protected void handleMessage(ItemUseMessage message, MessageContext ctx) {
			Entity p= Minecraft.getMinecraft().theWorld.getEntityByID(message.pentid);
			if(!(p instanceof EntityPlayer)) return;
			switch(message.id){
				case 0:
					((ItemCartRemoteModule)ModItems.item_CartRemoteModule).onMPUsage((EntityPlayer)p, message.data);
				case 1:
					((ItemRemoteAnalyzer)ModItems.item_CartRemoteAnalyzer).onMPUsage((EntityPlayer)p, message.data);
			}
		}
	}

}
