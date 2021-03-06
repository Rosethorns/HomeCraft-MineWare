package hcmw.medieval.common.block;

import hcmw.HCMWMedieval;
import hcmw.core.common.block.BlockMultiBlock;
import hcmw.core.common.block.IDirectional;
import hcmw.core.common.tileentity.TileEntityFluid;
import hcmw.core.common.tileentity.TileEntityInventory;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockBarrel extends BlockContainer implements IDirectional {

    public BlockBarrel() {
        super(Material.wood);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        return (side == 2 || side == 3 || side == 4 || side == 5) ? meta | 8 : meta; //If on the side
    }

    //I don't think I can get bitwise stuff to work properly with forge directions so hacky solution it is!
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        //Set direction facing
        int dir = BlockMultiBlock.determineForgeOrientation(entity);
        int onWall = world.getBlockMetadata(x, y, z) & 8;
        world.setBlockMetadataWithNotify(x, y, z, dir | onWall, 2);
    }

    @Override
    public int getDirectionOppositeFacing(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta >= 8 ? meta - 8 : meta;
    }

    public static class BlockBarrelFluid extends BlockBarrel {

        @Override
        public TileEntity createNewTileEntity(World world, int meta) {
            return new TileEntityFluid(8000);
        }
    }

    public static class BlockBarrelInventory extends BlockBarrel {

        @Override
        public TileEntity createNewTileEntity(World world, int meta) {
            return new TileEntityInventory().setTileEntityRendererId(HCMWMedieval.MOD_ID + ":barrelInventory");
        }
    }
}
