package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.LightBulb;
import dungeonmania.StaticEntities.SwitchDoor;
import dungeonmania.games.Game;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.StaticEntities.Wire;

public class LogicTest {
    // simple test to check wire works and light bulb can turn on/off
    @Test
    public void turnOnOffBulb(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_1", "c_battleTests_basicMercenaryMercenaryDies");
        controller.tick(Direction.UP);
        Entity lightBulbAfter = controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().get();
        // light bulb should be off
        assertNotEquals("light_bulb_off", lightBulbAfter.getType());
        controller.tick(Direction.UP);
        // light bulb turned off
        assertEquals(lightBulbAfter.getType(), "light_bulb_off");
    }   

    // same as above but for switch door
    @Test
    public void OpenCloseSwitchDoor(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_1", "c_battleTests_basicMercenaryMercenaryDies");

        Position doorPos = controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("switch_door")).findFirst().get().getPosition();
        // move lightbulb boulder
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);

        // active switch door
        controller.tick(Direction.LEFT);
        SwitchDoor door = (SwitchDoor) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("switch_door")).findFirst().get();
        assertFalse(door.getIsLocked());

        // try to walk through door - Success
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.LEFT);
        Player player = (Player) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("player")).findFirst().get();
        assertEquals(door.getPosition(), player.getPosition());

        // deactivate switchdoor
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        assertTrue(door.getIsLocked());

        // try to walk through door - Fail
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        assertNotEquals(door.getPosition(), player.getPosition());
    }   
/* 
    // two switches connect by wire, if one turns on, it turns on the connected switch
    // wires activated as long as they are connected to 1 active entity - 2 switches on, turn 1 off
    @Test
    public void twoConnectedSwitches(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_2", "c_battleTests_basicMercenaryMercenaryDies");
        Game game = controller.getCurrentGame();
        FloorSwitch switch1 = new FloorSwitch("s1", new Position(2, 4), "or");
        FloorSwitch switch2 = new FloorSwitch("s1", new Position(5, 7), "or");
        // turn on switch1
        controller.tick(Direction.UP);
        // check if switch2 is on
        assertTrue(switch2.isTriggered(game));
        assertTrue(switch1.isTriggered(game));
        // turn on switch2
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        // check if both switches still on
        assertTrue(switch2.isTriggered(game));
        assertTrue(switch1.isTriggered(game));
        // now turn off switch 1
        controller.tick(Direction.LEFT);
        controller.tick(Direction.LEFT);
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        // check if switches are still on
        assertTrue(switch2.isTriggered(game));
        assertTrue(switch1.isTriggered(game));
    }


    //  check bomb detonates from wire + switch
    @Test
    public void denoteLogicBombAndNormalBomb(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_2", "c_battleTests_basicMercenaryMercenaryDies");
        Game game = controller.getCurrentGame();
        FloorSwitch switch1 = new FloorSwitch("s1", new Position(2, 4), "or");
        FloorSwitch switch2 = new FloorSwitch("s2", new Position(5, 7), "or");
        game.addEntitiy(switch1);
        game.addEntitiy(switch2);
        List<Entity> bombs = game.getEntityList().stream().filter(i -> i.getType().equals("bomb")).collect(Collectors.toList());
        Entity switchBomb = bombs.get(0);
        Entity normalBOmbEntity = bombs.get(1);
        // turn on switch1 which explodes switch bomb only
        controller.tick(Direction.UP);
        assertTrue(switch1.isTriggered(game));
        List<Entity> wiresAfter = game.getEntityList().stream().filter(i -> i.getType().equals("wire")).collect(Collectors.toList());

        assertEquals(true, ((Wire) wiresAfter.stream().filter(i -> i.getPosition().equals(new Position(1, 4))).findFirst().get()).getState());
        assertEquals(true, ((Wire) wiresAfter.stream().filter(i -> i.getPosition().equals(new Position(3, 4))).findFirst().get()).getState());
        System.out.println(game.getEntityList());
        List<Entity> bombsAfter = game.getEntityList().stream().filter(i -> i.getType().equals("bomb")).collect(Collectors.toList());
        // doesnt exist so wont work
        //Entity Bomb1After = bombsAfter.get(0);
        // check if switch bomb exists
        assertEquals(1, bombsAfter.size());
        // check if normal bomb exists
        //assertEquals(normalBOmbEntity.getId(), Bomb1After.getId());
        //assertNotEquals(switchBomb, Bomb1After);
    }

    // AND light bulb - all 3 adj switches activated to turn on bulb
    @Test
    public void threeAdjacentANDBulb(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_3", "c_battleTests_basicMercenaryMercenaryDies");
        Game game = controller.getCurrentGame();

        Optional<Entity> lightBulb = game.getEntityList().stream().filter(i -> i.getType().equals("light_bulb_off")).findFirst();

        // turn on switch which turns on light bulb
        controller.tick(Direction.UP);
        assertEquals(lightBulb.get().getType(), "light_bulb_on");
    }

    // OR, CO_AND, XOR, AND light bulb turns on and switch doors open/close
    // turn on other switch, light should remain on
    @Test
    public void allLogics(){
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_4", "c_battleTests_basicMercenaryMercenaryDies");
        Game game = controller.getCurrentGame();
        // create light bulbs
        LightBulb ANDBulb = new LightBulb("lb1", new Position(5, 4), "and");
        LightBulb ORBulb = new LightBulb("lb2", new Position(6, 4), "or");
        LightBulb CO_ANDBulb = new LightBulb("lb3", new Position(5, 6), "co_and");
        LightBulb XORBulb = new LightBulb("lb4", new Position(6, 6), "xor");

        // turn on switch which turns on all light bulbs
        controller.tick(Direction.LEFT);
        assertEquals(ANDBulb.getType(), "light_bulb_on");
        assertEquals(ORBulb.getType(), "light_bulb_on");
        assertEquals(CO_ANDBulb.getType(), "light_bulb_on");
        assertEquals(XORBulb.getType(), "light_bulb_on");

        // turn on other switch
        controller.tick(Direction.DOWN);
        controller.tick(Direction.LEFT);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.RIGHT);
        // check lights stay on
        assertEquals(ANDBulb.getType(), "light_bulb_on");
        assertEquals(ORBulb.getType(), "light_bulb_on");
        assertEquals(CO_ANDBulb.getType(), "light_bulb_on");
        assertEquals(XORBulb.getType(), "light_bulb_on");
    }
    */

    // switch without logic fields cannot be activated by wires
    @Test
    public void normalSwitch() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_3", "c_battleTests_basicMercenaryMercenaryDies");
        Game game = controller.getCurrentGame();
        FloorSwitch logicSwitch = new FloorSwitch("s1", new Position(6, 10), "or");
        FloorSwitch normalSwitch = new FloorSwitch("s1", new Position(7, 9));
        assertFalse(logicSwitch.isTriggered(game));
        assertFalse(normalSwitch.isTriggered(game));
        // turn on switch
        controller.tick(Direction.RIGHT);
        assertTrue(logicSwitch.isTriggered(game));
        assertFalse(normalSwitch.isTriggered(game));
    }
    /* 
    // switch with logic fields
    @Test
    public void AndXorCo_andOrSwitches() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_4", "c_battleTests_basicMercenaryMercenaryDies");
        Game game = controller.getCurrentGame();
        // create switches
        FloorSwitch ANDSwitch = new FloorSwitch("s1", new Position(5, 4), "and");
        FloorSwitch ORSwitch = new FloorSwitch("s2", new Position(6, 4), "or");
        FloorSwitch CO_ANDSwitch = new FloorSwitch("s3", new Position(5, 6), "co_and");
        FloorSwitch XORSwitch = new FloorSwitch("s4", new Position(6, 6), "xor");
        // check switches are off
        assertFalse(ANDSwitch.isTriggered(game));
        assertFalse(ORSwitch.isTriggered(game));
        assertFalse(CO_ANDSwitch.isTriggered(game));
        assertFalse(XORSwitch.isTriggered(game));
        // turn on switch
        controller.tick(Direction.RIGHT);
        assertTrue(ANDSwitch.isTriggered(game));
        assertTrue(ORSwitch.isTriggered(game));
        assertTrue(CO_ANDSwitch.isTriggered(game));
        assertTrue(XORSwitch.isTriggered(game));
    }
    */
    @Test
    public void longWireBomb() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_logicTest_5", "c_battleTests_basicMercenaryMercenaryDies"); 
        assertTrue(controller.getCurrentGame().getEntityList().stream().anyMatch(i -> i.getType().equals("bomb")));
        controller.tick(Direction.DOWN);
        assertFalse(controller.getCurrentGame().getEntityList().stream().anyMatch(i -> i.getType().equals("bomb")));
    }
}
