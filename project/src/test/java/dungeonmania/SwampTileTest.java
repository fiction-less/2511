package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.MovingEntities.Assassin;
import dungeonmania.MovingEntities.Hydra;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.SwampTile;;


public class SwampTileTest {
    // test for mulitple movement factors
    // assuming movement of all entities will move them by the only option available
    @Test
    public void zombieSwamp() {
        // movement factor = 1
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_zombie", "c_battleTests_basicMercenaryMercenaryDies");
        ZombieToast zombie = (ZombieToast) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("zombie_toast")).findAny().get();
        controller.tick(Direction.DOWN);
        assertEquals(new Position(3,3), zombie.getPosition());
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        assertTrue(new Position(2,3).equals(zombie.getPosition()) || new Position(4,3).equals(zombie.getPosition()));
    }
    @Test
    public void mercenarySwamp() {
        // movement factor = 2
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_mercenary", "c_battleTests_basicMercenaryMercenaryDies");
        Mercenary mercenary = (Mercenary) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("mercenary")).findAny().get();
        Position initMercenaryPos = mercenary.getPosition();
        int i = 0;
        while (i < 4) {
            controller.tick(Direction.DOWN);
            i++;
        }
        Position expectedPosition = new Position(2,3);
        assertNotEquals(initMercenaryPos, mercenary.getPosition());
        assertEquals(expectedPosition, mercenary.getPosition());
    }
    
    @Test
    public void playerSwamp() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_MF", "c_battleTests_basicMercenaryMercenaryDies");
        Player player = controller.getCurrentGame().getPlayer();
        Position initPlayerPos = player.getPosition();
        controller.tick(Direction.LEFT);
        controller.tick(Direction.UP);
        Position expectedPosition = new Position(4,5);
        assertNotEquals(initPlayerPos, player.getPosition());
        assertEquals(expectedPosition, player.getPosition());
        initPlayerPos = player.getPosition();
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        expectedPosition = new Position(6,5);
        assertNotEquals(initPlayerPos, player.getPosition());
        assertEquals(expectedPosition, player.getPosition());
        initPlayerPos = player.getPosition();
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        expectedPosition = new Position(6,7);
        assertNotEquals(initPlayerPos, player.getPosition());
        assertEquals(expectedPosition, player.getPosition());
        initPlayerPos = player.getPosition();
        controller.tick(Direction.LEFT);
        controller.tick(Direction.LEFT);
        expectedPosition = new Position(4,7);
        assertNotEquals(initPlayerPos, player.getPosition());
        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void boudlerSwamp() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_zombie", "c_battleTests_basicMercenaryMercenaryDies");
        Player player = controller.getCurrentGame().getPlayer();
        Position initPlayerPos = player.getPosition();
        controller.tick(Direction.LEFT);
        controller.tick(Direction.LEFT);
        Position expectedPosition = new Position(3,6);
        assertNotEquals(initPlayerPos, player.getPosition());
        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hydraSwamp() {
        // movement factor = 10
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_bosses", "M3_config");
        Hydra hydra = (Hydra) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("hydra")).findAny().get();
        Position initHydraPos = hydra.getPosition();
        int i = 0;
        while (i < 12) {
            controller.tick(Direction.DOWN);
            System.out.println(hydra.getPosition());
            i++;
        }
        Position expectedPosition = new Position(7,2);
        assertTrue(initHydraPos.equals(hydra.getPosition()) || expectedPosition.equals(hydra.getPosition()));
    }
    @Test
    public void assassinSwamp() {
        // movement factor = 15
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_bosses", "M3_config");
        Hydra hydra = (Hydra) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("hydra")).findAny().get();
        Assassin assassin = (Assassin) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("assassin")).findAny().get();
        Position initAssassinPos = assassin.getPosition();
        int i = 0;
        while (i < 17) {
            controller.tick(Direction.DOWN);
            i++;
        }
        Position expectedPosition = new Position(2,3);
        assertNotEquals(initAssassinPos, assassin.getPosition());
        assertEquals(expectedPosition, assassin.getPosition());
    }

    @Test
    public void movementFactorZeroSwamp() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_swampTest_MF", "c_battleTests_basicMercenaryMercenaryDies");
        ZombieToast zombie = (ZombieToast) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("zombie_toast")).findAny().get();
        Position initZombiePos = zombie.getPosition();
        controller.tick(Direction.DOWN);
        controller.tick(Direction.DOWN);
        Position expectedPosition = new Position(2,3);
        assertTrue(expectedPosition.equals(zombie.getPosition()) || zombie.getPosition().equals(initZombiePos));
    }

    // test spider
    @Test
    public void spiderSwamp() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_spiderSwamp", "c_battleTests_basicMercenaryMercenaryDies");
        Spider spider = (Spider) controller.getCurrentGame().getEntityList().stream().filter(e -> e.getType().equals("spider")).findAny().get();
        Position initSpiderPos = spider.getPosition();
        controller.tick(Direction.DOWN);
        assertNotEquals(initSpiderPos, spider.getPosition());
        SwampTile st = (SwampTile) controller.getCurrentGame().getEntitiesAtPosition(spider.getPosition()).stream().filter(i -> i.getType().equals("swamp_tile")).findAny().get();
        controller.tick(Direction.DOWN);
        assertTrue(st.getPosition().equals(spider.getPosition()));
        controller.tick(Direction.DOWN);
        assertTrue(st.getPosition().equals(spider.getPosition()));
        controller.tick(Direction.DOWN);
        assertFalse(st.getPosition().equals(spider.getPosition()));
    }


}
