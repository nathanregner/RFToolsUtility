package mcjty.rftoolsutility.modules.cc_tweaked;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import mcjty.lib.varia.RedstoneMode;
import mcjty.rftoolsutility.modules.spawner.blocks.MatterBeamerTileEntity;

import static mcjty.rftoolsutility.modules.spawner.SpawnerModule.MATTER_BEAMER;

final class MatterBeamerPeripheral implements GenericPeripheral {

    @Override
    public String id() {
        return MATTER_BEAMER.block().getKey().location().toString();
    }

    @LuaFunction(mainThread = true)
    public String getRedstoneMode(MatterBeamerTileEntity te) {
        return te.getRSMode().toString();
    }

    @LuaFunction(mainThread = true)
    public void setRedstoneMode(MatterBeamerTileEntity te, String mode) {
        te.setRSMode(RedstoneMode.valueOf(mode));
    }
}
