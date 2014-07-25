package hcmw.core.common.block;

import net.minecraft.world.World;

//TODO move to TE?
/**
 * Implemented by blocks that have a directional facing
 */
public interface IDirectional {

    /**
     * Returns the direction this block was placed with the entity facing
     * @param world The world
     * @param x The x pos
     * @param y The y pos
     * @param z The z pos
     * @return The direction
     */
    public int getDirectionOppositeFacing(World world, int x, int y, int z);
}
