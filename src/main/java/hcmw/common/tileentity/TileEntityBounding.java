package hcmw.common.tileentity;

import net.minecraft.tileentity.TileEntity;

/**
 * A basic bounding tile entity that passes on most calls to it's parent instead
 * TODO implement most of that stuff
 */
public class TileEntityBounding extends TileEntityBase {

    public double parentX = 0;
    public double parentY = 0;
    public double parentZ = 0;

    public void setParent(double parentX, double parentY, double parentZ) {
        this.parentX = parentX;
        this.parentY = parentY;
        this.parentZ = parentZ;
    }

    public void setParent(TileEntity tileEntity) {
        this.parentX = tileEntity.xCoord;
        this.parentY = tileEntity.yCoord;
        this.parentZ = tileEntity.zCoord;
    }
}
