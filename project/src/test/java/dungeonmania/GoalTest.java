package dungeonmania;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;

import static dungeonmania.TestUtils.getPlayer;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import dungeonmania.util.Position;


public class GoalTest {
    
    @Test
    @DisplayName("Test for correct goal string at beginning of dungeon for single goal")
    public void singleGoalNewGame() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_movementTest_testMovementDown", "c_spiderTest_basicMovement");

        assertEquals(":exit", res.getGoals());
    }

    @Test
    @DisplayName("Test for correct goal string at beginning of dungeon for AND goal")
    public void conjunctionAndGoalNewGame() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_goalTest_conjunctionAnd", "c_spiderTest_basicMovement");

        assertEquals("(:exit AND :treasure)", res.getGoals());
    }

    @Test
    @DisplayName("Test for correct goal string at beginning of dungeon for OR goal")
    public void conjunctionOrGoalNewGame() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_goalTest_conjunctionOr", "c_spiderTest_basicMovement");

        assertEquals("(:exit OR :treasure)", res.getGoals());
    }

    @Test
    @DisplayName("Test for correct goal string at beginning of dungeon for complex goal")
    public void complexGoalNewGame() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_complexGoalsTest_noEntities", "c_spiderTest_basicMovement");

        assertEquals("((:exit AND :treasure) AND (:boulders AND :enemies))", res.getGoals());
    }

    @Test
    @DisplayName("Test that goal is not checked until 1st tick")
    public void singleGoalComplete() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_goalTest_exitComplete", "c_spiderTest_basicMovement");

        assertEquals(":exit", res.getGoals());

        // uncomment when tick(movement)
        res = dmc.tick(Direction.UP);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test that all goal types get completed")
    public void allGoalsComplete() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_complexGoalsTest_andAll", "c_complexGoalsTest_andAll");

        assertEquals("((:exit AND :treasure) AND (:boulders AND :enemies))", res.getGoals());

        res = dmc.tick(Direction.RIGHT);
        assertEquals("((:exit AND :treasure) AND :boulders)", res.getGoals());

        res = dmc.tick(Direction.RIGHT);
        assertEquals("(:exit AND :treasure)", res.getGoals());

        res = dmc.tick(Direction.DOWN);
        assertEquals(":exit", res.getGoals());

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test for correct goal string when 2nd goal completes last for AND goal")
    public void secondGoalCompletesFirstAND() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_goalTest_andEnemyTreasure", "c_spiderTest_basicMovement");
        assertEquals("(:enemies AND :treasure)", res.getGoals());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(":treasure", res.getGoals());

        res = dmc.tick(Direction.RIGHT);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test for correct goal string when 2nd goal completes last for OR goal")
    public void secondGoalCompletesFirstOR() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_goalTest_orEnemyTreasure", "c_spiderTest_basicMovement");
        assertEquals("(:enemies OR :treasure)", res.getGoals());

        res = dmc.tick(Direction.RIGHT);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test sunstone counts towards treasure")
    public void sunstoneTreasureGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse  res = dmc.newGame("d_sunstone_treasureGoal", "c_treasureGoal_sunstone");
        assertEquals(":treasure", res.getGoals());

        // get sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(":treasure", res.getGoals());
        assertEquals(new Position(1, 2), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().get(0).getType(), "sun_stone");
        assertEquals(res.getInventory().size(), 1);

        // get treasure
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 3), getPlayer(res).get().getPosition());
        assertEquals(res.getInventory().get(1).getType(), "treasure");
        assertEquals(res.getInventory().size(), 2);
        assertEquals("", res.getGoals());
    }
}
