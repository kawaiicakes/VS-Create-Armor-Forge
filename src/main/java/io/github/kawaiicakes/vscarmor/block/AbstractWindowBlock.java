package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

// TODO (1.1) - Collision and shape methods are required to be implemented from here for empty blockstates
// TODO (1.1) - Camera collision
@SuppressWarnings("deprecation")
public abstract class AbstractWindowBlock extends RotatedPillarBlock implements WindowBlock {
    public static boolean ezPredicateLol(BlockState a, BlockGetter b, BlockPos c, EntityType<?> d) {
        return false;
    }

    public static boolean ezPredicate(BlockState a, BlockGetter b, BlockPos c) {
        return false;
    }
    
    public AbstractWindowBlock(BlockBehaviour.Properties settings) {
        super(
                settings
                        .noOcclusion()
                        .isValidSpawn(AbstractWindowBlock::ezPredicateLol)
                        .isRedstoneConductor(AbstractWindowBlock::ezPredicate)
                        .isViewBlocking(AbstractWindowBlock::ezPredicate)
                        .isSuffocating(AbstractWindowBlock::ezPredicate)
        );
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return (stateFrom.is(this)) && stateFrom.getValue(AXIS).equals(state.getValue(AXIS));
    }
}
