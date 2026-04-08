package com.narxoz.rpg.strategy;

public class MeasuredStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int)(basePower * 0.8); // Conservative damage
    }
    
    @Override
    public int calculateDefense(int baseDefense) {
        return baseDefense; // Normal defense
    }
    
    @Override
    public String getName() {
        return "Measured (80% DMG, 100% DEF)";
    }
}