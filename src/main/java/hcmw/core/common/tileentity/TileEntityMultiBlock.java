package hcmw.core.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityMultiBlock extends TileEntityBase {
    public BlockPos parentPos;

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
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("parentX", this.parentX);
        nbtTagCompound.setInteger("parentY", this.parentY);
        nbtTagCompound.setInteger("parentZ", this.parentZ);

        nbtTagCompound.setBoolean("hasParent", this.hasParent);
        nbtTagCompound.setBoolean("isParent", this.isParent);

        return nbtTagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
        this.worldObj.markBlockForUpdate(getPos());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    /**
     * Sets this TE as a parent
     */
    public void setIsParent() {
        this.isParent = true;
    }

    /**
     * Sets the parent for this TE
     * @param pos
     */
    public void setParent(@Nullable BlockPos pos) {
        if (pos != null) {
            parentPos = pos;
            hasParent = true;
            isParent = false;
        }
        else {
            parentPos = null;
            hasParent = false;
        }
    }

    /**
     * Sets the parent for this TE
     * @param tileEntity The parent TE
     */
    public void setParent(TileEntityMultiBlock tileEntity) {
        setParent(tileEntity.parentPos);
    }

    /**
     * Check parent is valid
     * @return If parent exists
     */
    public boolean isParentValid() {
        if (isParent) return true;
        if (hasParent) {
            TileEntity block = worldObj.getTileEntity(parentPos);
            if (block instanceof TileEntityMultiBlock) return true;
        }
        return false;
    }
}
