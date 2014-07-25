package hcmw.medieval.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.core.client.render.TileEntityObjRenderer;
import hcmw.core.common.tileentity.TileEntityBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityBarrelRenderer extends TileEntityObjRenderer {

    public TileEntityBarrelRenderer(ResourceLocation texLoc, ResourceLocation model) {
        super(texLoc, model);
    }

    @Override
    public void renderTileEntityAt(TileEntityBase tileEntity, double x, double y, double z, float partialTick) {
        super.checkDisplayList();

        if (tileEntity != null) {
            int meta = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            //If standard directions, render as normal
            if (meta < 8) super.renderTileEntityAt(tileEntity, x, y, z, partialTick);
            else {
                GL11.glPushMatrix();
                //Model is off by -one in z
                switch (ForgeDirection.getOrientation(meta - 8)) {
                    //South
                    case SOUTH: {
                        GL11.glTranslated(x, y + 1D, z + 1D);
                        GL11.glRotatef(90F, 1F, 0F, 0F);
                        GL11.glRotatef(180F, 0F, 1F, 0F);
                        GL11.glRotatef(180F, 0F, 0F, 1F);
                        break;
                    }
                    //West
                    case WEST: {
                        GL11.glTranslated(x, y + 1D, z);
                        GL11.glRotatef(90F, 1F, 0F, 0F);
                        GL11.glRotatef(180F, 0F, 1F, 0F);
                        GL11.glRotatef(90F, 0F, 0F, 1F);
                        break;
                    }
                    //North
                    case NORTH: {
                        GL11.glTranslated(x + 1D, y + 1D, z);
                        GL11.glRotatef(90F, 1F, 0F, 0F);
                        GL11.glRotatef(180F, 0F, 1F, 0F);
                        break;
                    }
                    //East
                    case EAST: {
                        GL11.glTranslated(x + 1D, y + 1D, z + 1D);
                        GL11.glRotatef(90F, -1F, 0F, 0F);
                        GL11.glRotatef(90F, 0F, 0F, 1F);
                        break;
                    }
                    default: {
                        GL11.glTranslated(x, y, z + 1);
                    }
                }
                GL11.glCallList(displayListID);
                GL11.glPopMatrix();
            }
        }
    }
}
