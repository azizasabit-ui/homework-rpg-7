package com.narxoz.rpg.combatant;

import com.narxoz.rpg.strategy.CombatStrategy;
import com.narxoz.rpg.strategy.NeutralStrategy;
import java.util.ArrayList;
import java.util.List;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;

/**
 * Represents a player-controlled hero participating in the dungeon encounter.
 * Adapted from Homework 6.
 */
public class Hero {
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private CombatStrategy strategy;
    private boolean lowHpFired = false;
    private List<GameObserver> observers = new ArrayList<>();

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.strategy = new NeutralStrategy();
    }
    
    public Hero(String name, int hp, int attackPower, int defense, CombatStrategy strategy) {
        this(name, hp, attackPower, defense);
        this.strategy = strategy;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }
    public boolean isAlive() { return hp > 0; }
    public CombatStrategy getStrategy() { return strategy; }
    
    public void setStrategy(CombatStrategy strategy) {
        this.strategy = strategy;
        System.out.println("  🔄 " + name + " switches to " + strategy.getName() + " strategy!");
    }
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    private void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.onEvent(event);
        }
    }

    public void takeDamage(int amount) {
        int actualDamage = Math.max(0, amount);
        int oldHp = hp;
        hp = Math.max(0, hp - actualDamage);
        
        if (oldHp > 0 && hp <= 0) {
            notifyObservers(new GameEvent(GameEventType.HERO_DIED, name, hp));
        } else if (!lowHpFired && hp > 0 && hp <= maxHp * 0.3) {
            lowHpFired = true;
            notifyObservers(new GameEvent(GameEventType.HERO_LOW_HP, name, hp));
        }
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
        if (hp > maxHp * 0.3) {
            lowHpFired = false;
        }
    }
    
    public int calculateDamage() {
        return strategy.calculateDamage(attackPower);
    }
    
    public int calculateDefense() {
        return strategy.calculateDefense(defense);
    }
    
    public String getStatus() {
        return String.format("%s [HP: %d/%d, Strategy: %s]", name, hp, maxHp, strategy.getName());
    }
}