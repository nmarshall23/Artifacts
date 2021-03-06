package draco18s.artifacts.block;

import java.util.Random;

import draco18s.artifacts.DragonArtifacts;
import draco18s.artifacts.entity.TileEntityAntibuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAntibuilder extends BlockContainer {

	public static Block instance;

	public BlockAntibuilder(int par1) {
		super(par1, Material.rock);
		setUnlocalizedName("Anti-Builder");
		setHardness(1.0F);
		setResistance(200.0F);
		setStepSound(soundStoneFootstep);
        setCreativeTab(DragonArtifacts.tabTraps);
	}
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("stonebrick_carved");
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAntibuilder();
	}

	@Override
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
	/*public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
            TEAntibuilder tab = (TEAntibuilder)par1World.getBlockTileEntity(par2, par3, par4);
            tab.setActive(!flag);
        	//System.out.println("Antibuilder should be active: " + (!flag));
        }
    }*/
}
