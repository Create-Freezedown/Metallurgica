package dev.metallurgists.metallurgica.foundation.mixin.workplace_hazards;

import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    private Level level;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getEyeY();

    @Shadow
    public abstract double getZ();

    @Shadow
    protected abstract AABB getBoundingBoxForPose(Pose pose);

    // @todo: consider the compatibility effects

    @Unique
    BlockEntity wpHaz$findPress() {

        BlockPos eyePos = new BlockPos((int) getX(), (int) getEyeY(), (int) getZ());
        BlockEntity entity = level.getBlockEntity(eyePos.above());

        // this is literally the worst ever solution i could have come up with and i frankly deserve to be punted in the face for it
        if(entity == null)
            entity = level.getBlockEntity(eyePos.above().above()); // try 2 on for size

        return entity;
    }

    @Inject(method = "canEnterPose", at = @At("HEAD"), cancellable = true)
    public void canEnterPose(Pose pose, CallbackInfoReturnable<Boolean> cir) {
        if (!MetallurgicaConfigs.common().experiments.workplaceHazards.mechanicalPressCrushing.get())
            return;

        var entity = wpHaz$findPress();

        if(entity instanceof MechanicalPressBlockEntity pressEntity) { // fancy meeting you here
            PressingBehaviour behaviour = pressEntity.getPressingBehaviour();
            double position = (Math.abs(0.5 - ((double) Math.abs(behaviour.runningTicks) / PressingBehaviour.CYCLE)) * 2) - 1; // calculate the depression

            double posY = this.getBoundingBoxForPose(pose).maxY;
            double maxY  = entity.getBlockPos().getY() + position;

            if(posY > maxY) { // check if the piston is deep enough to justify shifting poses
                cir.setReturnValue(false);
                cir.cancel();
            }

            // fun fact! this automagically prevents the player from dying if they can be pushed out of the way

        }
    }

}
