package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class MovingEntitiesTests {


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                             SPIDER                                               //
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test basic movement of spiders")
    public void basicMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            System.out.println(getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test movement of trapped Spider")
    public void idleMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_trappedMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x, y-1));
        movementTrajectory.add(new Position(x, y-1));
        movementTrajectory.add(new Position(x, y-1));
        movementTrajectory.add(new Position(x, y-1));
        movementTrajectory.add(new Position(x, y-1));
        movementTrajectory.add(new Position(x, y-1));
        movementTrajectory.add(new Position(x, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            System.out.println(getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test movement of reversed Spider boulder ahead")
    public void boulderAheadMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderAhead", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x, y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));

        // Assert reversed Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            //System.out.println(movementTrajectory.get(nextPositionElement));
            System.out.println(getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
            if (nextPositionElement == 12){
                nextPositionElement = 0;
            }
        }
    }


    @Test
    @DisplayName("Test movement of reversed Spider")
    public void reverseMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_reversedMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x, y+1));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x, y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert reversed Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            System.out.println(getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
            if (nextPositionElement == 12){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test a spider spawns every 2 ticks")
    public void spawnSpider() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_newGameTest_simpleEntities", "c_spiderTest_spawnRate2");
        res = dmc.tick(Direction.UP);
        assertEquals(0, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.UP);
        assertEquals(1, countEntityOfType(res, "spider"));

        for (int i = 0; i < 20; ++i) {
            res = dmc.tick(Direction.UP);
        }
        assertEquals(11, countEntityOfType(res, "spider"));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                             ZOMBIE                                               //
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    @DisplayName("Test a zombie spawns every 2 ticks from ONE zombie spawner")
    public void spawnZombieOneToaster() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawner", "c_spawnsZombie2ticks");
        System.out.println("Starts ticking ");
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, countEntityOfType(res, "zombie_toast_spawner"));
        assertEquals(1, countEntityOfType(res, "zombie_toast"));

        for (int i = 0; i < 20; ++i) {
            res = dmc.tick(Direction.UP);
        }
        assertEquals(11, countEntityOfType(res, "zombie_toast"));
    }

    // zombie cant spawn cause things itcant pass are blocking all cardinally adjacent positions from toaster
    @DisplayName("Test a zombie cant spawn because toaster is blocked")
    public void cantSpawnZombie() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToasterSpawner_blocked", "c_spawnsZombie2ticks");
        System.out.println("Starts ticking ");
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, countEntityOfType(res, "zombie_toast_spawner"));
        assertEquals(0, countEntityOfType(res, "zombie_toast"));

        for (int i = 0; i < 20; ++i) {
            res = dmc.tick(Direction.UP);
        }
        assertEquals(0, countEntityOfType(res, "zombie_toast"));
    }

    // tests that zombie cant pass through certain items
    // cant pass -> boulders, walls, locked door
    // can  pass -> portals, toasters
    // - - - - - -
    // - - - B B -
    // - P D - T W
    // - - - D D -
    // - - - - - K K K
    // P -> player T->toaster D->door B->boulder
    @Test
    @DisplayName("Test contained movement of zombie -> can pass thru toaster")
    public void zombieContaineddMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToast_move", "c_spawnsZombie2ticks");
        System.out.println("Starts ticking ");

        Position pos = getEntities(res, "zombie_toast_spawner").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();

        res = dmc.tick(Direction.UP);
        assertEquals(0, countEntityOfType(res, "zombie_toast"));
        res = dmc.tick(Direction.UP);
        assertEquals(1, countEntityOfType(res, "zombie_toast"));
        assertEquals(new Position(x-1, y), getEntities(res, "zombie_toast").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(x, y), getEntities(res, "zombie_toast").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(x-1, y), getEntities(res, "zombie_toast").get(0).getPosition());
    }

    // trapppes the zombie so that its movement becomes idle
    @Test
    @DisplayName("Test a zombie trapped movement -> player moves boulder onto zombie toaster")
    public void zombieTrapped() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToast_trapped", "c_spawnsZombie2ticks");

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        Position pos = getEntities(res, "zombie_toast").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();
        res = dmc.tick(Direction.LEFT);
        for (int i = 0; i < 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(new Position(x, y), getEntities(res, "zombie_toast").get(0).getPosition());
        }

    }


    @Test
    @DisplayName("Zombie run away from invincible player, spawns every 2 ticks")
    public void zombieRunAway() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToast_runAway", "c_maxInvincibility");

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertThrows(InvalidActionException.class, () -> dmc.tick("20"));
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        String id = getInventory(res, "invincibility_potion").get(0).getId();
        assertDoesNotThrow(() -> dmc.tick(id));
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);

        Position pos = getEntities(res, "zombie_toast").get(3).getPosition();
        int x = pos.getX();
        int y = pos.getY();

        for (int i = 1; i <= 2; ++i) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(new Position(x + i, y), getEntities(res, "zombie_toast").get(3).getPosition());
        }
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(x + 2, y + 1), getEntities(res, "zombie_toast").get(3).getPosition());
    }


    @Test
    @DisplayName("Test movement of zombie")
    public void zombieMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicZombieToast", "c_spawnsZombie2ticks");
        // tick tceiw to spawn zombie
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        Position pos = getEntities(res, "zombie_toast").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();

        for (int i = 0; i < 10; i++) {
            List<Position> expected = Arrays.asList(new Position(x-1, y),new Position(x+1, y), new Position(x, y-1),new Position(x, y+1));
            expected.stream().forEach(k -> System.out.println(k));
            res = dmc.tick(Direction.LEFT);
            assertTrue(expected.contains(getEntities(res, "zombie_toast").get(0).getPosition()));

            x = getEntities(res, "zombie_toast").get(0).getPosition().getX();
            y = getEntities(res, "zombie_toast").get(0).getPosition().getY();
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          MERCENARY                                               //
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Merc follows player in a straight line")
    public void mercFollowStraightLine() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary_testMovement", "c_testMerc_movement");

        res = dmc.tick(Direction.UP);
        System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());

        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();

        for (int i = 1; i <= 8; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(new Position(x, y - i), getEntities(res, "mercenary").get(0).getPosition());
        }
    }


    @Test
    public void mercEnclosedSpace() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary_testMovement", "c_testMerc_movement");

        res = dmc.tick(Direction.UP);
        System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());

        for (int i = 0; i <= 2; ++i) {
            res = dmc.tick(Direction.UP);
            System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
            System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
            dmc.getCurrentGame().getEntityList().stream().forEach(k -> System.out.println(k.getType()));
            getEntities(res, "mercenary").get(0).getPosition();
        }
        for (int i = 0; i <= 5; ++i) {
            System.out.println(i);
            res = dmc.tick(Direction.DOWN);
            System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
            System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
            dmc.getCurrentGame().getEntityList().stream().forEach(k -> System.out.println(k.getType()));
            getEntities(res, "mercenary").get(0).getPosition();
        }
    }

    @Test
    @DisplayName("Merc tails the player if an ally. (tail means next to it) if player is idle, it is also idle")
    public void allyMercTail () {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_mercenaryTest_bribe", "c_battleTests_basicMercenaryMercenaryDies");
        String mercId = getEntities(res, "mercenary").get(0).getId();

        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();

        // move to pick up treasure - get bribe amout

        res = controller.tick(Direction.RIGHT); // follow 1 tick
        System.out.println("Player Position" + controller.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());

        assertEquals(new Position(x-1, y), getEntities(res, "mercenary").get(0).getPosition());
        assertDoesNotThrow(() -> controller.interact(mercId)); // bribes.



        //controller.getCurrentGame().getEntityList().stream().forEach(k -> System.out.println(k.getType()));
        Position posP = getEntities(res, "player").get(0).getPosition();
        int Px = pos.getX();
        int Py = pos.getY();

        for (int i = 0; i <= 5; ++i) {
            res = controller.tick(Direction.RIGHT);
            System.out.println("Player Position" + controller.getCurrentGame().getPlayer().getPosition());
            System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
        }
        for (int i = 0; i <2; ++i) {
            res = controller.tick(Direction.LEFT);
            System.out.println("Player Position" + controller.getCurrentGame().getPlayer().getPosition());
            System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
        }
        res = controller.tick(Direction.UP);
        System.out.println("Player Position" + controller.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
        res = controller.tick(Direction.RIGHT);
        System.out.println("Player Position" + controller.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    @DisplayName("Player is invisible -> merc go to random movement. Does not test if it reverts back ")
    public void mercInvisible () {
        DungeonManiaController dmc =  new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mer_PlayerInvis", "c_merc_playerInvis");

        res = dmc.tick(Direction.RIGHT); // gets potion

        System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());
        String id = getInventory(res, "invisibility_potion").get(0).getId();


        System.out.println("srink potion\n");
        assertDoesNotThrow(() -> dmc.tick(id)); // hould be random movement now
        System.out.println("drunk potion");


        res = dmc.tick(Direction.RIGHT);
        System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
        System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());

        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();


        for (int i = 0; i < 10; i++) {
            List<Position> expected = Arrays.asList(new Position(x-1, y),new Position(x+1, y), new Position(x, y-1),new Position(x, y+1));
            res = dmc.tick(Direction.RIGHT);

            System.out.println("Player Position" + dmc.getCurrentGame().getPlayer().getPosition());
            System.out.println("Enemy position" + getEntities(res, "mercenary").get(0).getPosition());

            assertTrue(expected.contains(getEntities(res, "mercenary").get(0).getPosition()));
            x = getEntities(res, "mercenary").get(0).getPosition().getX();
            y = getEntities(res, "mercenary").get(0).getPosition().getY();
        }

    }


    @Test
    public void mercRunaway () {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_merc_runaway", "c_maxInvincibility");

        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();

          // picks up a potions
        res = dmc.tick(Direction.RIGHT);
        String id = getInventory(res, "invincibility_potion").get(0).getId();

        assertEquals(new Position(x - 1, y), getEntities(res, "mercenary").get(0).getPosition());
        assertDoesNotThrow(() -> dmc.tick(id)); // tick 2
        res = dmc.tick(Direction.RIGHT); // tick 3
        assertEquals(new Position(x + 1, y), getEntities(res, "mercenary").get(0).getPosition()); // runing away now
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(x + 2, y), getEntities(res, "mercenary").get(0).getPosition()); // runing away now
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(x + 2, y + 1), getEntities(res, "mercenary").get(0).getPosition()); // runing away now
    }

    @Test
    public void mercRevertFromPotion () {
        // revert fom invincibility

        // revernt from isvisibility

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          BOSSES                                                  //
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // invisibility duration: 4

    // "assassin_attack": 10,
    // "assassin_bribe_fail_rate": 0.3,
    // "assassin_health": 10,

    // "assassin_recon_radius": 3,

    // "assassin_bribe_amount": 2,
    // assassin birbe radius : 2

    // "mind_control_duration": 3,

    // "hydra_attack": 10,
    // "hydra_health": 10,
    // "hydra_health_increase_amount": 1,
    // "hydra_health_increase_rate": 0.5,
    // "hydra_spawn_rate": 0,
    @Test
    @DisplayName("test general movement of assassins")
    public void assasinMovement () {

    }

    @Test
    @DisplayName("test movement of assassins within recon range of invisible player " )
    public void assasinWithinReconRange () {

        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin_invisible_treasure", "c_bosses_config");
        Position pos = getEntities(res, "assassin").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();
        // gets potion
        res = dmc.tick(Direction.DOWN);
        String id = getInventory(res, "invisibility_potion").get(0).getId();
        assertEquals(new Position(x, y - 1), getEntities(res, "assassin").get(0).getPosition());
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(x, y - 2), getEntities(res, "assassin").get(0).getPosition());

        // drinks potion
        assertDoesNotThrow(() -> dmc.tick(id)); // hould be random movement now
        res = dmc.tick(Direction.DOWN);
        assertEquals(0, countEntityOfType(res, "assassin")); // assassin is killed.
    }

    @Test
    @DisplayName("test movement of assassins outside recon range of invisible player " )
    public void assasinOutsideReconRange () {

        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin_invisible_treasure", "c_bosses_config");

        // gets potion
        res = dmc.tick(Direction.DOWN);
        String id = getInventory(res, "invisibility_potion").get(0).getId();

        // drinks potion
        assertDoesNotThrow(() -> dmc.tick(id)); // hould be random movement now
        // moves up
        res = dmc.tick(Direction.UP);

        Position pos = getEntities(res, "assassin").get(0).getPosition();
        int x = pos.getX();
        int y = pos.getY();

        for (int i = 0; i < 2; i++) {
            List<Position> expected = Arrays.asList(new Position(x-1, y),new Position(x+1, y), new Position(x, y-1),new Position(x, y+1));
            res = dmc.tick(Direction.UP);
            assertTrue(expected.contains(getEntities(res, "assassin").get(0).getPosition()));
            x = getEntities(res, "assassin").get(0).getPosition().getX();
            y = getEntities(res, "assassin").get(0).getPosition().getY();
        }
    }

    @Test
    @DisplayName("test Hydra movement " )
    public void hydraMovement () {
        // revert fom invincibility

        // revernt from isvisibility

    }


}

// BUGS
// [ ] - merc always runs away when invincible
// [ ] - merc always randomove when invisible
// [ ] - merc bribe does not consume gold in inventory
// [ ] - merc can go through locked doors
// [ ] - merc tailing player as ally does not work as intended

// TASK LIST:
// [ ] - assassins movement test
// [ ] - assassins bribe tests
// [ ] - hydra test


// TASK LIST:
// [x] - make sure zombies spawn correctly
// [x] - make sure zombie move correctly
// [X] - make sure dijkstra works
// [x] - implement run away state for zombs and mercs
// [x] - implement bribe merc
// [x] - implement interact
// [x] - implement when player is invisible/invincible observer
