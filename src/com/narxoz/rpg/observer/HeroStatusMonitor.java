package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class HeroStatusMonitor implements GameObserver {
    private List<Hero> heroes;
    
    public HeroStatusMonitor(List<Hero> heroes) {
        this.heroes = heroes;
    }
    
    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.HERO_LOW_HP || 
            event.getType() == GameEventType.HERO_DIED) {
            printStatus();
        }
    }
    
    private void printStatus() {
        System.out.println("  📊 [STATUS MONITOR] Hero Status:");
        for (Hero hero : heroes) {
            String status = hero.isAlive() ? 
                String.format("❤️ %s: %d/%d HP", hero.getName(), hero.getHp(), hero.getMaxHp()) :
                String.format("💀 %s: DEFEATED", hero.getName());
            System.out.println("     " + status);
        }
    }
}