package mods.ocminecart.network.message;


import io.netty.buffer.ByteBuf;
import mods.ocminecart.network.ISyncTile;
import mods.ocminecart.network.SynchronizedMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TileSyncResponse implements IMessage{

    public int x;
    public int y;
    public int z;
    public int dim;
    public NBTTagCompound data;

    public TileSyncResponse(BlockPos pos, World w, NBTTagCompound data) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.dim = w.provider.getDimension();
        this.data = data;
    }

    public static TileSyncResponse create(TileEntity entity) {
        if(entity instanceof ISyncTile) {
            NBTTagCompound data = new NBTTagCompound();
            ((ISyncTile) entity).writeSyncData(data);
            return new TileSyncResponse(entity.getPos(), entity.getWorld(), data);
        }
        return null;
    }

    public static TileSyncResponse create(TileEntity entity, NBTTagCompound data) {
        if(entity instanceof ISyncTile) {
            return new TileSyncResponse(entity.getPos(), entity.getWorld(), data);
        }
        return null;
    }

    public TileSyncResponse(){}

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.dim = buf.readInt();
        this.data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.dim);
        ByteBufUtils.writeTag(buf, this.data);
    }

    public static class Handler extends SynchronizedMessageHandler<TileSyncResponse> {

        @Override
        protected void handleMessage(TileSyncResponse message, MessageContext ctx) {
            if(Minecraft.getMinecraft().thePlayer==null || Minecraft.getMinecraft().thePlayer.worldObj.provider.getDimension() != message.dim)
                return;
            World world = Minecraft.getMinecraft().theWorld;
            BlockPos pos = new BlockPos(message.x,message.y,message.z);
            if(world==null)
                return;
            TileEntity entity = world.getTileEntity(pos);
            if(entity==null || !(entity instanceof ISyncTile))
                return;
            ((ISyncTile) entity).readSyncData(message.data);
        }
    }
}
