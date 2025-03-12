package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

import static net.minecraft.core.Direction.*;
import static net.minecraft.world.level.block.StairBlock.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@SuppressWarnings("deprecation")
public class VerticalStairsBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<BlockHalf> HALF = EnumProperty.create("half", BlockHalf.class);
    public static final EnumProperty<VerticalStairShape> V_SHAPE
            = EnumProperty.create("shape", VerticalStairShape.class);

    public static final int[] RIGHT_INDICES = new int[] {
            3, 11, 7, 2, 1
    };

    public static final int[] LEFT_INDICES = new int[] {
            12, 14, 13, 8, 4
    };

    protected static final VoxelShape[] NORTH_SHAPES = makeShapes(
            VerticalSlabBlock.NORTH, OCTET_PNP, OCTET_PPP,
            OCTET_NNP, OCTET_NPP
    );

    protected static final VoxelShape[] EAST_SHAPES = makeShapes(
            VerticalSlabBlock.EAST, OCTET_NNP, OCTET_NPP,
            OCTET_NNN, OCTET_NPN
    );

    protected static final VoxelShape[] SOUTH_SHAPES = makeShapes(
            VerticalSlabBlock.SOUTH, OCTET_NNN, OCTET_NPN,
            OCTET_PNN, OCTET_PPN
    );

    protected static final VoxelShape[] WEST_SHAPES = makeShapes(
            VerticalSlabBlock.WEST, OCTET_PNN, OCTET_PPN,
            OCTET_PNP, OCTET_PPP
    );

    private final Block baseBlock;
    private final BlockState baseBlockState;

    public VerticalStairsBlock(Supplier<BlockState> baseBlockState, BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(HORIZONTAL_FACING, NORTH)
                        .setValue(HALF, BlockHalf.RIGHT)
                        .setValue(V_SHAPE, VerticalStairShape.STRAIGHT)
                        .setValue(WATERLOGGED, Boolean.FALSE)
        );
        this.baseBlock = baseBlockState.get().getBlock();
        this.baseBlockState = baseBlockState.get();
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        int[] indices = state.getValue(HALF) == BlockHalf.RIGHT ? RIGHT_INDICES : LEFT_INDICES;
        VoxelShape[] directionShapes = switch (state.getValue(HORIZONTAL_FACING)) {
            case NORTH -> NORTH_SHAPES;
            case SOUTH -> SOUTH_SHAPES;
            case WEST -> WEST_SHAPES;
            default -> EAST_SHAPES;
        };

        return directionShapes[indices[state.getValue(V_SHAPE).ordinal()]];
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        this.baseBlock.animateTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        this.baseBlockState.attack(pLevel, pPos, pPlayer);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        this.baseBlock.destroy(pLevel, pPos, pState);
    }

    @Override
    public float getExplosionResistance() {
        return this.baseBlock.getExplosionResistance();
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean pMovedByPiston) {
        if (!state.is(state.getBlock())) {
            world.neighborChanged(this.baseBlockState, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            this.baseBlockState.onRemove(level, pos, newState, moved);
        }
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        this.baseBlock.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return this.baseBlock.isRandomlyTicking(pState);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.baseBlock.randomTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.baseBlock.tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public InteractionResult use(
            BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit
    ) {
        return this.baseBlockState.use(pLevel, pPlayer, pHand, pHit);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        this.baseBlock.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(blockPos);

        Direction playerFacing = ctx.getHorizontalDirection();

        boolean isRight = switch (playerFacing) {
            case NORTH -> (ctx.getClickLocation().x - (double) blockPos.getX()) >= 0.5;
            case SOUTH -> (ctx.getClickLocation().x - (double) blockPos.getX()) <= 0.5;
            case WEST -> (ctx.getClickLocation().z - (double) blockPos.getZ()) <= 0.5;
            default -> (ctx.getClickLocation().z - (double) blockPos.getZ()) >= 0.5;
        };

        BlockState toReturn = this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, playerFacing)
                .setValue(HALF, isRight ? BlockHalf.RIGHT : BlockHalf.LEFT)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

        return toReturn.setValue(V_SHAPE, getVerticalStairShape(toReturn, ctx.getLevel(), blockPos));
    }

    @Override
    public BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState,
            LevelAccessor world, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        Direction originalFacing = state.getValue(HORIZONTAL_FACING);

        return !direction.getAxis().equals(originalFacing.getAxis())
                ? state.setValue(V_SHAPE, getVerticalStairShape(state, world, pos))
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    private static VerticalStairShape getVerticalStairShape(BlockState placedState, BlockGetter world, BlockPos placedPos) {
        VerticalStairShape defaultReturn = VerticalStairShape.STRAIGHT;

        Direction placedFacing = placedState.getValue(HORIZONTAL_FACING);

        BlockState rightState = world.getBlockState(placedPos.offset(placedFacing.getClockWise().getNormal()));
        BlockState leftState = world.getBlockState(placedPos.offset(placedFacing.getCounterClockWise().getNormal()));

        if (!isStairs(rightState) && !isStairs(leftState))
            return defaultReturn;

        BlockState aboveState = world.getBlockState(placedPos.offset(UP.getNormal()));
        BlockState belowState = world.getBlockState(placedPos.offset(DOWN.getNormal()));

        BlockHalf placedHalf = placedState.getValue(HALF);

        boolean aboveForcesStraight = isVStairs(aboveState)
                && aboveState.getValue(HALF).equals(placedHalf)
                && aboveState.getValue(HORIZONTAL_FACING).equals(placedFacing)
                && isForcingShape(aboveState.getValue(V_SHAPE));

        // cut calculation early if above already forces straight
        boolean belowForcesStraight = !aboveForcesStraight
                && isVStairs(belowState)
                && belowState.getValue(HALF).equals(placedHalf)
                && belowState.getValue(HORIZONTAL_FACING).equals(placedFacing)
                && isForcingShape(belowState.getValue(V_SHAPE));

        if (aboveForcesStraight || belowForcesStraight) return defaultReturn;

        if (placedHalf.equals(BlockHalf.RIGHT)) {
            if (shapeIsCongruent(rightState, BlockHalf.RIGHT, placedFacing)) {
                return rightState.getValue(StairBlock.HALF).equals(Half.TOP)
                        ? VerticalStairShape.OUTER_TOP
                        : VerticalStairShape.OUTER_BOTTOM;
            } else if (shapeIsCongruent(leftState, BlockHalf.LEFT, placedFacing)) {
                return leftState.getValue(StairBlock.HALF).equals(Half.TOP)
                        ? VerticalStairShape.INNER_TOP
                        : VerticalStairShape.INNER_BOTTOM;
            }
        } else {
            if (shapeIsCongruent(leftState, BlockHalf.LEFT, placedFacing)) {
                return leftState.getValue(StairBlock.HALF).equals(Half.TOP)
                        ? VerticalStairShape.OUTER_TOP
                        : VerticalStairShape.OUTER_BOTTOM;
            } else if (shapeIsCongruent(rightState, BlockHalf.RIGHT, placedFacing)) {
                return rightState.getValue(StairBlock.HALF).equals(Half.TOP)
                        ? VerticalStairShape.INNER_TOP
                        : VerticalStairShape.INNER_BOTTOM;
            }
        }

        return defaultReturn;
    }

    public static boolean shapeIsCongruent(BlockState stairs, BlockHalf half, Direction horizontal) {
        if (!isStairs(stairs)) return false;
        if (!stairs.getValue(HORIZONTAL_FACING).equals(horizontal)) return false;

        StairsShape shape = stairs.getValue(SHAPE);

        if (shape.equals(StairsShape.STRAIGHT)) return true;

        if (half.equals(BlockHalf.RIGHT)) {
            return shape.equals(StairsShape.OUTER_LEFT) || shape.equals(StairsShape.INNER_RIGHT);
        } else {
            return shape.equals(StairsShape.OUTER_RIGHT) || shape.equals(StairsShape.INNER_LEFT);
        }
    }

    public static boolean isForcingShape(VerticalStairShape shape) {
        return shape.equals(VerticalStairShape.STRAIGHT)
                || shape.equals(VerticalStairShape.INNER_TOP)
                || shape.equals(VerticalStairShape.OUTER_BOTTOM);
    }

    public static boolean isVStairs(BlockState state) {
        return state.getBlock() instanceof VerticalStairsBlock;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(HORIZONTAL_FACING);

        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    return state.setValue(HALF, state.getValue(HALF).opposite());
                } else {
                    return state
                            .setValue(HALF, state.getValue(HALF).opposite())
                            .setValue(HORIZONTAL_FACING, direction.getOpposite());
                }
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    return state.setValue(HALF, state.getValue(HALF).opposite());
                } else {
                    return state
                            .setValue(HALF, state.getValue(HALF).opposite())
                            .setValue(HORIZONTAL_FACING, direction.getOpposite());
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_FACING, HALF, V_SHAPE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    public enum BlockHalf implements StringRepresentable {
        LEFT("left"),
        RIGHT("right");

        private final String name;

        BlockHalf(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public BlockHalf opposite() {
            return this.equals(LEFT) ? RIGHT : LEFT;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public enum VerticalStairShape implements StringRepresentable {
        STRAIGHT("straight"),
        INNER_TOP("inner_top"),
        INNER_BOTTOM("inner_bottom"),
        OUTER_TOP("outer_top"),
        OUTER_BOTTOM("outer_bottom");

        private final String name;

        VerticalStairShape(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
