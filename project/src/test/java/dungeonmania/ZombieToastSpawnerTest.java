package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.getEntities;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawnerTest {
    @Test
    public void testSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_zombieToastSpawnerTest", "c_battleTest_basicZombieToast");
        Position spawnerPosition = getEntities(res, "zombie_toast_spawner").get(0).getPosition();

        for (int i = 0; i < 5; i++) {
            controller.tick(Direction.LEFT);
        }
        Position zombiePos = null;
        List<Entity> entities = controller.getCurrentGame().getEntityList();
        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast")){
                zombiePos = entity.getPosition();
            }
        }
        boolean cardZombie = false;
        List<Position> adjPositions = new Distance().getCardinals(spawnerPosition);
        for (Position position : adjPositions) {
            if (position.equals(zombiePos)) {
                cardZombie = true;
            }
        }
        assertTrue(cardZombie);
    }

    @Test
    public void testBlockedCompletelySpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_blockedSpawnerTest", "c_battleTest_basicZombieToast");
        Position spawnerPosition = getEntities(res, "zombie_toast_spawner").get(0).getPosition();

        for (int i = 0; i < 5; i++) {
            controller.tick(Direction.LEFT);
        }
        Position zombiePos = null;
        List<Entity> entities = controller.getCurrentGame().getEntityList();
        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast")){
                zombiePos = entity.getPosition();
            }
        }
        boolean cardZombie = false;
        List<Position> adjPositions = new Distance().getCardinals(spawnerPosition);
        for (Position position : adjPositions) {
            if (position.equals(zombiePos)) {
                cardZombie = true;
            }
        }
        assertFalse(cardZombie);
        assertNull(zombiePos);
    }

    @Test
    public void testBlockedPartiallySpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_blockPartiallyTest", "c_battleTest_basicZombieToast");
        Position spawnerPosition = getEntities(res, "zombie_toast_spawner").get(0).getPosition();

        for (int i = 0; i < 5; i++) {
            controller.tick(Direction.LEFT);
        }
        Position zombiePos = null;
        List<Entity> entities = controller.getCurrentGame().getEntityList();
        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast")){
                zombiePos = entity.getPosition();
            }
        }
        boolean cardZombie = false;
        List<Position> adjPositions = new Distance().getCardinals(spawnerPosition);
        for (Position position : adjPositions) {
            if (position.equals(zombiePos)) {
                cardZombie = true;
            }
        }
        assertTrue(cardZombie);
    }
}
