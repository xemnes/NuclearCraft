package nc.multiblock.heatExchanger.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.tile.TileSaltFissionPartBase;

public class TileHeatExchangerDoor extends TileHeatExchangerPartBase {

    public TileHeatExchangerDoor() {
        super(CuboidalPartPositionType.WALL);
    }

    @Override
    public void onMachineAssembled(HeatExchanger controller) {
        doStandardNullControllerResponse(controller);
        super.onMachineAssembled(controller);
        if (getWorld().isRemote) return;
    }

    @Override
    public void onMachineBroken() {
        super.onMachineBroken();
        if (getWorld().isRemote) return;
        //getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
    }

}
