package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.countEntityOfType;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class TimeTravelTest {

    @Test
    @DisplayName("Test an old player becomes an enemy and follows same route")
    public void testSinglePlayer1ticks() {



        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_TT_singlePlayer", "c_TTM3");

        for (int i = 0; i < 8; i ++) res = controller.tick(Direction.DOWN);

        System.out.println("current entities");
        ArrayList<Entity> list = controller.getCurrentGame().getEntityList();
        list.stream().forEach(i -> System.out.println(i.getPosition()+ " + " + i.getType()));

        System.out.println("\nI rewind (not a tick)");
        res = controller.rewind(1);
        list = controller.getCurrentGame().getEntityList();
        list.stream().forEach(i -> System.out.println(i.getPosition()+ " + " + i.getType()));
        System.out.println("\nPlayer goes to the right (1 tick)");
        res = controller.tick(Direction.RIGHT);
        list = controller.getCurrentGame().getEntityList();
        list.stream().forEach(i -> System.out.println(i.getPosition()+ " + " + i.getType()));
        // Position OPpos = getEntities(res, "old_player").get(0).getPosition();
        // System.out.println(OPpos);
        System.out.println("\nPlayer goes to the right, old player disappears");
        res = controller.tick(Direction.RIGHT);

        list = controller.getCurrentGame().getEntityList();
        list.stream().forEach(i -> System.out.println(i.getPosition()+ " + " + i.getType()));



        // THE REWIND ITSELF IF NOT A TICK ITS SIMILAR TO INTERACT, SO THE SINGLE TICK SHOULD BE THE PLAYER MOVING TO THE LOCATION
        // OF THE TIME TURNER??????? UNLESSS????


    }

    @Test
    @DisplayName("Test an old player becomes an enemy and follows same route")
    public void testSinglePlayer5ticks() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_TT_singlePlayer", "c_TTM3");
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.tick(Direction.DOWN);
        res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
        res = controller.rewind(5);

        for (int ii = 0; ii < 10; ii++) {
            res = controller.tick(Direction.DOWN);

            res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
            System.out.println("\n");
        }

        for (int j = 0; j < 10; j++) {
            res = controller.tick(Direction.DOWN);

            res.getEntities().forEach(i -> System.out.println(i.getType() + i.getPosition()));
            System.out.println("\n");
        }

    }

    @Test
    @DisplayName("if player time travels more ticks then it has ticked then go back to start of game")
    public void timeTravelBeyondExistence() {

    }

    @Test
    @DisplayName("old player interacts with items properly -> curr player does not intefere")
    public void timetravelInteract() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_TeleportingPortal", "c_TTM3");
    }


    @Test
    @DisplayName("test portal")
    public void teleportingPortal() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_TeleportingPortal", "c_TTM3");
    }

    // THE ASUUMPTION IS THAT THE OLD PLAYER WILL INTERACT WITH AN ITEM EVEN IT ITS NO LONGER THERE
    // AND AN EXCEPTION WILL BE THROWN
    // INSTEAD OF THE PLAYER JUST DOING NOTHING BC IT CANT INTERACT WITH IT.
    @Test
    @DisplayName("old throws an exception because curr player took their stuff ")
    public void timetravelInteractException() {

    }

    @Test
    @DisplayName("Old player will kill any enemies it encounters/ all enemiers including player unless its bribed")
    public void imposterKillsEnemies() {


    }






}
