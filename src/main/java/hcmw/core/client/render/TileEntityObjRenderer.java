package hcmw.core.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.core.common.block.IDirectional;
import hcmw.core.common.tileentity.ICustomizable;
import hcmw.core.common.tileentity.TileEntityBase;
import hcmw.core.proxy.ProxyClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
//TODO optimisation anywhere?
public class TileEntityObjRenderer extends TileEntitySpecialRenderer {

    protected final RenderInfo renderInfo;

    public TileEntityObjRenderer(RenderInfo renderInfo) {
        this.renderInfo = renderInfo;
    }

    //This should not be overriden! It will pass on the rendering to the mapped TESR
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity != null) {
            TileEntityBase tileEntityBase = (TileEntityBase) tileEntity;
            TileEntityObjRenderer tesr = ProxyClient.tileEntityRenderers.get(tileEntityBase.getTileEntityRendererId());

            //If we have a mapping, pass the render on
            if (tesr != null) {
                tesr.renderTileEntityAt(tileEntityBase, x, y, z, partialTick);
            }
        }
    }

    /**
     * This does the actual tile entity rendering to prevent an infinite loop if super is called
     * @param tileEntity The tile entity
     * @param x The x
     * @param y The y
     * @param z The z
     * @param partialTick The partial tick
     */
    public void renderTileEntityAt(TileEntityBase tileEntity, double x, double y, double z, float partialTick) {
        GL11.glPushMatrix();
        Block block = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        if (block instanceof IDirectional) {
            switch (ForgeDirection.getOrientation(((IDirectional) block).getDirectionOppositeFacing(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord))) {
                case SOUTH: {
                    GL11.glTranslated(x, y, z + 1);
                    GL11.glRotatef(180F, 0F, 1F, 0F);
                    break;
                }
                case WEST: {
                    GL11.glTranslated(x, y, z);
                    GL11.glRotatef(90F, 0F, 1F, 0F);
                    break;
                }
                case NORTH: {
                    GL11.glTranslated(x + 1D, y, z);
                    break;
                }
                case EAST: {
                    GL11.glTranslated(x + 1, y, z + 1);
                    GL11.glRotatef(90F, 0F, -1F, 0F);
                    break;
                }
                default: {
                    GL11.glTranslated(x, y, z + 1);
                }
            }
        }
        else {
            GL11.glTranslated(x, y, z + 1);
        }

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //If model has multiple render passes
        if (tileEntity instanceof ICustomizable) {
            ICustomizable multiPartRender = (ICustomizable) tileEntity;
            for (int pass = 0; pass < this.renderInfo.getRenderPasses(); pass++) {
                //We pass a tesselator so it can all be added to one draw call per pass
                Tessellator tesselator = Tessellator.instance;
                float[] colour = multiPartRender.getRGBAForPass(pass);

                tesselator.startDrawing(GL11.GL_TRIANGLES);
                tesselator.setColorRGBA_F(colour[0], colour[1], colour[2], colour[3]);
                Minecraft.getMinecraft().renderEngine.bindTexture(this.renderInfo.getResourceLocForPass(pass));

                List<String> partsForPass = new ArrayList<String>();
                partsForPass.addAll(this.renderInfo.getPartsForPass(pass));
                partsForPass.addAll(multiPartRender.getPartsForPass(pass));
                this.renderInfo.getModel().tessellateOnly(tesselator, partsForPass.toArray(new String[partsForPass.size()]));

                tesselator.draw();
            }
        }
        else {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.renderInfo.getResourceLocForPass(0));
            this.renderInfo.getModel().renderAll();
        }

        //Reset GL stuff
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
