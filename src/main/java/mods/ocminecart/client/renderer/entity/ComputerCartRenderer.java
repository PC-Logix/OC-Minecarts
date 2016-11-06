package mods.ocminecart.client.renderer.entity;

import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.minecart.ComputerCart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class ComputerCartRenderer extends Render<ComputerCart> {
	
	private static final double EMBLEM_BX  = 0.5001;
	private static final double EMBLEM_X  = 0.5002;
	
	private static final ResourceLocation minecartTextures = new ResourceLocation(OCMinecart.MODID+":textures/entity/computercart.png");
	private static final ResourceLocation emblem_back = new ResourceLocation(OCMinecart.MODID+":textures/entity/computercart_eback.png");
	protected ComputerCartModel modelMinecart = new ComputerCartModel();

	public ComputerCartRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull  ComputerCart cart, double x, double y, double z, float yaw, float pitch) {
		
		GL11.glPushMatrix();
        this.bindEntityTexture(cart);
        
        /*double cx= cart.lastTickPosX + (cart.posX - cart.lastTickPosX) * (double)p_76986_9_;
        double cy= cart.lastTickPosY + (cart.posY - cart.lastTickPosY) * (double)p_76986_9_;
        double cz= cart.lastTickPosZ + (cart.posZ - cart.lastTickPosZ) * (double)p_76986_9_;
        double d6 = 0.30000001192092896D;*/
        double ryaw = (cart.rotationYaw+360D)%360;
        
        /*double yaw=p_76986_8_;
        float pitch = cart.rotationPitch;
        
        Vec3d vec1 = cart.func_70495_a(cx,cy,cz, d6);
        Vec3d vec2 = cart.func_70495_a(cx,cy,cz, -d6);
        
        if(vec1!=null && vec2!=null){
             y += (vec1.yCoord + vec2.yCoord) / 2.0D - cy;
             Vec3 vec3 = vec2.addVector(-vec1.xCoord, -vec1.yCoord, -vec1.zCoord);
             if(vec3.lengthVector()!=0){
            	 yaw = (float)(Math.atan2(vec3.zCoord, vec3.xCoord) * 180.0D / Math.PI);
            	 pitch = (float)(Math.atan(vec3.yCoord) * 73.0D);
             }
        }*/
        
        yaw= (float) ((yaw+360D)%360D);
        ryaw=yaw-ryaw;
        if(ryaw<=-90 || ryaw>=90){
        	yaw+=180D;
        	pitch*=-1;
        }
        yaw=90F-yaw;
        
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotated(yaw, 0.0D, 1.0D, 0.0D);
        GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
        float rollamp = (float)cart.getRollingAmplitude() - pitch;
        float dmgamp = cart.getDamage() - pitch;
        
        if (dmgamp < 0.0F)
        	dmgamp = 0.0F;

        if (rollamp > 0.0F)
        {
            GL11.glRotatef((float) (Math.sin(rollamp) * rollamp * dmgamp / 10.0F * (float)cart.getRollingDirection()), 0.0F, 0.0F, 1.0F);
        }
        
        GL11.glColor3f(1, 1, 1);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.renderTile(cart, 0.0625F);
        
        GL11.glRotated(90D, 0.0D, 1.0D, 0.0D);
        
        ResourceLocation emblem = cart.getEmblemIcon();
        
        if(emblem!=null){
        	Tessellator tes = Tessellator.instance;
        	Minecraft.getMinecraft().renderEngine.bindTexture(emblem_back);	//Render the emblem Background.
    
        	tes.startDrawingQuads();
        	tes.addVertexWithUV((3D/16D)+(6D/16D), (5D/16D), -EMBLEM_BX, 0, 1);
        	tes.addVertexWithUV((3D/16D)+(6D/16D) ,0, -EMBLEM_BX, 0, 0);
        	tes.addVertexWithUV((3D/16D), 0, -EMBLEM_BX, 1, 0);
        	tes.addVertexWithUV((3D/16D), (5D/16D), -EMBLEM_BX, 1, 1);
        	tes.draw();
        
        	tes.startDrawingQuads();
        	tes.addVertexWithUV((3D/16D), (5D/16D), EMBLEM_BX, 1, 1);
        	tes.addVertexWithUV((3D/16D), 0, EMBLEM_BX, 1, 0);
        	tes.addVertexWithUV((3D/16D)+(6D/16D) ,0, EMBLEM_BX, 0, 0);
        	tes.addVertexWithUV((3D/16D)+(6D/16D), (5D/16D), EMBLEM_BX, 0, 1);
        	tes.draw();
        	
        	Minecraft.getMinecraft().renderEngine.bindTexture(emblem);	//Render the actual emblem
        	
        	tes.startDrawingQuads();
        	tes.addVertexWithUV((4D/16D)+(5D/16D), (5D/16D), -EMBLEM_X, 1, 1);
        	tes.addVertexWithUV((4D/16D)+(5D/16D) ,0, -EMBLEM_X, 1, 0);
        	tes.addVertexWithUV((4D/16D), 0, -EMBLEM_X, 0, 0);
        	tes.addVertexWithUV((4D/16D), (5D/16D), -EMBLEM_X, 0, 1);
        	tes.draw();
        
        	tes.startDrawingQuads();
        	tes.addVertexWithUV((4D/16D), (5D/16D), EMBLEM_X, 1, 1);
        	tes.addVertexWithUV((4D/16D), 0, EMBLEM_X, 1, 0);
        	tes.addVertexWithUV((4D/16D)+(5D/16D) ,0, EMBLEM_X, 0, 0);
        	tes.addVertexWithUV((4D/16D)+(5D/16D), (5D/16D), EMBLEM_X, 0, 1);
        	tes.draw();
        }
        
        GL11.glPopMatrix();
	}


	@Override
	protected @Nonnull  ResourceLocation getEntityTexture(@Nonnull ComputerCart p_110775_1_) {
		return minecartTextures;
	}

}
