package mods.ocminecart.common.minecart;

import mods.ocminecart.Settings;
import mods.ocminecart.common.util.BitUtil;
import mods.ocminecart.interaction.railcraft.RailcraftUtils;
import mods.railcraft.client.emblems.EmblemToolsClient;
import mods.railcraft.common.blocks.charge.CartBattery;
import mods.railcraft.common.blocks.charge.ICartBattery;
import mods.railcraft.common.emblems.EmblemToolsServer;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;

//Is the Base for a solid, self powered cart with a brake.
//Also the CartBase for Railcraft Integration
public abstract class RailCart extends EntityMinecart{

	private static final DataParameter<Byte> FLAGS = EntityDataManager.createKey(Entity.class, DataSerializers.BYTE);
	private static final DataParameter<Float> SPEED = EntityDataManager.createKey(Entity.class, DataSerializers.FLOAT);
	private static final DataParameter<String> EMBLEM = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);

	private CartBattery charge;
	
	public RailCart(World p_i1713_1_, double p_i1713_2_, double p_i1713_4_,
                    double p_i1713_6_) {
		super(p_i1713_1_, p_i1713_2_, p_i1713_4_, p_i1713_6_);
	}

	public RailCart(World p_i1713_1) {
		super(p_i1713_1);
	}

	protected void entityInit() {
		super.entityInit();
		
		if(!this.getEntityWorld().isRemote)
			charge = new CartBattery(ICartBattery.Type.USER, Settings.ComputerCartETrackBuf, Settings.ComputerCartETrackLoss);
		
		this.getDataManager().register(FLAGS, (byte)0); // Booleans (is Locked, Brake enabled)
		this.getDataManager().register(SPEED, 0.0F);  //Engine speed
		this.getDataManager().register(EMBLEM, "");	//Emblem id [Railcraft]
	}
	
	protected final void setBrake(boolean b){ 
		this.getDataManager().set(FLAGS, BitUtil.setBit(b, this.getDataManager().get(FLAGS), 0));
	}
	protected final boolean getBrake(){ return BitUtil.getBit(this.getDataManager().get(FLAGS), 0); }
	protected final void setEngine(double d){ this.getDataManager().set(SPEED, (float)d); }
	protected final double getEngine(){ return this.getDataManager().get(SPEED); }
	public final boolean isLocked(){ return BitUtil.getBit(this.getDataManager().get(FLAGS), 1); }
	public final boolean isEngineActive(){ return this.getEngine()!=0 && !this.isLocked() && !this.getBrake() && this.onRail(); }
	
	public void writeEntityToNBT(NBTTagCompound nbt){
		super.writeEntityToNBT(nbt);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setDouble("enginespeed", this.getDataManager().get(SPEED));
		tag.setBoolean("brake", BitUtil.getBit(this.getDataManager().get(FLAGS), 0));
		if(Loader.isModLoaded("Railcraft")){
			NBTTagCompound rctag = new NBTTagCompound();
			rctag.setBoolean("locked", BitUtil.getBit(this.getDataManager().get(FLAGS), 1));
			if(this.charge!=null)
				RailcraftUtils.writeCharge(rctag, this.charge);
			String emblem = this.getDataManager().get(EMBLEM);
			if(emblem!=null && emblem.isEmpty()) rctag.setString("emblem_id", emblem);
			else rctag.removeTag("emblem_id");
			tag.setTag("railcraft", rctag);
		}
		nbt.setTag("advcart", tag);
	}
	
	public void readEntityFromNBT(NBTTagCompound nbt){
		super.readEntityFromNBT(nbt);
		if(nbt.hasKey("advcart")){
			NBTTagCompound tag = (NBTTagCompound) nbt.getTag("advcart");
			if(tag.hasKey("enginespeed")) this.getDataManager().set(SPEED, (float)tag.getDouble("enginespeed"));
			if(tag.hasKey("brake")) 
				this.getDataManager().set(FLAGS, BitUtil.setBit(tag.getBoolean("brake"), this.getDataManager().get(FLAGS), 0));
			if(tag.hasKey("railcraft") && Loader.isModLoaded("Railcraft")){
				NBTTagCompound rctag = tag.getCompoundTag("railcraft");
				this.getDataManager().set(FLAGS, BitUtil.setBit(rctag.getBoolean("locked"), this.getDataManager().get(FLAGS), 1));
				if(this.charge!=null)
					RailcraftUtils.readCharge(rctag, charge);
				if(rctag.hasKey("emblem_id")){
					String id= rctag.getString("emblem_id");
					this.getDataManager().set(EMBLEM, id);
				}
			}
		}
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		this.setDead();
		ItemStack itemstack = this.getCartItem();

		if (!this.getCustomNameTag().isEmpty()) {
			itemstack.setStackDisplayName(this.getCustomNameTag());
		}

		this.entityDropItem(itemstack, 0.0F);
	}

	@Override
	public Type getType() {
		return null;
	}

	public boolean onRail() {
		return BlockRailBase.isRailBlock(this.worldObj, this.getPosition());
	}

	public void onUpdate() {
		super.onUpdate();
		if (this.worldObj.isRemote) return;
		
		if(charge!=null){
			this.charge.tick(this);
			double mv = this.addEnergy(this.charge.getCharge() * Settings.OC_IC2PWR, true);  //Get max. energy we can load to the node
			mv = Math.min(mv, Settings.ComputerCartETrackLoad * Settings.OC_IC2PWR); //Check if the movable energy is higher than the limit.
			mv = this.charge.removeCharge(mv / Settings.OC_IC2PWR) * Settings.OC_IC2PWR; //Remove the charge from the buffer
			this.addEnergy(mv , false);	//Add the removed energy to the node network
		}
	}

	@Override
	protected void moveAlongTrack(BlockPos pos, IBlockState state) {
		super.moveAlongTrack(pos, state);
		if (this.worldObj.isRemote) return;
		if(charge!=null){
			this.charge.tickOnTrack(this, pos);
		}
	}

    @Override
    protected void applyDrag() {
        if(!(BitUtil.getBit(this.getDataManager().get(FLAGS), 0) || BitUtil.getBit(this.getDataManager().get(FLAGS), 1))){
			this.motionX *= 0.9699999785423279D;
			this.motionY *= 0.0D;
        	this.motionZ *= 0.9699999785423279D;
        	
        	if(this.getDataManager().get(SPEED)!=0){
        		double yaw = this.rotationYaw * Math.PI / 180.0;
        		
        		this.motionX += Math.cos(yaw) * 10;
        		this.motionZ += Math.sin(yaw) * 10;
        		
        		double nMotionX = Math.min( Math.abs(this.motionX) , this.getDataManager().get(SPEED));
        		double nMotionZ = Math.min( Math.abs(this.motionZ) , this.getDataManager().get(SPEED));
        		
        		if(this.motionX < 0) this.motionX = - nMotionX;
        		else this.motionX = nMotionX;
        		
        		if(this.motionZ < 0) this.motionZ = - nMotionZ;
        		else this.motionZ = nMotionZ;
        	}
        	
        	//Stop the cart if there is no speed. (below 0.0001 there are only sounds and no movement)
        	if(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) < 0.0001){
        		this.motionX = 0;
            	this.motionZ = 0;
        	}
        }
        else if(!BitUtil.getBit(this.getDataManager().get(FLAGS), 1)){
        	this.motionX = 0;
        	this.motionZ = 0;
        	this.setPosition(this.lastTickPosX, this.posY, this.lastTickPosZ);  // Fix: Bug on Booster Tracks (Reset Position)
        }
    }
    
    public double getSpeed(){
    	return Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    }

	public boolean canBePushed() {
		return (!BitUtil.getBit(this.getDataManager().get(FLAGS),0) || !onRail());
	}
	
	protected abstract double addEnergy(double amount, boolean simulate);

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == RailcraftUtils.CHARGE_CART_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == RailcraftUtils.CHARGE_CART_CAPABILITY)
		{
			return (T) charge;
		}
		return super.getCapability(capability, facing);
	}

	/*-------Railcraft-------*/
	public void lockdown(boolean lock){
		if(lock != BitUtil.getBit(this.getDataManager().get(FLAGS), 1))
			this.getDataManager().set(FLAGS, BitUtil.setBit(lock, this.getDataManager().get(FLAGS), 1));
	}

	public CartBattery getChargeBattery()
	{
		return charge;
	}
	
	public boolean setEmblem(ItemStack stack){
		//if(!Loader.isModLoaded("Railcraft")) return false;
		return setEmblem(EmblemToolsServer.getEmblemIdentifier(stack));
	}
	
	public boolean setEmblem(String emblem){
		//if(!Loader.isModLoaded("Railcraft")) return false;
		if(emblem==null) emblem="";
		if(emblem.equals(this.getDataManager().get(EMBLEM))) return false;
		this.getDataManager().set(EMBLEM, emblem);
		return true;
	}
	
	public String getEmblem(){
		//if(!Loader.isModLoaded("Railcraft")) return null;
		return this.getDataManager().get(EMBLEM);
	}

	public ResourceLocation getEmblemIcon(){
		String id = this.getDataManager().get(EMBLEM);
		if(id.isEmpty()) return null;
		return EmblemToolsClient.packageManager.getEmblemTextureLocation(id);
	}

}
