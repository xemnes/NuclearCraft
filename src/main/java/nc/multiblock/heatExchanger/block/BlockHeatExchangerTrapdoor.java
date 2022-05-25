package nc.multiblock.heatExchanger.block;

import nc.multiblock.MultiblockBlockPartTrapdoor;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTrapdoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHeatExchangerTrapdoor extends MultiblockBlockPartTrapdoor {

    public BlockHeatExchangerTrapdoor(Material material) {
        super(material);
        setHarvestLevel("pickaxe", 0);
        setHardness(2F);
        setResistance(15F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileHeatExchangerTrapdoor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player == null) return false;
        if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
        return rightClickOnPart(world, pos, player, hand, facing);
    }
}
