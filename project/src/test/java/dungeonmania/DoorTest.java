package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DoorTest {
    // test locked door movement
    @Test
    public void testDoorLocked() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_doorsTest", "c_battleTests_basicMercenaryMercenaryDies");
        // get position before movement
        Position expectedPosition = controller.getCurrentGame().getPlayer().getPosition();
        controller.tick(Direction.RIGHT);
        Position actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        // assert before and after is same
        assertEquals(expectedPosition, actualPosition);
    }
    
    @Test 
    public void testOpenDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_doorsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(0, 2);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        Position actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test 
    public void testWrongKeyId() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_doorsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(Direction.UP);
        controller.tick(Direction.UP);
        controller.tick(Direction.RIGHT);
        Position actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertEquals(expectedPosition, actualPosition);
    }

    // multiple
    @Test
    public void testMultipleKeys() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_doorsTest", "c_battleTests_basicMercenaryMercenaryDies");
        // First door
        Position expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(0, 2);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        Position actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertEquals(expectedPosition, actualPosition);
        // 2nd door
        expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(1, -2);
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.RIGHT);
        actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertEquals(expectedPosition, actualPosition);
        // 3rd door
        expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(0, -1);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.UP);
        controller.tick(Direction.UP);
        controller.tick(Direction.LEFT);
        controller.tick(Direction.DOWN);
        actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertEquals(expectedPosition, actualPosition);
    }
    // test cant pick up multiple keys
    @Test
    public void testCantPickUpMultipleKeys() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_doorsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(Direction.DOWN);
        controller.tick(Direction.UP);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        Position actualPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertEquals(expectedPosition, actualPosition);
    }
}
