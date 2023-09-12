package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class PersistenceTest {
    @Test
    @DisplayName("Test for entities being loaded correctly")
    public void entitiesLoaded() {
        DungeonManiaController dmc1 = new DungeonManiaController();
        dmc1.newGame("d_allEntities", "M3_config");
        DungeonResponse res1 = dmc1.saveGame("game1");

        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse res2 = dmc2.loadGame("game1");

        assertDungeonResponseEquals(res1, res2);
    }

    @Test
    @DisplayName("Test for battles being loaded correctly")
    public void BattlesLoaded() {
        DungeonManiaController dmc1 = new DungeonManiaController();
        dmc1.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        dmc1.tick(Direction.RIGHT);
        DungeonResponse res1 = dmc1.saveGame("game2");

        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse res2 = dmc2.loadGame("game2");

        assertTrue(res1.getBattles().size() > 0);
        assertEquals(res1.getBattles().size(), res2.getBattles().size());
        assertDungeonResponseEquals(res1, res2);
    }

    @Test
    @DisplayName("Test for items being loaded correctly")
    public void ItemsLoaded() {
        DungeonManiaController dmc1 = new DungeonManiaController();
        dmc1.newGame("d_movementTest_collectibles", "c_movementTest_testMovementDown");
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        dmc1.tick(Direction.RIGHT);
        DungeonResponse res1 = dmc1.saveGame("game3");

        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse res2 = dmc2.loadGame("game3");

        assertDungeonResponseEquals(res1, res2);
    }

    @Test
    @DisplayName("Loadgame throws exception when given invalid load name")
    public void invalidLoadName() {
        DungeonManiaController dmc1 = new DungeonManiaController();
        dmc1.newGame("d_allEntities", "M3_config");
        dmc1.saveGame("game4");

        DungeonManiaController dmc2 = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class,() -> dmc2.loadGame("game0"));

    }




    public void assertDungeonResponseEquals(DungeonResponse res1, DungeonResponse res2) {
        assertEquals(res1.getDungeonId(), res2.getDungeonId());
        assertEquals(res1.getDungeonName(), res2.getDungeonName());
        assertEntityResponsesEquals(res1.getEntities(), res2.getEntities());
        assertItemResponsesEquals(res1.getInventory(), res2.getInventory());
        assertBattleResponsesEquals(res1.getBattles(), res2.getBattles());
        assertEquals(res1.getBuildables(), res2.getBuildables());
        assertEquals(res1.getGoals(), res2.getGoals());
    }

    public void assertEntityResponsesEquals(List<EntityResponse> entities1, List<EntityResponse> entities2) {
        for (int i = 0; i < entities1.size(); i++) {
            assertEntityResponseEquals(entities1.get(i), entities2.get(i));
        }
    }

    public void assertEntityResponseEquals(EntityResponse entity1, EntityResponse entity2) {
        assertEquals(entity1.getId(), entity2.getId());
        assertEquals(entity1.getType(), entity2.getType());
        assertEquals(entity1.getPosition(), entity2.getPosition());
        assertEquals(entity1.isInteractable(), entity2.isInteractable());
    }

    public void assertItemResponsesEquals(List<ItemResponse> items1, List<ItemResponse> items2) {
        for (int i = 0; i < items1.size(); i++) {
            assertItemResponseEquals(items1.get(i), items1.get(i));
        }
    }

    public void assertItemResponseEquals(ItemResponse item1, ItemResponse item2) {
        assertEquals(item1.getId(), item2.getId());
        assertEquals(item1.getType(), item2.getType());
    }

    public void assertBattleResponsesEquals(List<BattleResponse> battles1, List<BattleResponse> battles2) {
        for (int i = 0; i < battles1.size(); i++) {
            assertBattleResponseEquals(battles1.get(i), battles1.get(i));
        }
    }

    public void assertBattleResponseEquals(BattleResponse battle1, BattleResponse battle2) {
        assertEquals(battle1.getEnemy(), battle2.getEnemy());
        assertEquals(battle1.getInitialPlayerHealth(), battle2.getInitialPlayerHealth());
        assertEquals(battle1.getInitialEnemyHealth(), battle2.getInitialEnemyHealth());
        assertRoundResponsesEquals(battle1.getRounds(), battle2.getRounds());
    }

    public void assertRoundResponsesEquals(List<RoundResponse> rounds1, List<RoundResponse> rounds2) {
        for (int i = 0; i < rounds1.size(); i++) {
            assertRoundResponseEquals(rounds1.get(i), rounds1.get(i));
        }
    }

    public void assertRoundResponseEquals(RoundResponse round1, RoundResponse round2) {
        assertEquals(round1.getDeltaCharacterHealth(), round2.getDeltaCharacterHealth());
        assertEquals(round1.getDeltaEnemyHealth(), round2.getDeltaEnemyHealth());
        assertItemResponsesEquals(round1.getWeaponryUsed(), round2.getWeaponryUsed());
    }
}
