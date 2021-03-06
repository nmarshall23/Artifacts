package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ComponentHeal implements IArtifactComponent {

	public ComponentHeal() {
	}
	
	public String getName() {
		return "Heal Effect";
	}
	
	public String getRandomTrigger(Random rand) {
		String str = "";
		switch(rand.nextInt(4)) {
			case 0:
				str = "onUpdate";
				break;
			case 1:
				str = "hitEntity";
				break;
			case 2:
				str = "onItemRightClick";
				break;
			case 3:
				str = "onHeld";
				break;
		}
		return str;
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand) {
		return i;
	}

	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return null;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return 0;
	}

	//works once!?
	//client side only!!
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,	EntityPlayer par3EntityPlayer) {
		ByteArrayOutputStream bt = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bt);
		try
		{
			//System.out.println("Building packet...");
			out.writeInt(1);
			out.writeFloat(1);
			Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
			Player player = (Player)par3EntityPlayer;
			//System.out.println("Sending packet..." + player);
			PacketDispatcher.sendPacketToServer(packet);
			par1ItemStack.damageItem(1, par3EntityPlayer);
		}
		catch (IOException ex)
		{
			System.out.println("couldnt send packet!");
		}
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		if(par3EntityLivingBase instanceof EntityClientPlayerMP) {
			//System.out.println("Atk: " + par2EntityLivingBase.hurtTime);
			//EntityClientPlayerMP player = (EntityClientPlayerMP)par3EntityLivingBase;
			//player.heal(1.0F);
			//player.setHealth(player.getHealth()+1);
			if(par2EntityLivingBase.hurtTime == 0) {
				ByteArrayOutputStream bt = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bt);
				try
				{
					//System.out.println("Building packet...");
					out.writeInt(1);
					out.writeFloat(1);
					Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
					Player player = (Player)par3EntityLivingBase;
					//System.out.println("Sending packet..." + player);
					PacketDispatcher.sendPacketToServer(packet);
					par1ItemStack.damageItem(1, par3EntityLivingBase);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}
			}
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	//works great
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par3Entity instanceof EntityLivingBase) {
			if(!par2World.isRemote && par2World.rand.nextInt(300) == 0) {//about 15 seconds
				EntityLivingBase elb = (EntityLivingBase)par3Entity;
				elb.setHealth(elb.getHealth()+1);
				//elb.heal(1F);
			}
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.block;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add("Heals the Player " + trigger);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Heals the Player");
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Healing";
				break;
			case 1:
				str = "Rejuvenating";
				break;
			case 2:
				str = "Holy";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Healing";
				break;
			case 1:
				str = "of Rejuvenation";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 7645;
	}

	@Override
	public int getNegTextureBitflags() {
		return 0;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		if(par3Entity instanceof EntityLivingBase) {
			if(!par2World.isRemote && par2World.rand.nextInt(100) == 0) {//about 5 seconds
				EntityLivingBase elb = (EntityLivingBase)par3Entity;
				elb.setHealth(elb.getHealth()+1);
				//elb.heal(1F);
			}
		}
	}
}
