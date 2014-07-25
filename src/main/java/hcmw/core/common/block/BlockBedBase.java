package hcmw.core.common.block;

import hcmw.core.common.tileentity.TileEntityBed;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockBedBase extends BlockMultiBlock {

    public BlockBedBase() {
        super(Material.wood);
        this.boundingBoxMax = new float[]{2F, 3F, 2F};
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
        if (!world.isRemote) {
            //Set this as the parent and create the structure
            TileEntityBed tileEntityBed = (TileEntityBed) world.getTileEntity(x, y, z);

            tileEntityBed.setIsParent();
            this.createStructure(world, x, y, z, meta);
        }
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (world.isRemote) return true;
        else {
            if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell) {
                //Check if another player is sleeping in this bed
                EntityPlayer entityplayer1 = null;

                for (Object playerObject : world.playerEntities) {
                    EntityPlayer player = (EntityPlayer) playerObject;

                    if (player.isPlayerSleeping()) {
                        ChunkCoordinates chunkcoordinates = player.playerLocation;

                        if (chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z) {
                            entityplayer1 = player;
                        }
                    }
                }

                if (entityplayer1 != null) {
                    entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied"));
                    return true;
                }

                this.setOccupied(world, x, y, z, false);

                EntityPlayer.EnumStatus sleepStatus = entityPlayer.sleepInBedAt(x, y, z);

                if (sleepStatus == EntityPlayer.EnumStatus.OK) {
                    this.setOccupied(world, x, y, z, true);
                    return true;
                }
                else {
                    if (sleepStatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
                        entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep"));
                    }
                    else if (sleepStatus == EntityPlayer.EnumStatus.NOT_SAFE) {
                        entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe"));
                    }

                    return true;
                }
            }
            else {
                world.func_147480_a(x, y, z, false); //Destroy block
                world.newExplosion(null, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 5.0F, true, true);
                return true;
            }
        }
    }

    public void setOccupied(World world, int x, int y, int z, boolean bool) {
/*        int l = world.getBlockMetadata(x, y, z);

        if (bool) l |= 4;
        else l &= -5;

        world.setBlockMetadataWithNotify(x, y, z, l, 4);*/
    }

    @Override
    public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
        return true;
    }

    @Override
    public ChunkCoordinates getBedSpawnPosition(IBlockAccess world, int x, int y, int z, EntityPlayer player) {
        if (world instanceof World) return BlockBed.func_149977_a((World) world, x, y, z, 1);
        return null;
    }

    @Override
    public void setBedOccupied(IBlockAccess world, int x, int y, int z, EntityPlayer player, boolean occupied) {
        if (world instanceof World) BlockBed.func_149979_a((World)world,  x, y, z, occupied);
    }

    public int getBedDirection(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    public boolean isBedFoot(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBed();
    }

    @Override
    public int getDirectionOppositeFacing(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }
}
