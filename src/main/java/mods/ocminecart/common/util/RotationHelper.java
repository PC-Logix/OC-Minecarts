package mods.ocminecart.common.util;

import net.minecraft.util.EnumFacing;

public class RotationHelper {
	
	public static EnumFacing calcLocalDirection(EnumFacing value, EnumFacing face){
		int n = face.getHorizontalIndex();
		int d = value.getHorizontalIndex();
		if(n<0 || d<0) return value;
		return EnumFacing.getHorizontal((d+n+4)%4);
	}
	
	public static EnumFacing calcGlobalDirection(EnumFacing value, EnumFacing face){
		int n = face.getHorizontalIndex();
		int d = value.getHorizontalIndex();
		if(n<0 || d<0) return value;
		return EnumFacing.getHorizontal((d-n+4)%4);
	}
	
	//http://jabelarminecraft.blogspot.co.at/p/minecraft-forge-172-finding-block.html
	public static EnumFacing directionFromYaw(double yaw){
		yaw+=44.5;
		yaw = (yaw+360)%360;
		int di = (int) Math.floor((yaw * 4.0D / 360D) + 0.5D);
		di=(di+4)%4;
		return EnumFacing.getHorizontal(di);
	}
	
	public static double calcAngle(double x1, double z1, double x2, double z2){
		double dx = x1-x2;
		double dy = z1-z2;
		return (Math.atan2(dy, dx)*180D)/Math.PI;
	}
}
