package dev.metallurgists.metallurgica.experimental.exposure_effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
@BlurryAt(2)
public class LeadPoisoningEffect extends ExposureEffect {
    //public static final DamageSource LEAD = (new DamageSource("leadPoisoning")).bypassArmor().bypassEnchantments().bypassMagic().setScalesWithDifficulty();
    public LeadPoisoningEffect(int pColor) {
        super(pColor);
    }
    
    @Override
    public void stageOneEffect(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getRandom().nextInt(300) < 15) {
                player.causeFoodExhaustion(0.21f);
            }
        }
    }
    
    public void stageTwoEffect(LivingEntity entity) {
        if (entity instanceof Player player) {
            player.getPersistentData().putBoolean("metallurgica:exposureEffect_showBlur", true);
        }
    }
    
    public void stageThreeEffect(LivingEntity entity) {
        if (entity instanceof Player player) {
            player.getPersistentData().putBoolean("metallurgica:exposureEffect_fatigue", true);
        }
    }
    
    public void stageFourEffect(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getRandom().nextInt(15) == 0) {
                //player.hurt(LEAD, 0.5f);
            }
        }
    }
}
