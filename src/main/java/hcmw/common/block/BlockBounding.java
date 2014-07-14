package hcmw.common.block;

import hcmw.common.tileentity.TileEntityBounding;
import hcmw.common.tileentity.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A bounding block that is mostly to prevent players from entering the block as well as passing on any needed calls
 */
public class BlockBounding extends BlockMultiBlock {

    public BlockBounding() {
        super(Material.wood);
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemById(-1);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    //TODO Allow for multiple boxes so players can stand on the bed?
    /*    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity) {
        if (this.boundingBoxMax != null && this.boundingBoxMax.length == 6) {
            this.setBlockBounds(this.boundingBoxMax[0], this.boundingBoxMax[1], this.boundingBoxMax[2], this.boundingBoxMax[3], this.boundingBoxMax[4], this.boundingBoxMax[5]);
        }
        else {
            this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
        }
        super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
    }*/

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(x, y, z);

        //Pass the activation to the parent
        if (tileEntity != null) {
            Block parentBlock = world.getBlock(tileEntity.parentX, tileEntity.parentY, tileEntity.parentZ);
            return parentBlock.onBlockActivated(world, tileEntity.parentX, tileEntity.parentY, tileEntity.parentZ, player, meta, p_149727_7_, p_149727_8_, p_149727_9_);
        }
        else return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBounding();
    }
}
