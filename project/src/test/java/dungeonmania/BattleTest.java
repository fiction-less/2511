package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import static dungeonmania.TestUtils.getEntities;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.BuildableEntities.MidnightArmour;
import dungeonmania.BuildableEntities.Shield;
import dungeonmania.CollectableEntities.SunStone;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BattleTest {
    // test player vs zombie toast
    // make dungeon config
    @Test
    public void testBattleZombie() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicZombie", "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse response = controller.tick(Direction.DOWN);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 10);
        assertEquals(battle.getInitialEnemyHealth(), 5);
        assertEquals(battle.getEnemy(), "zombie_toast");
        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals(-(double)5/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
    }


    // test player vs spider
    @Test
    public void testBattleSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicSpider", "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse response = controller.tick(Direction.DOWN);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 10);
        assertEquals(battle.getInitialEnemyHealth(), 5);
        assertEquals(battle.getEnemy(), "spider");
        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals(-(double)5/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
    }

    @Test
    public void testBattleMerc() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 10);
        assertEquals(battle.getInitialEnemyHealth(), 5);
        assertEquals(battle.getEnemy(), "mercenary");
        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals(-(double)5/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
    }

    @Test
    public void testPlayerDies() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryPlayerDies");
        DungeonResponse response = controller.tick(Direction.RIGHT);
        assertTrue(!response.getEntities().stream().anyMatch(r -> r.getType().equals("player")));
    }

    // test sword and arrows + bow vs merc
    @Test
    public void testBattleMercWithWeapons() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicMercenary", "c_spiderTest_spawnRate2");
        controller.getCurrentGame().getPlayer().addInventory(new Sword("10", new Position(0,0), 1, 2));
        controller.getCurrentGame().getPlayer().addInventory(new Bow("11", new Position(0,0), 1));
        controller.getCurrentGame().getPlayer().addInventory(new Shield("12", new Position(0,0), 2, 1));
        DungeonResponse response = controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 10);
        assertEquals(battle.getInitialEnemyHealth(), 10);
        assertEquals(battle.getEnemy(), "mercenary");

        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals(-(double)(5-2)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(2*(10 + 1))/5, rr.getDeltaEnemyHealth());
        rr = rounds.get(1);
        assertEquals(-(double)(5)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(10 + 1)/5, rr.getDeltaEnemyHealth());
        rr = rounds.get(2);
        assertEquals(-(double)(5)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
    }

    // test invincibility potion
    @Test //failing test needs to be commented out for coverage checking
    public void testBattleMercInvincible() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_invincible", "c_battleTest_potions");

        // Pick up potion and check if player is alive
        DungeonResponse response = controller.tick(Direction.RIGHT);
        //System.out.println("Player Position" + controller.getCurrentGame().getPlayer().getPosition());
        //System.out.println("Enemy position" + getEntities(response, "mercenary").get(0).getPosition());
        assertTrue(response.getEntities().stream().anyMatch(r -> r.getType().equals("player")));
        assertEquals(response.getInventory().get(0).getType(), "invincibility_potion");
        assertEquals(response.getInventory().get(0).getId(), "2");

        // Use Potion and check if player is alive (Enemies should tick after player takes potion)
        assertDoesNotThrow(() -> controller.tick("2"));
        assertTrue(response.getEntities().stream().anyMatch(r -> r.getType().equals("player")));
        System.out.println(controller.getCurrentGame().getPlayer().getIsInvincible());

        response = controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);

        assertEquals(1, battle.getRounds().size());
        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals((double) 0, rr.getDeltaCharacterHealth());
        assertEquals(-(double)100, rr.getDeltaEnemyHealth());

        // Check if player is alive
        assertTrue(response.getEntities().stream().anyMatch(r -> r.getType().equals("player")));
        assertFalse(response.getEntities().stream().anyMatch(r -> r.getType().equals("mercenary")));
        
    }

    // test invisibility potion
    @Test //commenting out failing test for coverage
    public void testBattlePlayerInvisible() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_invisible", "c_battleTest_potions");

        // Pick up potion and check if player is alive
        DungeonResponse response = controller.tick(Direction.RIGHT);
        assertTrue(response.getEntities().stream().anyMatch(r -> r.getType().equals("player")));
        assertEquals(response.getInventory().get(0).getType(), "invisibility_potion");
        assertEquals(response.getInventory().get(0).getId(), "2");

        // Use Potion and check if player is alive (Enemies should tick after player takes potion)
        assertDoesNotThrow(() -> controller.tick("2"));
        assertTrue(response.getEntities().stream().anyMatch(r -> r.getType().equals("player")));

        response = controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = response.getBattles();
        assertTrue(battles.isEmpty());
    }

    // test battle with ally
    @Test
    public void testBattleWithAllyWeapon() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_battleTest_withAlly", "c_battleTest_largeHealth");
        // add weapons to inventory
        controller.getCurrentGame().getPlayer().addInventory(new Treasure("9", new Position(0,0)));
        controller.getCurrentGame().getPlayer().addInventory(new Sword("10", new Position(0,0), 1, 2));
        controller.getCurrentGame().getPlayer().addInventory(new Bow("11", new Position(0,0), 1));
        controller.getCurrentGame().getPlayer().addInventory(new Shield("12", new Position(0,0), 4, 1));

        // Bribe merc
        assertEquals(res.getEntities().get(1).getType(), "mercenary");
        assertEquals(res.getEntities().get(1).getId(), "2");
        assertDoesNotThrow(() -> controller.interact("2"));

        DungeonResponse response;
        response = controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 100);
        assertEquals(battle.getInitialEnemyHealth(), 100);
        assertEquals(battle.getEnemy(), "mercenary");

        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals((double) 0, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(2*(10 + 1 + 3))/5, rr.getDeltaEnemyHealth());
        rr = rounds.get(1);
        assertEquals(-(double)(7 - 3)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(10 + 1 + 3)/5, rr.getDeltaEnemyHealth());
        rr = rounds.get(2);
        assertEquals(-(double)(7 - 3)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(10 + 3)/5, rr.getDeltaEnemyHealth());
    }

    // test battle with ally
    @Test
    public void testBattleWithMidnightArmour() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_battleTest_withAlly", "c_battleTest_largeHealth");
        // add weapons to inventory
        controller.getCurrentGame().getPlayer().addInventory(new Treasure("9", new Position(0,0)));
        controller.getCurrentGame().getPlayer().addInventory(new Sword("10", new Position(0,0), 1, 2));
        controller.getCurrentGame().getPlayer().addInventory(new Bow("11", new Position(0,0), 1));
        controller.getCurrentGame().getPlayer().addInventory(new Shield("12", new Position(0,0), 4, 1));
        controller.getCurrentGame().getPlayer().addInventory(new MidnightArmour("13", new Position(0,0), 3, 3));

        // Bribe merc
        assertEquals(res.getEntities().get(1).getType(), "mercenary");
        assertEquals(res.getEntities().get(1).getId(), "2");
        assertDoesNotThrow(() -> controller.interact("2"));

        DungeonResponse response;
        response = controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        response = controller.tick(Direction.RIGHT);
        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 100);
        assertEquals(battle.getInitialEnemyHealth(), 100);
        assertEquals(battle.getEnemy(), "mercenary");

        List<RoundResponse> rounds = battle.getRounds();
        RoundResponse rr = rounds.get(0);
        assertEquals((double) 0, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(2*(10 + 1 + 3 + 3))/5, rr.getDeltaEnemyHealth());
        rr = rounds.get(1);
        assertEquals(-(double)(7 - 3 - 3)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(10 + 1 + 3 + 3)/5, rr.getDeltaEnemyHealth());
        rr = rounds.get(2);
        assertEquals(-(double)(7 - 3 - 3)/10, rr.getDeltaCharacterHealth());
        assertEquals(-(double)(10 + 3 + 3)/5, rr.getDeltaEnemyHealth());
    }

    @Test
    @DisplayName("Player vs Hydra")
    public void testBattleHydra() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicHydra", "c_battleTests_bosses");
        DungeonResponse response = controller.tick(Direction.DOWN);

        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 500);
        assertEquals(battle.getInitialEnemyHealth(), 500);
        assertEquals(battle.getEnemy(), "hydra");

        List<RoundResponse> rounds = battle.getRounds();
        double count = 0;
        for (int i = 0; i < rounds.size(); i++) {
            RoundResponse rr = rounds.get(i);
            assertEquals(-(double)10/10, rr.getDeltaCharacterHealth());
            assertTrue(rr.getDeltaEnemyHealth() == -(double)10/5
                    || rr.getDeltaEnemyHealth() == 1);
            if (rr.getDeltaEnemyHealth() == 1) {
                count++;
            }
        }
        assertTrue(count / (rounds.size() * 0.5) > 0.75
                && count / (rounds.size() * 0.5) < 1.25);
    }

    @Test
    @DisplayName("Test Hydra spawn head rate is correct")
    public void testBattleHydraWinAverage() {
        for (int i = 0; i < 100; i++) {
            testBattleHydra();
        }
    }

    @Test
    @DisplayName("Player vs Assassin")
    public void testBattleAssassin() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicAssassin", "c_battleTests_bosses");
        DungeonResponse response = controller.tick(Direction.DOWN);

        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 500);
        assertEquals(battle.getInitialEnemyHealth(), 500);
        assertEquals(battle.getEnemy(), "assassin");

        List<RoundResponse> rounds = battle.getRounds();
        for (int i = 0; i < rounds.size(); i++) {
            RoundResponse rr = rounds.get(i);
            assertEquals(-(double)10/10, rr.getDeltaCharacterHealth());
            assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
        }
    }

    @Test
    @DisplayName("Player vs Bribed Assassin")
    public void testBattleAssassinBribed() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicAssassin", "c_battleTests_bosses");
        ((Mercenary)controller.getCurrentGame().getEntityList().get(1)).setIsBribed(true);
        DungeonResponse res = controller.tick(Direction.DOWN);

        assertTrue(res.getBattles().isEmpty());
    }

    @Test
    @DisplayName("Player vs Bribed Mercenary")
    public void testBattleMercBribed() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_basicMercenary", "c_battleTests_bosses");
        ((Mercenary)controller.getCurrentGame().getEntityList().get(1)).setIsBribed(true);
        DungeonResponse res = controller.tick(Direction.DOWN);

        assertTrue(res.getBattles().isEmpty());
    }

    @Test
    @DisplayName("Player wins vs Old Player - Win")
    public void testBattleOldPlayerLose() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_oldplayer", "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse response = controller.tick(Direction.DOWN);

        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 10);
        assertEquals(battle.getInitialEnemyHealth(), 10);
        assertEquals(battle.getEnemy(), "old_player");

        List<RoundResponse> rounds = battle.getRounds();
        for (int i = 0; i < rounds.size(); i++) {
            RoundResponse rr = rounds.get(i);
            assertEquals(-(double)10/10, rr.getDeltaCharacterHealth());
            assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
        }
    }

    @Test
    @DisplayName("Player vs Old Player - Lose")
    public void testBattleOldPlayerWin() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_oldplayer", "c_battleTests_basicMercenaryMercenaryDies");
        controller.getCurrentGame().getPlayer().setHealth(1);
        DungeonResponse response = controller.tick(Direction.DOWN);

        List<BattleResponse> battles = response.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 1);
        assertEquals(battle.getInitialEnemyHealth(), 10);
        assertEquals(battle.getEnemy(), "old_player");

        List<RoundResponse> rounds = battle.getRounds();
        for (int i = 0; i < rounds.size(); i++) {
            RoundResponse rr = rounds.get(i);
            assertEquals(-(double)10/10, rr.getDeltaCharacterHealth());
            assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
        }
    }

    @Test
    @DisplayName("Player vs Old Player - No Fight with Sunstone")
    public void testBattleOldPlayerSunstone() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_oldplayer", "c_battleTests_basicMercenaryMercenaryDies");
        ((Player)controller.getCurrentGame().getEntityList().get(1)).addInventory(new SunStone("4", new Position(0, 0)));
        DungeonResponse res = controller.tick(Direction.DOWN);

        assertTrue(res.getBattles().isEmpty());
    }

    @Test
    @DisplayName("Player vs Old Player - No Fight with Midnight Armour")
    public void testBattleOldPlayerMidnightArmour() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_oldplayer", "c_battleTests_basicMercenaryMercenaryDies");
        ((Player)controller.getCurrentGame().getEntityList().get(1)).addInventory(new MidnightArmour("4", new Position(0, 0), 3, 3));
        DungeonResponse res = controller.tick(Direction.DOWN);

        assertTrue(res.getBattles().isEmpty());
    }

    @Test
    @DisplayName("Player vs Old Player - No Fight when invisible")
    public void testBattleOldPlayerInvisible() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_oldplayer", "c_battleTests_basicMercenaryMercenaryDies");
        ((Player)controller.getCurrentGame().getEntityList().get(1)).setIsInvisible(true);
        DungeonResponse res = controller.tick(Direction.DOWN);

        assertTrue(res.getBattles().isEmpty());
    }

    @Test
    @DisplayName("Player vs Old Player - No Fight when invisible")
    public void testBattleOldPlayerWithWeapons() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_battleTest_oldplayer", "c_battleTests_basicMercenaryMercenaryDies");
        ((Player)controller.getCurrentGame().getEntityList().get(1)).addInventory(new Sword("4", new Position(0, 0), 3, 3));
        ((Player)controller.getCurrentGame().getEntityList().get(1)).addInventory(new Bow("4", new Position(0, 0), 2));
        ((Player)controller.getCurrentGame().getEntityList().get(1)).addInventory(new Shield("4", new Position(0, 0), 3, 3));
        DungeonResponse res = controller.tick(Direction.DOWN);

        List<BattleResponse> battles = res.getBattles();
        BattleResponse battle = battles.get(0);
        assertEquals(battle.getInitialPlayerHealth(), 10);
        assertEquals(battle.getInitialEnemyHealth(), 10);
        assertEquals(battle.getEnemy(), "old_player");

        List<RoundResponse> rounds = battle.getRounds();
        for (int i = 0; i < rounds.size(); i++) {
            RoundResponse rr = rounds.get(i);
            assertEquals(-(double)10/10, rr.getDeltaCharacterHealth());
            assertEquals(-(double)10/5, rr.getDeltaEnemyHealth());
        }
    }
}
