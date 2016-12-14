package mods.ocminecart.network.message;

import io.netty.buffer.ByteBuf;
import mods.ocminecart.common.items.ItemCartRemoteModule;
import mods.ocminecart.common.items.ItemRemoteAnalyzer;
import mods.ocminecart.common.items.ModItems;
import mods.ocminecart.network.SynchronizedMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemUseMessage implements IMessage {

	int pentid;
	EnumHand hand;
	NBTTagCompound data;
	
	public ItemUseMessage(){}
	
	public ItemUseMessage(int pentid, EnumHand hand, NBTTagCompound data){
		this.pentid = pentid;
		this.hand = hand;
		this.data = (data==null) ? new NBTTagCompound() : data;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pentid = buf.readInt();
		int handO = buf.readInt();
		if(handO<0 || handO>EnumHand.values().length)
			hand = null;
		else
			hand = EnumHand.values()[handO];
		this.data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.pentid);
		buf.writeInt(this.hand.ordinal());
		ByteBufUtils.writeTag(buf, this.data);
	}
	
	public static class Handler extends SynchronizedMessageHandler<ItemUseMessage> {

		@Override
		protected void handleMessage(ItemUseMessage message, MessageContext ctx) {
			Entity p= Minecraft.getMinecraft().theWorld.getEntityByID(message.pentid);
			if(!(p instanceof EntityPlayer) || message.hand == null) return;

			ItemStack held = ((EntityPlayer) p).getHeldItem(message.hand);
			if(held == null || !(held.getItem() instanceof IMPUsageItem))
				return;

			((IMPUsageItem) held.getItem()).onMPUsage((EntityPlayer) p, message.hand, message.data);
		}
	}

	public interface IMPUsageItem {

		void onMPUsage(EntityPlayer p, EnumHand hand, NBTTagCompound data);

	}

}
