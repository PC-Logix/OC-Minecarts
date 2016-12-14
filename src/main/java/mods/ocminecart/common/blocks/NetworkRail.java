package mods.ocminecart.common.blocks;

import li.cil.oc.api.network.Environment;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.tileentity.NetworkRailBaseTile;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkRail extends BlockRailBase implements INetRail{

	public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.<BlockRailBase.EnumRailDirection>create("shape", BlockRailBase.EnumRailDirection.class, (railDirection) -> railDirection != EnumRailDirection.NORTH_EAST && railDirection != EnumRailDirection.NORTH_WEST && railDirection != EnumRailDirection.SOUTH_EAST && railDirection != EnumRailDirection.SOUTH_WEST);

	public NetworkRail() {
		super(false);
		this.setUnlocalizedName(OCMinecart.MODID+".networkrail");
		this.setHardness(0.7F);
	}

	@Override
	public Environment getResponseEnvironment(World world, BlockPos pos) {
		if(world.getTileEntity(pos.add(0,1,0)) instanceof NetworkRailBaseTile) return ((NetworkRailBaseTile) world.getTileEntity(pos.add(0,1,0))).getRailPlug();
		return null;
	}

	@Override
	public boolean isValid(World world, BlockPos pos, EntityMinecart cart) {
		return (cart instanceof Environment) && cart.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 0.5 && cart.worldObj == world;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(SHAPE).getMetadata();
		return i;
	}

	@Override
	public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty()
	{
		return SHAPE;
	}
}
