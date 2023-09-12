package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
public class PlayerMovementTest {

    @Test
    public void simpleMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_testMovementDown", "c_spiderTest_basicMovement");
        assertTrue(getPlayer(res).get().getPosition().equals(new Position(1,1)));
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getPlayer(actualDungonRes).get().getPosition().equals(new Position(1,2)));
    }

    @Test
    @DisplayName("Test for adding item to inventory and removing it from entity list")
    public void collectItem() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_collectItem", "c_spiderTest_basicMovement");

        assertTrue(getPlayer(res).get().getPosition().equals(new Position(1,1)));
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getPlayer(actualDungonRes).get().getPosition().equals(new Position(1,2)));
        assertEquals(actualDungonRes.getInventory().get(0).getType(), "treasure");
        
        Boolean treasureOnMap = false;
        for (EntityResponse entity : actualDungonRes.getEntities()) {
            if (entity.getType() == "treasure") {
                treasureOnMap = true;
            }
        }

        assertFalse(treasureOnMap);
    }

    @Test
    @DisplayName("Test that only one key is allowed in inventory")
    public void twoKeysInInventory() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_2doors", "c_spiderTest_basicMovement");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(0).getType(), "key");
        assertEquals(res.getInventory().size(), 1);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(res.getInventory().size(), 1);
    }
    
    @Test
    @DisplayName("Test for that blockable entities are implemented correctly. Player doesn't move")
    public void movementBlocked() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_blocked", "c_movementTest_testMovementDown");

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(1, 1), getPlayer(res).get().getPosition());

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 1), getPlayer(res).get().getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), getPlayer(res).get().getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getPlayer(res).get().getPosition());
    }

    @Test
    @DisplayName("Test for boulders, switches, doors and spawners. Player can move")
    public void movementUnblocked() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_unblock", "c_movementTest_testMovementDown");
        assertEquals("(:exit AND :boulders)", res.getGoals());

        // push boulder onto switch
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 2), getPlayer(res).get().getPosition());
        assertEquals(":exit", res.getGoals());
        assertTrue(res.getInventory().isEmpty());

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(0).getType(), "key");
        assertEquals(new Position(2, 2), getPlayer(res).get().getPosition());

        // unlock door
        res = dmc.tick(Direction.RIGHT);
        assertTrue(res.getInventory().isEmpty());
        assertEquals(new Position(3, 2), getPlayer(res).get().getPosition());

        // walk over spawner
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 2), getPlayer(res).get().getPosition());

        // exit
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 2), getPlayer(res).get().getPosition());
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test for walking over collectibles. Player can move")
    public void movementCollectibles() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_collectibles", "c_movementTest_testMovementDown");
        assertEquals("(:exit AND :treasure)", res.getGoals());

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), getPlayer(res).get().getPosition());
        assertEquals(":exit", res.getGoals());
        assertEquals(res.getInventory().get(0).getType(), "treasure");
        assertEquals(res.getInventory().size(), 1);

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(1).getType(), "key");
        assertEquals(res.getInventory().size(), 2);
        assertEquals(new Position(3, 1), getPlayer(res).get().getPosition());

        // pick up invincibility_potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(2).getType(), "invincibility_potion");
        assertEquals(res.getInventory().size(), 3);
        assertEquals(new Position(4, 1), getPlayer(res).get().getPosition());

        // pick up invisibility_potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(3).getType(), "invisibility_potion");
        assertEquals(res.getInventory().size(), 4);
        assertEquals(new Position(5, 1), getPlayer(res).get().getPosition());

        // pick up wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(4).getType(), "wood");
        assertEquals(res.getInventory().size(), 5);
        assertEquals(new Position(6, 1), getPlayer(res).get().getPosition());

        // pick up arrow
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(5).getType(), "arrow");
        assertEquals(res.getInventory().size(), 6);
        assertEquals(new Position(7, 1), getPlayer(res).get().getPosition());

        // pick up bomb
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(6).getType(), "bomb");
        assertEquals(res.getInventory().size(), 7);
        assertEquals(new Position(8, 1), getPlayer(res).get().getPosition());

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().get(7).getType(), "sword");
        assertEquals(res.getInventory().size(), 8);
        assertEquals(new Position(9, 1), getPlayer(res).get().getPosition());

        // exit
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(10, 1), getPlayer(res).get().getPosition());
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test that having wrong key for door")
    public void wrongKeyForDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_2doors", "c_spiderTest_basicMovement");

        // Get key 1
        res = dmc.tick(Direction.DOWN);
        assertEquals(res.getInventory().get(0).getType(), "key");
        assertEquals(res.getInventory().size(), 1);

        // Try to go through door 2 - Fail
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getInventory().size(), 1);
        assertEquals(new Position(2, 4), getPlayer(res).get().getPosition());
    }

    @Test
    @DisplayName("Test that player can move through already opened door")
    public void movementThroughOpenedDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_2doors", "c_spiderTest_basicMovement");

        // Get key 1
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(2, 3), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().get(0).getType(), "key");
        assertEquals(res.getInventory().size(), 1);
        

        // Go through door 1 - Success
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 3), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().size(), 0);

        // Go out the door and come back through unlocked door
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 3), getPlayer(res).get().getPosition());
    }

    @Test
    @DisplayName("Unlock door with sunstone")
    public void movementThroughDoorSunstone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstone_unlockDoors", "simple");

        // Get sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 2), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().get(0).getType(), "sun_stone");
        assertEquals(res.getInventory().size(), 1);

        // Go through door 1 - Success - Sunstone not removed
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 3), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().get(0).getType(), "sun_stone");
        assertEquals(res.getInventory().size(), 1);

        // Go through door 2 - Success - Sunstone not removed
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 4), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().get(0).getType(), "sun_stone");
        assertEquals(res.getInventory().size(), 1);
    }

    @Test
    @DisplayName("Test that player can move through portals")
    public void movementPortal() {

    }
    
}
