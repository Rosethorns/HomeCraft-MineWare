package hcmw.common.block;

import hcmw.common.tileentity.TileEntityBounding;
import net.minecraft.block.material.Material;
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBounding();
    }
}
