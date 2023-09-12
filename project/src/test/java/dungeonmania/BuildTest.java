package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntities.Arrows;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.Key;
import dungeonmania.CollectableEntities.SunStone;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.CollectableEntities.Wood;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class BuildTest {
    
    String dummyDungeon = "d_newGameTest_simpleEntities";
    String dummyConfig = "c_spiderTest_basicMovement";
    Position dummyPos = new Position(1, 1);
    
    @Test
    public void illegalArgument(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        assertThrows(IllegalArgumentException.class, () -> dmc.build("buildable_nonexistent"));
    }

    @Test
    public void invalidActionBow(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));
    }

    @Test
    public void invalidActionShield(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    public void buildsBow(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking functionality for moving to cells with bow recipe items
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Arrows("arrow1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Arrows("arrow2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Arrows("arrow3", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Bomb("bomb1", dummyPos, 1));
        
        //asserts bow does not exist in inventory
        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("bow")), false);

        //asserts 1 wood before bow creation
        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("wood")).count(), 1);

        //asserts 3 arrows before bow creation
        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("arrow")).count(), 3);
        
        assertDoesNotThrow(() -> dmc.build("bow"));

        //asserts bow exists in inventory 
        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("bow")), true);

        //asserts removal of 1 wood after biw creation
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("wood")).count());
        
        //asserts removal of 3 arrows after bow creation
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("arrow")).count());

    }

    @Test
    public void buildsShieldViaTreasure(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking functionality for moving to cells with shield recipe items
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Treasure("treasure1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Bomb("bomb1", dummyPos, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone2", dummyPos));

        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("shield")), false);

        assertDoesNotThrow(() -> dmc.build("shield"));

        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("shield")), true);


        //asserts recipe items are removed after crafting of shield
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("wood")).count());
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("treasure")).count());

    }

    @Test
    public void buildsShieldViaKey(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking functionality for moving to cells with shield recipe items
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Key("key1", dummyPos, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone2", dummyPos));

        assertEquals(false, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("shield")));

        assertDoesNotThrow(() -> dmc.build("shield"));

        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("shield")), true);

        //asserts recipe items are removed after crafting of shield
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("wood")).count());
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("key")).count());
    }

    @Test
    public void buildShieldPrioritiseTreasure(){
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, dummyConfig);

        //Mocking functionality for moving to cells with shield recipe items
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Key("key1", dummyPos, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Treasure("treasure1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone2", dummyPos));

        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("shield")), false);

        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).canCraftShield(), "treasure");

        assertDoesNotThrow(() -> dmc.build("shield"));

        assertEquals(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("shield")), true);

        //asserts recipe items are removed after crafting of shield
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("wood")).count());
        assertEquals(0, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("treasure")).count());

        //asserts key still remains in inventory 
        assertEquals(1, Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("key")).count());
    }

    //Milestone 3 Testing 

    @Test 
    public void buildSceptreNormal() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, "M3_config");

        //Mocking functionality for adding items to player's inventory 
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("wood2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Key("key1", dummyPos, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Treasure("treasure1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone1", dummyPos));

        System.out.print(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory() + "\n");

        ArrayList<String> arr = Helper.getPlayer(dmc.getCurrentGame().getEntityList()).canCraftSceptre();
        
        //assert that sceptre will build with wood and treasure
        System.out.println(arr);
        assert(!arr.isEmpty() && arr.contains("wood") && arr.contains("treasure"));
        
        // //builds sceptre execute 
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        // //asserts that sceptre is successfully built 
        assert(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().anyMatch(x -> x.getType().equals("sceptre")));

        // assert that recipe items are removed 
        assert(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("wood")).count() == 1);

        assert(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("treasure")).count() == 0);

        assert(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("sun_stone")).count() == 0);

        System.out.println(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory());

    }

    @Test
    public void buildSceptreRetain() throws IllegalArgumentException, InvalidActionException{
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, "M3_config");

        //Mocking functionality for adding items to player's inventory 
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Arrows("a1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Arrows("a2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Key("k1", dummyPos, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone2", dummyPos));

        System.out.println(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory());
    
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        assert(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("arrow")).count() == 0);

        assert(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory().stream().filter(x -> x.getType().equals("sun_stone")).count() == 1);

        System.out.println(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory());

    }

    @Test
    public void buildArmour() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, "M3_config");

        //Mocking functionality for adding items to player's inventory 
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Sword("s1", dummyPos, 1, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone1", dummyPos));

        assertDoesNotThrow(() ->dmc.build("midnight_armour"));

        System.out.println(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory());

    }

    @Test
    public void cannotBuildArmourItems() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, "M3_config");

        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }


    @Test
    public void cannotBuildArmourZombie() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, "M3_config");
        dmc.getCurrentGame().addEntitiy(new Entity("1", "zombie_toast", dummyPos, true, true));

        //Mocking functionality for adding items to player's inventory 
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Sword("s1", dummyPos, 1, 1));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone1", dummyPos));

        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    //M2 Entities with Sunstone 
    @Test
    public void sunstoneBuildShield() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(dummyDungeon, "M3_config");

        //Mocking functionality for adding items to player's inventory 
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("w1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new Wood("w2", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone1", dummyPos));
        Helper.getPlayer(dmc.getCurrentGame().getEntityList()).addInventory(new SunStone("sunstone2", dummyPos));

        System.out.println(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory());

        assertDoesNotThrow(() -> dmc.build("shield"));

        System.out.println(Helper.getPlayer(dmc.getCurrentGame().getEntityList()).getInventory());
    }



}
