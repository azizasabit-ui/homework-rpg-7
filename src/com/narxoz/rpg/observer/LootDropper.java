package com.narxoz.rpg.observer;

import java.util.Random;

public class LootDropper implements GameObserver {
    private Random random = new Random();
    
    @Override
    public void onEvent(GameEvent event) {
        switch(event.getType()) {
            case BOSS_PHASE_CHANGED:
                dropLoot("Phase " + event.getValue() + " Reward", event.getValue());
                break;
            case BOSS_DEFEATED:
                dropLoot("Final Boss Loot", 5);
                break;
        }
    }
    
    private void dropLoot(String lootType, int phase) {
        String[] commonLoot = {"Gold Coins", "Health Potion", "Mana Crystal", "Leather Scraps"};
        String[] rareLoot = {"Dragon Scale", "Legendary Sword", "Phoenix Feather", "Ancient Relic"};
        
        String loot;
        if (phase >= 3) {
            loot = rareLoot[random.nextInt(rareLoot.length)];
            System.out.println("  💎 [LOOT] " + lootType + ": " + loot + " (LEGENDARY)");
        } else if (phase == 2) {
            loot = rareLoot[random.nextInt(rareLoot.length)];
            System.out.println("  🎁 [LOOT] " + lootType + ": " + loot + " (RARE)");
        } else {
            loot = commonLoot[random.nextInt(commonLoot.length)];
            System.out.println("  📦 [LOOT] " + lootType + ": " + loot);
        }
    }
}