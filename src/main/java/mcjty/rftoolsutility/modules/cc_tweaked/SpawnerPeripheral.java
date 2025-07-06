package mcjty.rftoolsutility.modules.cc_tweaked;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import mcjty.rftoolsutility.modules.spawner.blocks.SpawnerTileEntity;
import mcjty.rftoolsutility.modules.spawner.recipes.SpawnerRecipes;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.HashMap;
import java.util.Map;

import static mcjty.rftoolsutility.modules.spawner.SpawnerModule.SPAWNER;

final class SpawnerPeripheral implements GenericPeripheral {

    @Override
    public String id() {
        return SPAWNER.block().getKey().location().toString();
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
