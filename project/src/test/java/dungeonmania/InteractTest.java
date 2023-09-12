package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.countEntityOfType;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;


public class InteractTest {
    @Test
    @DisplayName("Test for destroy spawner - with sword")
    public void testDestroySpawnerSuccess() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("interactSpawner", "c_battleTests_basicMercenaryMercenaryDies");
        controller.tick(Direction.RIGHT);
        assertTrue(res.getEntities().stream().anyMatch(i -> i.getType().equals("zombie_toast_spawner")));

        assertDoesNotThrow(() -> controller.interact("3"));
        res = controller.getDungeonResponseModel();
        assertEquals(0, countEntityOfType(res, "zombie_toast_spawner"));
    }

    @Test
    @DisplayName("Test for destroy spawner - without sword")
    public void testDestroySpawnerFail() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("interactSpawner", "c_battleTests_basicMercenaryMercenaryDies");
        controller.tick(Direction.DOWN);
        String Id = getEntities(res, "zombie_toast_spawner").get(0).getId();
        assertThrows(InvalidActionException.class, () -> controller.interact(Id));
        res = controller.getDungeonResponseModel();
    }

    @Test
    @DisplayName("Test for mercenary bribe success")
    public void testBribeMercenarySuccess() {
        DungeonManiaController controller = new DungeonManiaController();
        // create new dungeon with mercenary + treasure + bribeaemout from config
        DungeonResponse res = controller.newGame("d_mercenaryTest_bribe", "c_battleTests_basicMercenaryMercenaryDies");
        String mercId = getEntities(res, "mercenary").get(0).getId();

        // move to pick up treasure - get bribe amout

        assertEquals(1, countEntityOfType(res, "treasure"));
        res = controller.tick(Direction.RIGHT);
        assertEquals(0, countEntityOfType(res, "treasure"));
        assertEquals(1, getInventory(res, "treasure").size());
        assertDoesNotThrow(() -> controller.interact(mercId));
        res = controller.tick(Direction.LEFT);
        assertEquals(0, countEntityOfType(res, "treasure"));

    }

    @Test
    @DisplayName("Test for invalid interact id ")
    public void testInvalidInteractId() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_mercenaryTest_bribe", "c_mercenary_2treasuresNeeded");
        controller.tick(Direction.LEFT);
        assertThrows(IllegalArgumentException.class, () -> controller.interact("121"));
    }

    // bribe radius 1 , bribe amount 2
    @Test
    @DisplayName("Test for mercenary bribe failed -> enough money not radius ")
    public void testBribeMercenaryFailureRadius() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_merc_notInRadius", "c_mercenary_2treasuresNeeded");
        String mercId = getEntities(res, "mercenary").get(0).getId();
        res = controller.tick(Direction.DOWN);
        res = controller.tick(Direction.DOWN);
        assertEquals(2, getInventory(res, "treasure").size());
        assertThrows(InvalidActionException.class, () -> controller.interact(mercId));

    }


    @Test
    @DisplayName("Test for mercenary bribe failed -> not enought money but in radius ")
    public void testBribeMercenaryFailureMoney() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_mercenaryTest_bribe", "c_mercenary_2treasuresNeeded");
        String mercId = getEntities(res, "mercenary").get(0).getId();
        res = controller.tick(Direction.LEFT);
        res = controller.tick(Direction.RIGHT);
        assertEquals(0, getInventory(res, "treasure").size());
        assertThrows(InvalidActionException.class, () -> controller.interact(mercId));
    }


    // NOT COMPLETE??? IT WORKS BUT WHY DID IT NOT WORK ON FRONTEND ??????
    @Test
    @DisplayName("Test that all 3 treasures are used up  ")
    public void testMoneyIsUsed() {


    }


    @Test
    @DisplayName("Test bribe assassin fails")
    public void assassinBribeFails() {

    }


    @Test
    @DisplayName("Test bribe assassin fails")
    public void assassinBribeSucceeds() {

    }




}
