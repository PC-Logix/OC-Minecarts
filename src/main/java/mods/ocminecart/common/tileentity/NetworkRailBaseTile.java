package mods.ocminecart.common.tileentity;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.*;
import mods.ocminecart.Settings;
import mods.ocminecart.common.util.IPlugable;
import mods.ocminecart.common.util.Plug;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class NetworkRailBaseTile extends TileEntity implements ITickable,SidedEnvironment, IPlugable, Analyzable{
	
	
	private Plug rail;	//Environment for the Cart
	private Plug side;	//Environment for Cables, Computers, ...
	
	private boolean firstupdate = true; //First call of updateEntity()
	
	private ItemStack camoItem = null;
	private ItemStack camoItemOld = null;
	
	/*
	 * 0 = Connect: Network,Power
	 * 1 = Connect: Network
	 * 2 = Connect: Power
	 */
	private int Mode = 0;
	private boolean moving=false;
	
	public NetworkRailBaseTile(){
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()){
			rail = new Plug(this);
			side = new Plug(this);
			
			rail.setNode(Network.newNode(rail, Visibility.Network).withConnector().create());
			side.setNode(Network.newNode(side,Visibility.Network).withConnector(500D).create());
		}
		this.markDirty();
	}
	
	/*---------NBT/Sync--------*/
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("conMode")) Mode = nbt.getInteger("conMode");
		
		camoItem=ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("CamoItem"));
		
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()){
			 if(nbt.hasKey("Plug_1"))side.load((NBTTagCompound) nbt.getTag("Plug_1"));
			 if(nbt.hasKey("Plug_2"))rail.load((NBTTagCompound) nbt.getTag("Plug_2"));
		}
		
		this.markDirty();
	}
		
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("conMode", Mode);
		
		if(!this.worldObj.isRemote){
			NBTTagCompound plug1 = new NBTTagCompound();
			NBTTagCompound plug2 = new NBTTagCompound();
			
			side.save(plug1);
			nbt.setTag("Plug_1",plug1);
			
			rail.save(plug2);
			nbt.setTag("Plug_2", plug2);
		}
		
		NBTTagCompound item = new NBTTagCompound();
		if(camoItem!=null)camoItem.writeToNBT(item);
		nbt.setTag("CamoItem",item);
		return nbt;
	}

	@Override
	public void update(){
		if (!this.worldObj.isRemote && !this.moving) {
			if(firstupdate){
				Network.joinOrCreateNetwork(this);
				Network.joinNewNetwork(this.rail.node());
				firstupdate=false;
			}
			
			if(Mode==0 || Mode ==2){
				Connector con1=(Connector)side.node();
				Connector con2=(Connector)rail.node();
				if(con2.globalBuffer()<con2.globalBufferSize()){
					double need = con2.globalBufferSize() - con2.globalBuffer();
					double provide = 0.0;
					
					if(need > Settings.NetRailPowerTransfer) need = Settings.NetRailPowerTransfer;
					
					provide = need + con1.changeBuffer(-need);
					con2.changeBuffer(provide);
				}
			}
			
		}
	}
	/*------END-NBT/Sync------*/
	
	/*------Tile-Update-------*/
	 
	public void onChunkUnload() {
		 super.onChunkUnload();
		 if(rail != null){
			 rail.node().remove();
		 }
		 if(side!=null){
			 side.node().remove();
		 }
	 }
	 
	public void invalidate() {
		 super.invalidate();
		 if(rail != null){
			 rail.node().remove();
		 }
		 if(side!=null){
			 side.node().remove();
		 }
	 }
	
	/*-----END-Tile-Update-----*/
	
	/*-----OC-Network------*/
	
	@Override
	public Node sidedNode(EnumFacing side) {
		if(this.worldObj!=null && !this.worldObj.isRemote && side!=null && !side.equals(EnumFacing.UP)) return this.side.node();
		return null;
	}
	
	@Override
	public boolean canConnect(EnumFacing side) {
		if(side!=null && !side.equals(EnumFacing.UP)) return true;
		return false;
	}

	@Override
	public void onPlugMessage(Plug plug, Message message) {
		if(Mode==0 || Mode==1){
			if(message.name()=="network.message" && this.side.node()!= message.source() && this.rail.node()!=message.source()){
				if(plug==rail) side.node().sendToReachable("network.message", message.data());
				else if(plug==side) rail.node().sendToReachable("network.message", message.data());
			}
		}
	}

	@Override
	public void onPlugConnect(Plug plug, Node node) {
	}

	@Override
	public void onPlugDisconnect(Plug plug, Node node) {
		if(plug == this.rail && rail.node().network()==null) Network.joinNewNetwork(this.rail.node());
	}

	public int getMode() {
		return this.Mode;
	}

	/*-----END-OC-Network----*/
	
	public void onButtonPress(int buttonID) {
		if(buttonID==0){
			this.Mode += 1;
			if(this.Mode > 3) this.Mode = 0;
		}
	}
	
	public void setMode(int Mode){ this.Mode=Mode; }
	
	public Environment getRailPlug(){
		return this.rail;
	}


	@Override
	public Node[] onAnalyze(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		return new Node[0];
	}
}