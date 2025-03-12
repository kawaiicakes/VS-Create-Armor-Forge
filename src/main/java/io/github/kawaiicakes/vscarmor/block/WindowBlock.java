package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("unused")
public interface WindowBlock {
    boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction);

    default RenderType getRenderLayer() {
        return RenderType.translucent();
    }

    VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context);

    default float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0F;
    }

    default boolean isTransparent(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }
}
