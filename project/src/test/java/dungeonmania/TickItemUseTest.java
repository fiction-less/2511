package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.InvincibilityPotion;
import dungeonmania.CollectableEntities.InvisibilityPotion;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TickItemUseTest {
    
    String dummyDungeon = "d_newGameTest_simpleEntities";
    String dummyConfig = "c_spiderTest_basicMovement";
    int dummyRadius = 20;
    Position dummyPos = new Position(1, 1);
    
    @Test
    public void invalidAction(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking bomb NOT being added to players inventory
        Entity item = new Bomb("someid", dummyPos, 23);
        dmc.getCurrentGame().addEntitiy(item);

        assertThrows(InvalidActionException.class, () -> dmc.tick("someid"));
    }

    @Test
    public void illegalArgument(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking adding treasure to players inventory
        Entity item = new Treasure("someid", dummyPos);
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory((Item) item);

        assertThrows(IllegalArgumentException.class, () -> dmc.tick("someid"));
    }

    @Test
    public void validItem(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking adding usable items to players inventory
        Entity item1 = new Bomb("someid1", dummyPos, 20);
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory((Item) item1);

        Entity item2 = new InvincibilityPotion("someid2", dummyPos, 20);
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory((Item) item2);
        
        Entity item3 = new InvincibilityPotion("someid3", dummyPos, 20);
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory((Item) item3);

        assertDoesNotThrow(() -> dmc.tick("someid1"));
        assertDoesNotThrow(() -> dmc.tick("someid2"));
        assertDoesNotThrow(() -> dmc.tick("someid3"));
    }
    
    @Test 
    public void potionUse(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_movementTest_collectibles", "c_battleTest_potions");

        //Mocking adding potions to player's inventory
        Player player = Helper.getPlayer(dmc.getCurrentGame().getEntityList());
        Entity p1 = new InvincibilityPotion("pot1", dummyPos, 10);
        player.addInventory((Item) p1);
        
        Entity p2 = new InvisibilityPotion("pot2", dummyPos, 10);
        player.addInventory((Item) p2);

        Entity p3 = new InvincibilityPotion("pot3", dummyPos, 10);
        player.addInventory((Item) p3);

        //Consuming pots 
        assertDoesNotThrow(() -> dmc.tick("pot1")); //pot 1 added to potionqueue
        assertDoesNotThrow(() -> dmc.tick("pot2")); //pot 2 added to potionqueue
        assertDoesNotThrow(() -> dmc.tick("pot3")); //pot 3 added to potionqueue

        //potion in use is god potion (pot1)
        assertEquals(p1, player.getPotionInUse());
        assertEquals(true, player.getIsInvincible());
        assertEquals(false, player.getIsInvisible());

        //potion queue is [pot1, pot2]
        ArrayList<Item> expected = new ArrayList<Item>();
        expected.add((Item) p1);
        expected.add((Item) p2);
        expected.add((Item) p3);
        assertEquals(expected, player.getPotionQueue());
        
        //at this point duration of pot1 and pot2 should be 8 and 10 respectively 
        assertEquals(7, ((InvincibilityPotion) player.getPotionInUse()).getDuration()); 
        assertEquals(10, ((InvisibilityPotion) p2).getDuration()); 

        //tick 8 times 
        for (int i = 0; i < 8; i++){
            assertThrows(InvalidActionException.class, () -> dmc.tick("nonExistent"));
        }
        
        //p1 duration should have ran out and now p2 should be in use  
        assertEquals(p2, player.getPotionQueue().get(0));
        assertEquals(p2, player.getPotionInUse());
        assertEquals(false, player.getIsInvincible());
        
        assertEquals(true, player.getIsInvisible());
        
        assertEquals(9, ((InvisibilityPotion) p2).getDuration()); 
        
        //tick 9 times 
        for (int i = 0; i < 10; i++){
        assertThrows(InvalidActionException.class, () -> dmc.tick("nonExistent"));
        }

        //invincible pot left, activates automatically as its queued
        assertEquals(1, player.getPotionQueue().size());
        assertEquals(false, player.getIsInvisible());
        assertEquals(true, player.getIsInvincible());   
    }

    @Test
    public void bombPlantUnitTest(){
        DungeonManiaController dmc = new DungeonManiaController();
        //game starts with boulder on switch, and player next to switch
        dmc.newGame("d_bombTest_placeBombRadius2", "c_battleTest_potions");

        //mocking adding bomb to player's inventory 
        Player player = dmc.getCurrentGame().getPlayer();
        Bomb bomb = new Bomb("b1", dummyPos, 1);
        player.addInventory(bomb);

        //planting bomb
        bomb.plantBomb(dmc.getCurrentGame());
        
        //checking that bomb is planted next to floor switch
        FloorSwitch fs = Helper.switchNextToBomb(dmc.getCurrentGame().getEntityList(), bomb.getPosition());
        assertEquals(new Position(4, 1), fs.getPosition());
        assertEquals(new Position(3, 1), bomb.getPosition());

        //checking that floorswitch is triggered since boulder lies on it 
        assertEquals(true, fs.isTriggered(dmc.getCurrentGame()));

        // //checking that before explosion, entities are in the map
        // assertEquals(true, dmc.getEntityList().stream().anyMatch(x ->x.getType().equals("treasure")));
        // assertEquals(true, dmc.getEntityList().stream().anyMatch(x ->x.getType().equals("wall")));
        
        // //blowing up bomb  
        // bomb.blowUp(dmc.getEntityList());

        // assertEquals(false, dmc.getEntityList().stream().anyMatch(x ->x.getType().equals("treasure")));
        // assertEquals(false, dmc.getEntityList().stream().anyMatch(x ->x.getType().equals("wall")));
    }

    @Test
    public void bombExplodeTest(){
        DungeonManiaController dmc = new DungeonManiaController();
        //game starts with boulder on switch, and player next to switch
        dmc.newGame("d_bombTest_placeBombRadius2", "c_battleTest_potions");

        //mocking adding bomb to player's inventory 
        Player player = Helper.getPlayer(dmc.getCurrentGame().getEntityList());
        Bomb bomb = new Bomb("b1", dummyPos, 1);
        player.addInventory(bomb);

        //check that entities in bombs radius exist before explosion 
        assertEquals(true, dmc.getCurrentGame().getEntityList().stream().anyMatch(x ->x.getType().equals("treasure")));
        assertEquals(true, dmc.getCurrentGame().getEntityList().stream().anyMatch(x ->x.getType().equals("wall")));
        
        assertDoesNotThrow(() -> dmc.tick("b1"));

        //check that entities are destroyed after bomb explosion
        assertEquals(false, dmc.getCurrentGame().getEntityList().stream().anyMatch(x ->x.getType().equals("treasure")));
        assertEquals(false, dmc.getCurrentGame().getEntityList().stream().anyMatch(x ->x.getType().equals("wall")));
    }

    @Test 
    public void potionUse2(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("advanced", "bribe_amount_3");

        assertDoesNotThrow(() -> dmc.tick(Direction.RIGHT)); 
        assertDoesNotThrow(() -> dmc.tick(Direction.RIGHT)); 
        assertDoesNotThrow(() -> dmc.tick(Direction.UP)); 
        assertDoesNotThrow(() -> dmc.tick(Direction.UP)); 
        assertDoesNotThrow(() -> dmc.tick(Direction.UP)); 

        assertDoesNotThrow(() -> dmc.tick("2")); 

    }
} 