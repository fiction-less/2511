package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BoulderTest {
    // test if boulder moves when nothing is blocking it
    @Test
    public void playerMoveBoulder() {
        // make game
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse d_response = controller.newGame("d_boulderTest", "c_battleTests_basicMercenaryMercenaryDies");
        // get expected player position
        Position expectedPosition = controller.getCurrentGame().getPlayer().getPosition().translateBy(Direction.RIGHT);
        // move player 
        DungeonResponse response = controller.tick(Direction.RIGHT);
        // assert player position
        assertEquals(expectedPosition, controller.getCurrentGame().getPlayer().getPosition());
    }

    // test if boulder doesnt move when its blocked by wall, boulder
    @Test void playerMoveBoulderBlocked() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse d_response = controller.newGame("d_boulderTest", "c_battleTests_basicMercenaryMercenaryDies");
        // get expected player position
        Position expected = controller.getCurrentGame().getPlayer().getPosition();
        // move player 
        controller.tick(Direction.DOWN);
        // assert player position
        assertEquals(expected, controller.getCurrentGame().getPlayer().getPosition());
        controller.tick(Direction.LEFT);
        assertEquals(expected, controller.getCurrentGame().getPlayer().getPosition());
    }
}
