package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

import dungeonmania.util.Position;

public class NewGameTest {

    @Test
    public void dungeonNonExistent() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("d_nonexistent", "c_spiderTest_basicMovement"));
    }

    @Test
    public void configNonExistent() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("d_spiderTest_basicMovement", "c_nonexistent"));
    }

    @Test void dungeonConfigExistent() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.newGame("d_newGameTest_simpleEntities", "c_spiderTest_basicMovement"));
    }
    
    //@Test
    //public void newGameReturnType() {
    //    DungeonManiaController dmc;
    //    dmc = new DungeonManiaController();
    //    DungeonResponse res = dmc.newGame("d_newGameTest_simpleEntities", "c_spiderTest_basicMovement");
    //
    //    assertTrue(res.getClass() == DungeonResponse.instance);
    //}

    @Test
    public void newGameEntityOnly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_newGameTest_simpleEntities", "c_spiderTest_basicMovement");

        List<EntityResponse> er = new ArrayList<EntityResponse>();
        er.add(new EntityResponse("1", "player", new Position(1,1), false));
        er.add(new EntityResponse("2", "exit", new Position(8,8), false));

        assertEquals(er, res.getEntities());
        assertEquals(er.get(0).getId(), res.getEntities().get(0).getId());
        assertEquals(er.get(1).getId(), res.getEntities().get(1).getId());
    }

    @Test
    public void newGamePlayerAddedLast() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.newGame("d_allEntities", "M3_config"));

        assertEquals(4, dmc.getCurrentGame().getPlayer().getObservers().size());
    }

    @Test
    @DisplayName("Test to see newGame returns correct DungeonResponse")
    public void newGameResponse() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_newGameTest_simpleEntities", "c_spiderTest_basicMovement");

        List<EntityResponse> er = new ArrayList<EntityResponse>();
        er.add(new EntityResponse("1", "player", new Position(1,1), false));
        er.add(new EntityResponse("2", "exit", new Position(8,8), false));

        List<ItemResponse> inventoryResponse = new ArrayList<ItemResponse>();
        List<BattleResponse> battlesResponse = new ArrayList<BattleResponse>();
        List<String> buildables = new ArrayList<String>();

        DungeonResponse expected = new DungeonResponse("game1", "d_newGameTest_simpleEntities", er, inventoryResponse, battlesResponse, buildables, ":exit");

        assertEquals(expected.getDungeonId(), res.getDungeonId());
        assertEquals(expected.getDungeonName(), res.getDungeonName());
        assertEquals(expected.getEntities(), res.getEntities());
        assertEquals(expected.getInventory(), res.getInventory());
        assertEquals(expected.getBattles(), res.getBattles());
        assertEquals(expected.getBuildables(), res.getBuildables());
        assertEquals(expected.getGoals(), res.getGoals());
    }


}
