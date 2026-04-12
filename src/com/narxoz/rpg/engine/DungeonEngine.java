package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import java.util.List;

public class DungeonEngine {
    private List<Hero> heroes;
    private DungeonBoss boss;
    private int maxRounds = 30;
    
    public DungeonEngine(List<Hero> heroes, DungeonBoss boss) {
        this.heroes = heroes;
        this.boss = boss;
    }
    
    public EncounterResult runEncounter() {
        System.out.println("\n⚔️ DUNGEON ENCOUNTER START! ⚔️\n");
        int round = 0;
        
        while (round < maxRounds && boss.isAlive() && atLeastOneHeroAlive()) {
            round++;
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("ROUND " + round);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            // Heroes attack boss
            System.out.println("\n🔹 HEROES TURN:");
            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    int damage = hero.calculateDamage();
                    int actualDamage = Math.max(0, damage - boss.calculateDefense());
                    boss.takeDamage(actualDamage);
                    System.out.println("  " + hero.getName() + " (" + hero.getStrategy().getName() + 
                                     ") attacks for " + damage + " damage! (Boss defense: " + 
                                     boss.calculateDefense() + ", net: " + actualDamage + ")");
                    
                    // Fire attack landed event
                    fireEvent(new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), actualDamage));
                    
                    if (!boss.isAlive()) break;
                }
            }
            
            if (!boss.isAlive()) break;
            
            // Boss attacks all heroes
            System.out.println("\n🔻 BOSS TURN (" + boss.getStrategy().getName() + "):");
            int bossDamage = boss.calculateDamage();
            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    int actualDamage = Math.max(0, bossDamage - hero.calculateDefense());
                    hero.takeDamage(actualDamage);
                    System.out.println("  " + boss.getName() + " attacks " + hero.getName() + 
                                     " for " + bossDamage + " damage! (Hero defense: " + 
                                     hero.calculateDefense() + ", net: " + actualDamage + ")");
                    
                    // Fire attack landed event
                    fireEvent(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), actualDamage));
                }
            }
            
            // Print status
            System.out.println("\n📊 Status after round " + round + ":");
            for (Hero hero : heroes) {
                System.out.println("  " + hero.getStatus());
            }
            System.out.println("  " + boss.getStatus());
        }
        
        boolean heroesWon = boss.isAlive() ? false : true;
        int survivingHeroes = (int)heroes.stream().filter(Hero::isAlive).count();
        
        System.out.println("\n═══════════════════════════════════════════════════════");
        if (heroesWon) {
            System.out.println("🏆 VICTORY! The heroes have defeated the Cursed Dungeon Boss! 🏆");
        } else {
            System.out.println("💀 DEFEAT! The heroes have fallen to the Cursed Dungeon Boss! 💀");
        }
        System.out.println("═══════════════════════════════════════════════════════");
        
        return new EncounterResult(heroesWon, round, survivingHeroes);
    }
    
    private boolean atLeastOneHeroAlive() {
        return heroes.stream().anyMatch(Hero::isAlive);
    }
    
    private void fireEvent(GameEvent event) {
        // Events are handled by observers registered with heroes and boss
        // The observers will print their own messages
    }
}