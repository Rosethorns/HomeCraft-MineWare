package hcmw.common.block;

import hcmw.common.tileentity.TileEntityBounding;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A bounding block that is mostly to prevent players from entering the block as well as passing on any needed calls
 */
public class BlockBounding extends BlockContainer {

    public BlockBounding() {
        super(Material.wood);
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        super.breakBlock(world, x, y, z, block, meta);
        TileEntityBounding tileEntity = (TileEntityBounding) world.getTileEntity(x, y, z);
        Block parentBlock = world.getBlock((int) tileEntity.parentX, (int) tileEntity.parentY, (int) tileEntity.parentZ);
        if (parentBlock != null) {
            parentBlock.breakBlock(world, (int) tileEntity.parentX, (int) tileEntity.parentY, (int) tileEntity.parentZ, parentBlock, meta);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBounding();
    }
}
