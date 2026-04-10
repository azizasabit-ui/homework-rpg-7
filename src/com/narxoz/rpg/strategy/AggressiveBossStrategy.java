package com.narxoz.rpg.strategy;

public class AggressiveBossStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int)(basePower * 1.2); // +20% damage
    }
    
    @Override
    public int calculateDefense(int baseDefense) {
        return (int)(baseDefense * 0.8); // -20% defense
    }
    
    @Override
    public String getName() {
        return "Aggressive (120% DMG, 80% DEF)";
    }
}