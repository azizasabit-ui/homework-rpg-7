package com.narxoz.rpg.strategy;

public class DefensiveStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int)(basePower * 0.7); // -30% damage
    }
    
    @Override
    public int calculateDefense(int baseDefense) {
        return (int)(baseDefense * 1.5); // +50% defense
    }
    
    @Override
    public String getName() {
        return "Defensive (-30% DMG, +50% DEF)";
    }
}