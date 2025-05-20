package com.edge.linden.avaritia.alter;
import com.edge.linden.registry.ModDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class DemonicDamageSource {

    public static DamageSource create(LivingEntity attacker) {
        Holder<DamageType> type = attacker.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(ModDamageTypes.DEMONIC);

        return new DamageSource(type, attacker);
    }
}
