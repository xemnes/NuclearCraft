package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.tile.TileSaltFissionPartBase;
import nc.multiblock.turbine.Turbine;

public class TileTurbineTrapdoor extends TileTurbinePartBase {

    public TileTurbineTrapdoor() {
        super(CuboidalPartPositionType.WALL);
    }

    @Override
    public void onMachineAssembled(Turbine controller) {
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
