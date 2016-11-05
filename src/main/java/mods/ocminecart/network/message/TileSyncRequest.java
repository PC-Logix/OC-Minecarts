package mods.ocminecart.network.message;

import io.netty.buffer.ByteBuf;
import mods.ocminecart.network.ISyncTile;
import mods.ocminecart.network.ModNetwork;
import mods.ocminecart.network.SynchronizedMessageHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TileSyncRequest implements IMessage {

    public int x;
    public int y;
    public int z;
    public int dim;

    public TileSyncRequest(BlockPos pos, World w){
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.dim = w.provider.getDimension();
    }

    public static TileSyncRequest create(TileEntity entity) {
        if(entity instanceof ISyncTile) {
            return new TileSyncRequest(entity.getPos(), entity.getWorld());
        }
        return null;
    }

    public TileSyncRequest(){}

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.dim = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.dim);
    }

    public static class Handler extends SynchronizedMessageHandler<TileSyncRequest> {

        @Override
        protected void handleMessage(TileSyncRequest message, MessageContext ctx) {
            if(ctx.getServerHandler().playerEntity==null || ctx.getServerHandler().playerEntity.worldObj.provider.getDimension() != message.dim)
                return;
            World world = DimensionManager.getWorld(message.dim);
            BlockPos pos = new BlockPos(message.x,message.y,message.z);
            if(world==null || pos==null)
                return;
            TileEntity entity = world.getTileEntity(pos);
            if(entity==null || !(entity instanceof ISyncTile))
                return;
            NBTTagCompound data = new NBTTagCompound();
            ((ISyncTile) entity).writeSyncData(data);
            ModNetwork.channel.sendTo(new TileSyncResponse(pos,world,data), ctx.getServerHandler().playerEntity);
        }
    }
}
