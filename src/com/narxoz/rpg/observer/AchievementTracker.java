package com.narxoz.rpg.observer;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver {
    private Set<String> unlockedAchievements = new HashSet<>();
    private int totalAttacks = 0;
    
    @Override
    public void onEvent(GameEvent event) {
        switch(event.getType()) {
            case ATTACK_LANDED:
                totalAttacks++;
                if (totalAttacks >= 10 && !unlockedAchievements.contains("Relentless")) {
                    unlockedAchievements.add("Relentless");
                    System.out.println("  🏅 [ACHIEVEMENT] Relentless - Landed 10 attacks!");
                }
                break;
            case BOSS_DEFEATED:
                if (!unlockedAchievements.contains("Boss Slayer")) {
                    unlockedAchievements.add("Boss Slayer");
                    System.out.println("  🏅 [ACHIEVEMENT] Boss Slayer - Defeated the Cursed Dungeon Boss!");
                }
                break;
            case HERO_DIED:
                if (!unlockedAchievements.contains("Tragic Loss")) {
                    unlockedAchievements.add("Tragic Loss");
                    System.out.println("  🏅 [ACHIEVEMENT] Tragic Loss - A hero has fallen...");
                }
                break;
        }
    }
}