package mods.ocminecart.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ImageButton extends GuiButton{
	
	boolean isToggleButton;
	boolean toggle;
	ResourceLocation texture;
	
	public ImageButton(int id, int posx, int posy, int width, int height, String text, ResourceLocation texture , boolean isToggleButton) {
		super(id, posx, posy, width, height, text);
		this.isToggleButton=isToggleButton;
		this.texture=texture;
		this.toggle=false;
	}
	
	public ImageButton(int id, int posx, int posy, int width, int height, ResourceLocation texture, String text) {
		super(id, posx, posy, width, height, text);
		this.isToggleButton=false;
		this.texture=texture;
		this.toggle=false;
	}
	
	public void drawButton(Minecraft minecraft, int mx, int my)
    {
        if (this.visible)
        {
            minecraft.renderEngine.bindTexture(this.texture);
           
            this.hovered = (mx<=this.xPosition+this.width) && (mx>=this.xPosition) && (my<=this.yPosition+this.height) && (my>=this.yPosition);

            Tessellator tes = Tessellator.getInstance();
            VertexBuffer buffer = tes.getBuffer();
           
            double v0 = (this.hovered && this.enabled) ? 0.5 : 0;
            double v1 = v0 + 0.5;
            double u0 = (this.toggle) ? 0.5 : 0;
            double u1 =  u0 +((this.isToggleButton) ? 0.5 : 1);
           
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(this.xPosition, this.yPosition + this.height, this.zLevel).tex(u0, v1);
            buffer.pos(this.xPosition + this.width, this.yPosition + this.height , this.zLevel).tex(u1, v1);
            buffer.pos(this.xPosition + this.width, this.yPosition , this.zLevel).tex(u1, v0);
            buffer.pos(this.xPosition, this.yPosition , this.zLevel).tex(u0, v0);
            tes.draw();
           
            if(this.displayString!=null && this.displayString!=""){
        	   int color = (!this.enabled) ? 0xA0A0A0 : ((this.isMouseOver()) ? 0xFFFFA0 : 0xE0E0E0);
        	   this.drawCenteredString(minecraft.fontRendererObj, this.displayString, this.xPosition+this.width/2, this.yPosition+(this.height-8)/2, color);
            }
        }
    }
	
	public void setToggle(boolean toggle){
		this.toggle=toggle;
	}
	
	public boolean getToggle(){
		return this.toggle;
	}
	

}
