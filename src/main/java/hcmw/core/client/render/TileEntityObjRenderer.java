package hcmw.core.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.core.common.block.IDirectional;
import hcmw.core.common.tileentity.TileEntityBase;
import hcmw.core.proxy.ProxyClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityObjRenderer extends TileEntitySpecialRenderer {

    protected final ResourceLocation texLoc;
    protected final WavefrontObject model;
    protected int displayListID = 0;

    public TileEntityObjRenderer() {
        this.texLoc = null;
        this.model = null;
    }

    public TileEntityObjRenderer(ResourceLocation model, ResourceLocation texLoc) {
        this.texLoc = texLoc;
        this.model = (WavefrontObject) AdvancedModelLoader.loadModel(model);
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
        checkDisplayList();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        Block block = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        if (block instanceof IDirectional) {
            switch (ForgeDirection.getOrientation(((IDirectional) block).getDirectionOppositeFacing(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord))) {
                //South
                case SOUTH: {
                    GL11.glTranslated(x + 1D, y, z);
                    GL11.glRotatef(180F, 0F, 1F, 0F);
                    break;
                }
                //West
                case WEST: {
                    GL11.glTranslated(x + 1D, y, z + 1D);
                    GL11.glRotatef(90F, 0F, 1F, 0F);
                    break;
                }
                //North
                case NORTH: {
                    GL11.glTranslated(x, y, z + 1D);
                    break;
                }
                //East
                case EAST: {
                    GL11.glTranslated(x, y, z);
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
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCallList(displayListID);
        GL11.glPopMatrix();
    }

    protected void checkDisplayList() {
        //ID is 0 if unset or it failed to create to create list
        if (this.displayListID == 0 && model != null) {
            this.displayListID = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.displayListID, GL11.GL_COMPILE);
            Minecraft.getMinecraft().renderEngine.bindTexture(this.texLoc);
            this.model.renderAll();
            GL11.glEndList();
        }
    }
}
