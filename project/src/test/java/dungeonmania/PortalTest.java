package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.getEntities;

import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTest {
    // test success
    @Test
    public void testTeleportSuccess() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_portalsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position originalPosition = controller.getCurrentGame().getPlayer().getPosition();
        controller.tick(Direction.LEFT);
        Position teleportedPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertNotEquals(teleportedPosition, originalPosition);
        // get correspodning portal position
        Position expectedPosition = getEntities(res, "portal").get(0).getPosition().translateBy(Direction.LEFT);
        assertEquals(expectedPosition, controller.getCurrentGame().getPlayer().getPosition());
    }
    
    // test multiple portals not together
    @Test
    public void teleportMultipleNotTogether() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_portalsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position originalPosition = controller.getCurrentGame().getPlayer().getPosition();
        controller.tick(Direction.LEFT);
        controller.tick(Direction.UP);
        Position teleportedPosition = controller.getCurrentGame().getPlayer().getPosition();
        assertNotEquals(teleportedPosition, originalPosition);
        boolean isInCardinal = false;
        // get correspodning portal position
        Position linkedPortalPos = getEntities(res, "portal").get(3).getPosition();
        // portal.getposition.getadj
        List<Position> adjPositions = new Distance().getCardinals(linkedPortalPos);
        // for each position
        for (Position position : adjPositions) {
            if (position.equals(controller.getCurrentGame().getPlayer().getPosition())) {
                isInCardinal = true;
            }
        }
        assertTrue(isInCardinal);
    }
    // test multiple together
    @Test
    public void teleportMultipleTogether() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_portalsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position originalPosition = controller.getCurrentGame().getPlayer().getPosition();
        controller.tick(Direction.RIGHT);
        Position teleportedPosition = controller.getCurrentGame().getPlayer().getPosition();
        System.out.println(teleportedPosition);
        assertNotEquals(teleportedPosition, originalPosition);
        boolean isInCardinal = false;
        // get correspodning portal position
        Position linkedPortalPos = getEntities(res, "portal").get(9).getPosition();
        System.out.println("FINAL PORT" + linkedPortalPos);
        // portal.getposition.getadj
        List<Position> adjPositions = new Distance().getCardinals(linkedPortalPos);
        // for each position
        for (Position position : adjPositions) {
            if (position.equals(controller.getCurrentGame().getPlayer().getPosition())) {
                isInCardinal = true;
            }
        }
        assertTrue(isInCardinal);
    }

    // test with blocking entity
    @Test
    public void teleportBlock() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_portalsTest", "c_battleTests_basicMercenaryMercenaryDies");
        Position originalPosition = controller.getCurrentGame().getPlayer().getPosition();
        controller.tick(Direction.DOWN);
        Position teleportedPosition = controller.getCurrentGame().getPlayer().getPosition();
        System.out.println(teleportedPosition);
        assertNotEquals(teleportedPosition, originalPosition);
        boolean isInCardinal = false;
        // get correspodning portal position
        Position linkedPortalPos = getEntities(res, "portal").get(7).getPosition();
        Position wallPosition = getEntities(res, "wall").get(0).getPosition();
        System.out.println("FINAL PORT" + linkedPortalPos);
        // portal.getposition.getadj
        List<Position> adjPositions = new Distance().getCardinals(linkedPortalPos);
        // for each position
        for (Position position : adjPositions) {
            if (position.equals(controller.getCurrentGame().getPlayer().getPosition())) {
                isInCardinal = true;
            }
        }
        assertNotEquals(wallPosition, teleportedPosition);
        assertTrue(isInCardinal);
    }
}
