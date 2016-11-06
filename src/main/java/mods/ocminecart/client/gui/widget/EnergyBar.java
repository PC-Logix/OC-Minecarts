package mods.ocminecart.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EnergyBar {
	public static void drawBar(int posx, int posy, int h, int w,int zlevel, double percent, ResourceLocation bar){
		RenderHelper.disableStandardItemLighting();
		
		percent = (percent > 1) ? 1 : percent;
		
		double dw = w * percent;

		Tessellator tes = Tessellator.getInstance();
		VertexBuffer buffer = tes.getBuffer();
		Minecraft.getMinecraft().renderEngine.bindTexture(bar);
		
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(posx + dw, posy + h, zlevel).tex(percent, 1).endVertex();
		buffer.pos(posx + dw, posy, zlevel).tex(percent, 0).endVertex();
		buffer.pos(posx, posy, zlevel).tex(0, 0).endVertex();
		buffer.pos(posx, posy + h, zlevel).tex(0, 1).endVertex();
		tes.draw();
		
		RenderHelper.enableStandardItemLighting();
	}
}
