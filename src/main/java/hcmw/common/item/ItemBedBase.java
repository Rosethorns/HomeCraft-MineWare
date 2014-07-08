package hcmw.common.item;

import hcmw.common.block.BlockBedBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBedBase extends ItemBlock {
    public ItemBedBase(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        //The block placed will always be front-left-bottom of the final large structure so we take that the bounding box expands to the right.
        //We could support allowing bounding box to expand to the left in the future but lets get this done for now

        int facing = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        //TODO cast check needed or safe to assume?
        BlockBedBase item = (BlockBedBase) Block.getBlockFromItem(stack.getItem());
        float[] boundingBoxMax = item.getBoundingBoxMax();

        //Check if we can place the bed within the bounds defined by the bounding box. I don't like how this looks :(
        switch (facing) {
            //South
            case 0: {
                for (int checkX = x; checkX > x - boundingBoxMax[0]; checkX--) {
                    for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                        for (int checkZ = z; checkZ < z + boundingBoxMax[2]; checkZ++) {
                            Block block = world.getBlock(checkX, checkY, checkZ);
                            if (!block.isReplaceable(world, checkX, checkY, checkZ) || checkY > 255) {
                                return false;
                            }
                        }
                    }
                }
                break;
            }
            //West
            case 1: {
                for (int checkX = x; checkX > x - boundingBoxMax[2]; checkX--) {
                    for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                        for (int checkZ = z; checkZ > z - boundingBoxMax[0]; checkZ--) {
                            Block block = world.getBlock(checkX, checkY, checkZ);
                            if (!block.isReplaceable(world, checkX, checkY, checkZ) || checkY > 255) {
                                return false;
                            }
                        }
                    }
                }
                break;
            }
            //North
            case 2: {
                for (int checkX = x; checkX < x + boundingBoxMax[0]; checkX++) {
                    for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                        for (int checkZ = z; checkZ > z - boundingBoxMax[2]; checkZ--) {
                            Block block = world.getBlock(checkX, checkY, checkZ);
                            if (!block.isReplaceable(world, checkX, checkY, checkZ) || checkY > 255) {
                                return false;
                            }
                        }
                    }
                }
                break;
            }
            //East
            case 3: {
                for (int checkX = x; checkX < x + boundingBoxMax[2]; checkX++) {
                    for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                        for (int checkZ = z; checkZ < z + boundingBoxMax[0]; checkZ++) {
                            Block block = world.getBlock(checkX, checkY, checkZ);
                            if (!block.isReplaceable(world, checkX, checkY, checkZ) || checkY > 255) {
                                return false;
                            }
                        }
                    }
                }
                break;
            }
        }

        System.out.println(facing);
        if (!world.setBlock(x, y, z, this.field_150939_a, facing, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == this.field_150939_a) {
            this.field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            this.field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
