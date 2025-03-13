package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class PortholeSlab extends AbstractWindowSlab {
    public PortholeSlab(BlockBehaviour.Properties settings) {
        super(
                settings
                        .noOcclusion()
                        .isValidSpawn(AbstractWindowBlock::ezPredicateLol)
                        .isRedstoneConductor(AbstractWindowBlock::ezPredicate)
                        .isViewBlocking(AbstractWindowBlock::ezPredicate)
                        .isSuffocating(AbstractWindowBlock::ezPredicate)
        );
    }
}
