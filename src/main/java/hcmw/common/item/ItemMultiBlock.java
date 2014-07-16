package hcmw.common.item;

import hcmw.common.block.BlockMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMultiBlock extends ItemBlock {
    public ItemMultiBlock(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        //The block placed will always be front-left-bottom of the final large structure so we take that the bounding box expands to the right.
        //We could support allowing bounding box to expand to the left in the future but lets get this done for now
        int facing = BlockMultiBlock.determineOrientation(player);
        BlockMultiBlock block = (BlockMultiBlock) Block.getBlockFromItem(stack.getItem());

        //Check if we can place the bed within the bounds defined by the bounding box
        if (!block.canPlaceAtLocFacing(world, x, y, z, facing)) {
            return false;
        }

        if (!world.setBlock(x, y, z, this.field_150939_a, facing, 3)) {
            return false;
        }

        //Verify the block has been placed and if so, call methods
        if (world.getBlock(x, y, z) == this.field_150939_a) {
            metadata = facing;
            this.field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            this.field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
