package nc.multiblock.saltFission.block;

import java.util.Random;

import nc.block.NCBlockDoor;
import nc.init.NCItems;
import nc.multiblock.MultiblockBlockPartDoor;
import nc.multiblock.saltFission.tile.TileSaltFissionDistributor;
import nc.multiblock.saltFission.tile.TileSaltFissionDoor;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSaltFissionDoor extends MultiblockBlockPartDoor {

    public BlockSaltFissionDoor(Material material) {
        super(material);
        setHarvestLevel("pickaxe", 0);
        setHardness(2F);
        setResistance(15F);
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileSaltFissionDoor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player == null) return false;
        if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
        return rightClickOnPart(world, pos, player, hand, facing);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(getItem());
    }

    private static Item getItem() {
        return NCItems.salt_fission_door;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : this.getItem();
    }
}
