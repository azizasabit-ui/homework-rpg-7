package com.narxoz.rpg.combatant;

import com.narxoz.rpg.strategy.CombatStrategy;
import com.narxoz.rpg.strategy.MeasuredStrategy;
import com.narxoz.rpg.strategy.AggressiveStrategy;
import com.narxoz.rpg.strategy.DesperateStrategy;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import java.util.ArrayList;
import java.util.List;

public class DungeonBoss {
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private int currentPhase;
    private CombatStrategy strategy;
    private List<GameObserver> observers = new ArrayList<>();
    private boolean phase2Fired = false;
    private boolean phase3Fired = false;

    public DungeonBoss(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.currentPhase = 1;
        this.strategy = new MeasuredStrategy();
    }
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    private void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.onEvent(event);
        }
    }
    
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED && 
            event.getSourceName().equals(name)) {
            int newPhase = event.getValue();
            switchToPhase(newPhase);
        }
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }
    public int getCurrentPhase() { return currentPhase; }
    public boolean isAlive() { return hp > 0; }
    
    public CombatStrategy getStrategy() { return strategy; }
    
    private void switchToPhase(int phase) {
        this.currentPhase = phase;
        switch(phase) {
            case 1:
                strategy = new MeasuredStrategy();
                break;
            case 2:
                strategy = new AggressiveStrategy();
                break;
            case 3:
                strategy = new DesperateStrategy();
                break;
        }
        System.out.println("  🔥 " + name + " enters PHASE " + phase + " (" + strategy.getName() + ")!");
    }

    public void takeDamage(int amount) {
        int oldHp = hp;
        int actualDamage = Math.min(amount, hp);
        hp = Math.max(0, hp - amount);
        
        // Check phase transitions
        double hpPercent = (double)hp / maxHp;
        
        if (!phase3Fired && hpPercent <= 0.3) {
            phase3Fired = true;
            notifyObservers(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        } else if (!phase2Fired && hpPercent <= 0.6) {
            phase2Fired = true;
            notifyObservers(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        }
        
        if (hp <= 0) {
            notifyObservers(new GameEvent(GameEventType.BOSS_DEFEATED, name, 0));
        }
    }
    
    public int calculateDamage() {
        return strategy.calculateDamage(attackPower);
    }
    
    public int calculateDefense() {
        return strategy.calculateDefense(defense);
    }
    
    public String getStatus() {
        return String.format("%s [HP: %d/%d, Phase: %d, Strategy: %s]", 
                           name, hp, maxHp, currentPhase, strategy.getName());
    }
}