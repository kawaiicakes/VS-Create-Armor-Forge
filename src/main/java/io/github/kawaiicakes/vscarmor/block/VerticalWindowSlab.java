package io.github.kawaiicakes.vscarmor.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

@SuppressWarnings("deprecation")
public class VerticalWindowSlab extends AbstractWindowSlab {
    public VerticalWindowSlab(BlockBehaviour.Properties settings) {
        super(
                settings
                        .noOcclusion()
                        .isValidSpawn(AbstractWindowBlock::ezPredicateLol)
                        .forceSolidOff()
                        .isViewBlocking(AbstractWindowBlock::ezPredicate)
                        .isSuffocating(AbstractWindowBlock::ezPredicate)
        );
    }
}
