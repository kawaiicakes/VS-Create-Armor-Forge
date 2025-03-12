package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// TODO (1.1) - Collision and shape methods are required to be implemented from here for empty blockstates
@SuppressWarnings("deprecation")
public abstract class AbstractWindowSlab extends SlabBlock implements WindowBlock {
    public AbstractWindowSlab(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public abstract VoxelShape getCollisionShape(
            BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
    );

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (!stateFrom.is(this)) return false;

        return switch (direction) {
            case DOWN -> !state.getValue(TYPE).equals(SlabType.TOP) && !stateFrom.getValue(TYPE).equals(SlabType.BOTTOM);
            case UP -> !state.getValue(TYPE).equals(SlabType.BOTTOM) && !stateFrom.getValue(TYPE).equals(SlabType.TOP);
            default -> false;
        };
    }
}
