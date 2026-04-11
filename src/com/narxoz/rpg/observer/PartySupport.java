package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {
    private List<Hero> heroes;
    private Random random = new Random();
    
    public PartySupport(List<Hero> heroes) {
        this.heroes = heroes;
    }
    
    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.HERO_LOW_HP) {
            // Find a random living ally (excluding the low HP hero)
            List<Hero> livingHeroes = heroes.stream()
                .filter(h -> h.isAlive() && !h.getName().equals(event.getSourceName()))
                .toList();
            
            if (!livingHeroes.isEmpty()) {
                Hero supporter = livingHeroes.get(random.nextInt(livingHeroes.size()));
                int healAmount = 20;
                supporter.heal(healAmount);
                System.out.println("  🤝 [PARTY SUPPORT] " + supporter.getName() + " heals " + 
                                 event.getSourceName() + " for " + healAmount + " HP as party support!");
            }
        }
    }
}