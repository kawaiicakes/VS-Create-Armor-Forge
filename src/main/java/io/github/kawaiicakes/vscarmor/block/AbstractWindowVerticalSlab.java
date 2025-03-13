package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

// TODO (1.1) - Collision and shape methods are required to be implemented from here for empty blockstates
// TODO (1.1) - Camera collision
@SuppressWarnings("deprecation")
public abstract class AbstractWindowVerticalSlab extends VerticalSlabBlock implements WindowBlock {
    public AbstractWindowVerticalSlab(BlockBehaviour.Properties settings) {
        super(
                settings
                        .noOcclusion()
                        .isValidSpawn(AbstractWindowBlock::ezPredicateLol)
                        .forceSolidOff()
                        .isViewBlocking(AbstractWindowBlock::ezPredicate)
                        .isSuffocating(AbstractWindowBlock::ezPredicate)
        );
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (!stateFrom.is(this)) return false;
        if (direction.equals(Direction.UP) || direction.equals(Direction.DOWN)) return false;
        if (!state.getValue(FACING).getAxis().equals(stateFrom.getValue(FACING).getAxis())) return false;
        if (!direction.getAxis().equals(state.getValue(FACING).getAxis())) return false;

        if (stateFrom.getValue(DOUBLET)) {
            return state.getValue(DOUBLET) || state.getValue(FACING).equals(stateFrom.getValue(FACING).getOpposite());
        } else {
            return state.getValue(DOUBLET)
                    ? stateFrom.getValue(FACING).equals(direction)
                    : state.getValue(FACING).equals(stateFrom.getValue(FACING).getOpposite());
        }
    }
}
