package mcjty.rftoolsutility.modules.cc_tweaked;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsutility.RFToolsUtility;
import mcjty.rftoolsutility.modules.spawner.blocks.SpawnerTileEntity;
import mcjty.rftoolsutility.modules.spawner.recipes.SpawnerRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class CCTweakedModule implements IModule {

    @Override
    public void init(FMLCommonSetupEvent event) {
            registerPeripherals();
    }

    @Override
    public void initClient(FMLClientSetupEvent fmlClientSetupEvent) {
    }

    @Override
    public void initConfig(IEventBus iEventBus) {
    }

    private void registerPeripherals() {
        ComputerCraftAPI.registerGenericSource(new SpawnerPeripheral());
    }

    private static class SpawnerPeripheral implements GenericPeripheral {
        @Override
        public String id() {
            return RFToolsUtility.MODID + ":spawner";
        }

        // using "detail" for consistency with other CC APIs
        @LuaFunction(mainThread = true)
        public Map<String, ?> getSpawnerDetail(SpawnerTileEntity te) {
            var matter = Map.of(
                    "key", te.getMatter(0),
                    "bulk", te.getMatter(1),
                    "living", te.getMatter(2)
            );
            var data = new HashMap<String, Object>();
            data.put("matter", matter);
            data.put("syringe", getSyringeDetail(te));
            return data;
        }

        public Map<String, ?> getSyringeDetail(SpawnerTileEntity te) {
            var mobData = te.getMobData();
            var spawnerData = te.getSpawnerData();
            if (mobData == null || spawnerData.mob() == null) return null;
            return Map.of(
                    "mob", spawnerData.mob().toString(),
                    "key", itemDetail(mobData.getItem1()),
                    "bulk", itemDetail(mobData.getItem2()),
                    "living", itemDetail(mobData.getItem3()),
                    "rf", mobData.getSpawnRf()
            );
        }

        private static Map<String, Object> itemDetail(SpawnerRecipes.MobSpawnAmount spawnAmount) {
            var data = new HashMap<String, Object>(2);
            var itemStacks = spawnAmount.getObject().getItems();
            if (itemStacks.length > 0) {
                var item = BuiltInRegistries.ITEM.getResourceKey(itemStacks[0].getItem()).orElseThrow();
                data.put("item", item.location().toString());
            }
            data.put("amount", spawnAmount.getAmount());
            return data;
        }
    }
}
