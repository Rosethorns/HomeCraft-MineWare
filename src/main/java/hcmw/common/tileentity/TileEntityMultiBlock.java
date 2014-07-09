package hcmw.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMultiBlock extends TileEntityBase {

    public int parentX;
    public int parentY;
    public int parentZ;

    public boolean hasParent;
    public boolean isParent;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.parentX = nbtTagCompound.getInteger("parentX");
        this.parentY = nbtTagCompound.getInteger("parentY");
        this.parentZ = nbtTagCompound.getInteger("parentZ");

        this.hasParent = nbtTagCompound.getBoolean("hasParent");
        this.isParent = nbtTagCompound.getBoolean("isParent");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("parentX", this.parentX);
        nbtTagCompound.setInteger("parentY", this.parentY);
        nbtTagCompound.setInteger("parentZ", this.parentZ);

        nbtTagCompound.setBoolean("hasParent", this.hasParent);
        nbtTagCompound.setBoolean("isParent", this.isParent);
    }

    public void setIsParent() {
        this.isParent = true;
    }

    public void setParent(int parentX, int parentY, int parentZ) {
        this.parentX = parentX;
        this.parentY = parentY;
        this.parentZ = parentZ;

        this.hasParent = true;
        this.isParent = false;
    }

    public void setParent(TileEntityMultiBlock tileEntity) {
        setParent(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    @Override
    public String toString() {
        return String.valueOf(this.getClass()) + "[hasParent: " + this.hasParent + ", isParent: " + this.isParent + ", parentX: " + this.parentX + ", parentY: " + this.parentY + ", parentZ: " + this.parentZ + "]";
    }
}
