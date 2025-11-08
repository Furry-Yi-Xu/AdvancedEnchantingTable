package com.yixu.Util.Enchant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StoredVanillaEnchantKeys {

    public static final String PROTECTION = "minecraft:protection";
    public static final String FIRE_PROTECTION = "minecraft:fire_protection";
    public static final String FEATHER_FALLING = "minecraft:feather_falling";
    public static final String BLAST_PROTECTION = "minecraft:blast_protection";
    public static final String PROJECTILE_PROTECTION = "minecraft:projectile_protection";
    public static final String RESPIRATION = "minecraft:respiration";
    public static final String AQUA_AFFINITY = "minecraft:aqua_affinity";
    public static final String THORNS = "minecraft:thorns";
    public static final String DEPTH_STRIDER = "minecraft:depth_strider";
    public static final String FROST_WALKER = "minecraft:frost_walker";
    public static final String BINDING_CURSE = "minecraft:binding_curse";
    public static final String SHARPNESS = "minecraft:sharpness";
    public static final String SMITE = "minecraft:smite";
    public static final String BANE_OF_ARTHROPODS = "minecraft:bane_of_arthropods";
    public static final String KNOCKBACK = "minecraft:knockback";
    public static final String FIRE_ASPECT = "minecraft:fire_aspect";
    public static final String LOOTING = "minecraft:looting";
    public static final String SWEEPING_EDGE = "minecraft:sweeping_edge";
    public static final String EFFICIENCY = "minecraft:efficiency";
    public static final String SILK_TOUCH = "minecraft:silk_touch";
    public static final String UNBREAKING = "minecraft:unbreaking";
    public static final String FORTUNE = "minecraft:fortune";
    public static final String POWER = "minecraft:power";
    public static final String PUNCH = "minecraft:punch";
    public static final String FLAME = "minecraft:flame";
    public static final String INFINITY = "minecraft:infinity";
    public static final String LUCK_OF_THE_SEA = "minecraft:luck_of_the_sea";
    public static final String LURE = "minecraft:lure";
    public static final String LOYALTY = "minecraft:loyalty";
    public static final String IMPALING = "minecraft:impaling";
    public static final String RIPTIDE = "minecraft:riptide";
    public static final String CHANNELING = "minecraft:channeling";
    public static final String MULTISHOT = "minecraft:multishot";
    public static final String QUICK_CHARGE = "minecraft:quick_charge";
    public static final String PIERCING = "minecraft:piercing";
    public static final String DENSITY = "minecraft:density";
    public static final String BREACH = "minecraft:breach";
    public static final String WIND_BURST = "minecraft:wind_burst";
    public static final String MENDING = "minecraft:mending";
    public static final String VANISHING_CURSE = "minecraft:vanishing_curse";
    public static final String SOUL_SPEED = "minecraft:soul_speed";
    public static final String SWIFT_SNEAK = "minecraft:swift_sneak";

    // ------------------------------
    // 所有原生附魔集合
    // ------------------------------
    public static final Set<String> VANILLA_KEYS;

    static {
        Set<String> temp = new HashSet<>();
        Collections.addAll(temp,
                PROTECTION, FIRE_PROTECTION, FEATHER_FALLING, BLAST_PROTECTION,
                PROJECTILE_PROTECTION, RESPIRATION, AQUA_AFFINITY, THORNS,
                DEPTH_STRIDER, FROST_WALKER, BINDING_CURSE, SHARPNESS,
                SMITE, BANE_OF_ARTHROPODS, KNOCKBACK, FIRE_ASPECT, LOOTING,
                SWEEPING_EDGE, EFFICIENCY, SILK_TOUCH, UNBREAKING, FORTUNE,
                POWER, PUNCH, FLAME, INFINITY, LUCK_OF_THE_SEA, LURE, LOYALTY,
                IMPALING, RIPTIDE, CHANNELING, MULTISHOT, QUICK_CHARGE, PIERCING,
                DENSITY, BREACH, WIND_BURST, MENDING, VANISHING_CURSE, SOUL_SPEED,
                SWIFT_SNEAK
        );
        VANILLA_KEYS = Collections.unmodifiableSet(temp);
    }

    // 判断是否为原生附魔
    public static boolean isVanilla(String key) {
        return VANILLA_KEYS.contains(key);
    }
}
