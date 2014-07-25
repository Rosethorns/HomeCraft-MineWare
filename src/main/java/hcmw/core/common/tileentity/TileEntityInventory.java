package hcmw.core.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityInventory extends TileEntityBase implements IInventory {

    private ItemStack[] chestContents = new ItemStack[36];
    private String customName;

    @Override
    public int getSizeInventory() {
        return 27; //TODO constructor param?
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.chestContents[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.chestContents[slot] != null) {
            ItemStack itemstack;

            if (this.chestContents[slot].stackSize <= amount) {
                itemstack = this.chestContents[slot];
                this.chestContents[slot] = null;
                this.markDirty();
                return itemstack;
            }
            else {
                itemstack = this.chestContents[slot].splitStack(amount);

                if (this.chestContents[slot].stackSize == 0) {
                    this.chestContents[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.chestContents[slot] != null) {
            ItemStack itemstack = this.chestContents[slot];
            this.chestContents[slot] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        this.chestContents[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.chest";
    }

    public boolean hasCustomInventoryName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String name) {
        this.customName = name;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagList nbttaglist = tagCompound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (tagCompound.hasKey("CustomName", 8)) {
            this.customName = tagCompound.getString("CustomName");
        }

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound slotTag = nbttaglist.getCompoundTagAt(i);
            int j = slotTag.getByte("Slot") & 255;

            if (j >= 0 && j < this.chestContents.length) {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(slotTag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.chestContents.length; ++i) {
            if (this.chestContents[i] != null) {
                NBTTagCompound slotTag = new NBTTagCompound();
                slotTag.setByte("Slot", (byte) i);
                this.chestContents[i].writeToNBT(slotTag);
                nbttaglist.appendTag(slotTag);
            }
        }

        tagCompound.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            tagCompound.setString("CustomName", this.customName);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
        this.markDirty();
    }

    @Override
    public void closeInventory() {
        this.markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
    }
}
