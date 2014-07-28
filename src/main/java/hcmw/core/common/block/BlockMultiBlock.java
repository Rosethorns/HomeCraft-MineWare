package hcmw.core.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.HCMWCore;
import hcmw.core.common.tileentity.TileEntityBounding;
import hcmw.core.common.tileentity.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

//TODO less loops plz
public abstract class BlockMultiBlock extends BlockContainer implements IDirectional {

    //TODO should we store bed data per tile entity or create a new block for each one
    /**
     * This is the dimensions for the structure from the bottom-left-front block facing where it is placed. The coords
     * will be refered as such based on its facing.
     * First is x co-ord is width from the parent block to the right
     * Second is y which is height
     * Third is z which is depth from the parent block to the right
     */
    protected float[] boundingBoxMax = new float[3];

    /**
     * A list of collision box co-ordinates used by this block that are an array of floats.
     * Position is relative to the block so 0, 0, 0 is the bottom, left, front corner
     * First index is min x (width)
     * Second index is min y (height)
     * Third index is min z (depth)
     * Forth index is max x (width)
     * Fifth index is max y (height)
     * Sixth index is max z (depth)
     */
    public List<float[]> collisionBoxes;

    protected BlockMultiBlock(Material material) {
        super(material);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            //Revalidate the structure
            TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(x, y, z);

            //Only make the parent recheck, children are basically dummies
            if (tileEntity.hasParent && !tileEntity.isParent) {
                //If the structure is no longer valid, break. Block metadata is same as parents
                if (!this.isStructureValid(world, tileEntity.parentX, tileEntity.parentY, tileEntity.parentZ, tileEntity.getBlockMetadata())) {
                    //Destroy structure
                    if (!this.destroyStructure(world, tileEntity.parentX, tileEntity.parentY, tileEntity.parentZ, tileEntity.getBlockMetadata())) {
                        HCMWCore.logger.warn("Failed to properly delete mutliblock structure, this is very bad!");
                    }
                }
            }
        }
    }

    /**
     * Checks if a structure can be created at the defined location
     * @param world The world
     * @param x The parent x
     * @param y The parent y
     * @param z The parent z
     * @param facing The direction it is facing
     * @return Whether it can be placed here
     */
    public boolean canPlaceAtLocFacing(World world, int x, int y, int z, int facing) {
        return this.multiBlockLoop(world, x, y, z, facing, 0);
    }

    /**
     * Creates a bounding box structure at this location
     * @param world The world
     * @param x The parent x
     * @param y The parent y
     * @param z The parent z
     * @param facing The direction it is facing
     * @return Whether it successfully created the stucture
     */
    public boolean createStructure(World world, int x, int y, int z, int facing) {
        return this.multiBlockLoop(world, x, y, z, facing, 1);
    }

    /**
     * Checks if the current structure is still valid ie not missing children
     * @param world The world
     * @param x The parent x
     * @param y The parent y
     * @param z The parent z
     * @param facing The direction it is facing
     * @return Whether the structure is valid
     */
    public boolean isStructureValid(World world, int x, int y, int z, int facing) {
        return this.multiBlockLoop(world, x, y, z, facing, 2);
    }

    /**
     * Removes the structure and drops the parent block
     * @param world The world
     * @param x The parent x
     * @param y The parent y
     * @param z The parent z
     * @param facing The direction it is facing
     * @return Whether it was successfully removed
     */
    public boolean destroyStructure(World world, int x, int y, int z, int facing) {
        return this.multiBlockLoop(world, x, y, z, facing, 3);
    }

    /**
     * To save copy pasting looping code, this method exists. It basically loops through what would be the multiblock
     * structure and performs a certain task as passed in. This is probably not a good way of doing it but the best I can
     * come up with for now
     * @param world The world
     * @param x The parent x
     * @param y The parent y
     * @param z The parent z
     * @param facing The direction it is facing
     * @param taskID The task to perform.
     *             0 == check if can be placed
     *             1 == create structure
     *             2 == check if structure is still valid after creation
     *             3 == remove structure and parent as block
     * @return What the task returns
     */
    private boolean multiBlockLoop(World world, int x, int y, int z, int facing, int taskID) {
        float[] boundingBoxMax = this.boundingBoxMax;
        if (world.getBlock(x, y, z) instanceof BlockMultiBlock) {
            BlockMultiBlock parentBlock = (BlockMultiBlock) world.getBlock(x, y, z);
            boundingBoxMax = parentBlock.boundingBoxMax;
        }
        
        ForgeDirection dir = ForgeDirection.getOrientation(facing);
        
        int xSize = (int) ((boundingBoxMax[0] * dir.offsetX) + (boundingBoxMax[2] * dir.offsetZ));
        int ySize = (int) boundingBoxMax[1];
        int zSize = (int) ((boundingBoxMax[2] * dir.offsetX) + (boundingBoxMax[0] * dir.offsetZ));
        xSize = (xSize < 0 ? -xSize : xSize);
        zSize = (zSize < 0 ? -zSize : zSize);
        
        int xOffset;
        int zOffset;
        
        switch (dir) {
            case NORTH:
                xOffset = 0;
                zOffset = -zSize + 1;
                break;
            case SOUTH:
                xOffset = -xSize + 1;
                zOffset = 0;
                break;
            case WEST:
                zOffset = -zSize + 1;
                xOffset = -xSize + 1;
                break;
            case EAST:
                xOffset = 0;
                zOffset = 0;
                break;
            default:
                HCMWCore.logger.warn("Multi-block at X:" + x + " Y:" + y + " Z:" + z + " has invalid rotation " + dir.name() + ".");
                return false;
        }
        
        for (int checkX = x + xOffset; checkX < x + xSize + xOffset; checkX++) {
            for (int checkY = y; checkY < y + ySize; checkY++) {
                for (int checkZ = z + zOffset; checkZ < z + zSize + zOffset; checkZ++) {
                    if (!doTask(world, x, y, z, facing, checkX, checkY, checkZ, taskID)) return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Does the various tasks for {@link #multiBlockLoop(net.minecraft.world.World, int, int, int, int, int)}
     * @param world The world
     * @param parentX The parent x pos
     * @param parentY The parent y pos
     * @param parentZ The parent z pos
     * @param posX The target x pos
     * @param posY The target y pos
     * @param posZ The target z pos
     * @param taskID The task to perform.  See {@link #multiBlockLoop(net.minecraft.world.World, int, int, int, int, int)}
     *               for task ID's
     * @return Whether the task was successful
     */
    private boolean doTask(World world, int parentX, int parentY, int parentZ, int parentMeta, int posX, int posY, int posZ, int taskID) {
        switch (taskID) {
            //Check if block can be placed at this position
            case (0): {
                Block block = world.getBlock(posX, posY, posZ);
                return !(posY > 255 || !block.isReplaceable(world, posX, posY, posZ));
            }
            //Create the structure
            case (1): {
                if (!(posX == parentX && posY == parentY && posZ == parentZ)) {
                    this.setBoundingBlock(world, parentX, parentY, parentZ, parentMeta, posX, posY, posZ);
                }
                return true;
            }
            //Validate the structure
            case (2): {
                Block block = world.getBlock(posX, posY, posZ);
                if (block instanceof BlockMultiBlock) {
                    TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(posX, posY, posZ);
                    if (tileEntity.isParent) return true;
                    if (tileEntity.hasParent) {
                        return world.getBlock(parentX, parentY, parentZ) instanceof BlockMultiBlock && parentX == tileEntity.parentX && parentY == tileEntity.parentY && parentZ == tileEntity.parentZ;
                    }
                }
                return false;
            }
            //Destroy structure
            case (3): {
                //TODO play sound and break effects for each block? See world.func_147480_a
                //If parent, drop
                if (posX == parentX && posY == parentY && posZ == parentZ) {
                    Block block = world.getBlock(posX, posY, posZ);
                    block.dropBlockAsItem(world, posX, posY, posZ, world.getBlockMetadata(posX, posY, posZ), 0);
                }
                //Ignore the output from this as it'll return false if it hits the part that was removed by the player
                world.setBlock(posX, posY, posZ, Blocks.air, 0, 2);
                return true;
            }
            default: return false;
        }
    }

    /**
     * Sets the target block as an invisible bounding box that passes any calls onto it's parent.
     * @param world The world
     * @param parentX The parent x pos
     * @param parentY The parent y pos
     * @param parentZ The parent z pos
     * @param parentMeta The parents meta
     * @param posX The target x pos
     * @param posY The target y pos
     * @param posZ The target z pos
     */
    private void setBoundingBlock(World world, int parentX, int parentY, int parentZ, int parentMeta, int posX, int posY, int posZ) {
        //Set the block and verify. Use flag 2 here and we don't want to trigger a block update
        if (world.setBlock(posX, posY, posZ, HCMWCore.blockBounding, parentMeta, 2)) {
            TileEntityBounding tileEntityBounding = (TileEntityBounding) world.getTileEntity(posX, posY, posZ);
            tileEntityBounding.setParent(parentX, parentY, parentZ);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(x, y, z);

        if (tileEntity != null) {
            //We want to return the parent item, not this
            if (tileEntity.isParent) return Item.getItemFromBlock(this);
            else {
                return Item.getItemFromBlock(world.getBlock(tileEntity.parentX, tileEntity.parentY, tileEntity.parentZ));
            }
        }
        else return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(x, y, z);
        float[] boundingBoxMax = this.boundingBoxMax;
        int facing = world.getBlockMetadata(x, y, z);

        if (tileEntity != null) {
            if (!tileEntity.isParent) {
                x = tileEntity.parentX;
                y = tileEntity.parentY;
                z = tileEntity.parentZ;
                if (world.getBlock(x, y, z) instanceof BlockMultiBlock) boundingBoxMax = ((BlockMultiBlock) world.getBlock(x, y, z)).boundingBoxMax;
            }
            switch (ForgeDirection.getOrientation(facing)) {
                //South
                case SOUTH: {
                    return AxisAlignedBB.getBoundingBox(x - 1, y, z, x + boundingBoxMax[0] - 1, y + boundingBoxMax[1], z + boundingBoxMax[2]);
                }
                //West
                case WEST: {
                    return AxisAlignedBB.getBoundingBox(x + 1, y, z - 1, x - boundingBoxMax[2] + 1, y + boundingBoxMax[1], z + boundingBoxMax[0] - 1);
                }
                //North
                case NORTH: {
                    return AxisAlignedBB.getBoundingBox(x, y, z + 1, x + boundingBoxMax[0], y + boundingBoxMax[1], z - boundingBoxMax[2] + 1);
                }
                //East
                case EAST: {
                    return AxisAlignedBB.getBoundingBox(x, y, z, x + boundingBoxMax[2], y + boundingBoxMax[1], z + boundingBoxMax[0]);
                }
            }
        }
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1F, y + 1F, z + 1F);
    }

    //TODO test properly with different directions and more collision boxes. This really needs testing with more models.
    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List boundingBoxList, Entity entityColliding) {
        TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(x, y, z);
        int facing = world.getBlockMetadata(x, y, z);
        List<float[]> collisionBoxes = this.collisionBoxes;

        if (tileEntity != null) {
            //Call the parent block to get the collision boxes
            if (!tileEntity.isParent) {
                x = tileEntity.parentX;
                y = tileEntity.parentY;
                z = tileEntity.parentZ;
                world.getBlock(x, y, z).addCollisionBoxesToList(world, x, y, z, aabb, boundingBoxList, entityColliding);
                return;
            }
        }
        //Loop through and add the collision boxes
        if (collisionBoxes != null && collisionBoxes.size() > 0) {
            for (float[] coords : collisionBoxes) {
                if (coords.length == 6) {
                    AxisAlignedBB axisAlignedBB = null;
                    //TODO test this code on other sized structures
                    switch (ForgeDirection.getOrientation(facing)) {
                        //South
                        case SOUTH: {
                            axisAlignedBB = AxisAlignedBB.getBoundingBox(x + coords[0] - 1, y + coords[1], z + coords[2], x + coords[3] - 1, y + coords[4], z + coords[5]);
                            break;
                        }
                        //West
                        case WEST: {
                            axisAlignedBB = AxisAlignedBB.getBoundingBox(x - coords[5] + 1, y + coords[1], z + coords[0] - 1, x - coords[2] + 1, y + coords[4], z + coords[3] - 1);
                            break;
                        }
                        //North
                        case NORTH: {
                            axisAlignedBB = AxisAlignedBB.getBoundingBox(x + coords[0], y + coords[1], z - coords[5] + 1, x + coords[3], y + coords[4], z - coords[2] + 1);
                            break;
                        }
                        //East
                        case EAST: {
                            axisAlignedBB = AxisAlignedBB.getBoundingBox(x + coords[2], y + coords[1], z + coords[0], x + coords[5], y + coords[4], z + coords[3]);
                            break;
                        }
                    }
                    //axisAlignedBB = AxisAlignedBB.getBoundingBox(x + coords[2], y + coords[1], z + coords[0], x + coords[5], y + coords[4], z + coords[3]);
                    if (axisAlignedBB != null && aabb.intersectsWith(axisAlignedBB)) {
                        boundingBoxList.add(axisAlignedBB);
                    }
                }
            }
        }
    }

    public BlockMultiBlock setBoundingBoxMax(float maxWidth, float maxHeight, float maxDepth) {
        this.boundingBoxMax = new float[] {maxWidth, maxHeight, maxDepth};
        return this;
    }

    public BlockMultiBlock setCollisionBoxes(List<float[]> collisionBoxes) {
        this.collisionBoxes = collisionBoxes;
        return this;
    }

    /**
     * Determines the {@link net.minecraftforge.common.util.ForgeDirection} the entity is facing
     * @param entity The entity
     * @return The direction
     */
    public static int determineForgeOrientation(EntityLivingBase entity) {
        int dir = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return dir == 0 ? 3 : (dir == 1 ? 4 : (dir == 2 ? 2 : (dir == 3 ? 5 : 0)));
    }
}
