package draco18s.artifacts.factory;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ChestGenHooks;
import draco18s.artifacts.WeightedRandomArtifact;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.IArtifactAPI;
import draco18s.artifacts.api.interfaces.IArtifactComponent;
import draco18s.artifacts.components.ComponentAirWalk;
import draco18s.artifacts.components.ComponentBreathing;
import draco18s.artifacts.components.ComponentCashout;
import draco18s.artifacts.components.ComponentDamage;
import draco18s.artifacts.components.ComponentExplosive;
import draco18s.artifacts.components.ComponentFireball;
import draco18s.artifacts.components.ComponentFoodie;
import draco18s.artifacts.components.ComponentHarvesting;
import draco18s.artifacts.components.ComponentHeal;
import draco18s.artifacts.components.ComponentHealth;
import draco18s.artifacts.components.ComponentJumping;
import draco18s.artifacts.components.ComponentLight;
import draco18s.artifacts.components.ComponentLightning;
import draco18s.artifacts.components.ComponentMassWeb;
import draco18s.artifacts.components.ComponentMining;
import draco18s.artifacts.components.ComponentNormalDamage;
import draco18s.artifacts.components.ComponentRepair;
import draco18s.artifacts.components.ComponentResistance;
import draco18s.artifacts.components.ComponentSpeed;
import draco18s.artifacts.components.ComponentVision;
import draco18s.artifacts.item.ItemArtifact;

public class FactoryArtifact implements IArtifactAPI {
	private HashMap effects = new HashMap();
	private Random rand = new Random();
	private int numComponents = 0;
	private int Amulet;      //1
	private int Dagger;      //2
	private int Figurine;    //4
	private int Ring;        //8
	private int Staff;       //16
	private int Sword;       //32
	private int Trinket;     //64
	private int Wand;        //128
	private int Armor;       //256
	
	private int Boots;       //512
	private int Chestplate;  //1024
	private int Helm;        //2048
	private int Leggings;    //4096
	private final IArtifactComponent baseDamage = new ComponentNormalDamage();

	public FactoryArtifact() {
		registerComponent(new ComponentHeal());
		registerComponent(new ComponentDamage());
		registerComponent(new ComponentFireball());
		registerComponent(new ComponentHarvesting());
		registerComponent(new ComponentLightning());
		registerComponent(new ComponentMining());
		registerComponent(new ComponentExplosive());
		registerComponent(new ComponentLight());
		registerComponent(new ComponentCashout());
		registerComponent(new ComponentResistance());
		registerComponent(new ComponentJumping());
		registerComponent(new ComponentVision());
		registerComponent(new ComponentBreathing());
		registerComponent(new ComponentFoodie());
		registerComponent(new ComponentSpeed());
		registerComponent(new ComponentHealth());
		registerComponent(new ComponentRepair());
		registerComponent(new ComponentMassWeb());
		registerComponent(new ComponentAirWalk());
	}
	
	@Override
	public IArtifactComponent getComponent(int componentID) {
		IArtifactComponent eff = (IArtifactComponent)effects.get(componentID);
		if(eff == null) {
			System.out.print("ERROR: NO EFFECT ID "+ componentID +" REGISTERED!");
		}
		return eff;
	}

	@Override
	public ItemStack generateRandomArtifact() {
		ItemStack artifact = new ItemStack(ItemArtifact.instance, 1, 0);
		return applyRandomEffects(artifact);
	}

	@Override
	public void registerComponent(IArtifactComponent component) {
		effects.put(effects.size()+1, component);
	}

	@Override
	public int getComponentCount() {
		return effects.size();
	}
	
	public ItemStack applyRandomEffects(ItemStack artifact) {
		artifact.stackTagCompound = createDefault();
		String nameChunk = "";
		Amulet = 1;
		Dagger = 1;
		Figurine = 1;
		Ring = 1;
		Staff = 1;
		Sword = 1;
		Trinket = 1;
		Wand = 1;
		Armor = 1;
		Boots = 1;
		Chestplate = 1;
		Helm = 1;
		Leggings = 1;
		int flags,effID;
		String artiName = "";
		Vector effectsOnItem = new Vector();
		IArtifactComponent c;
		int count = 0, a[];
		int numEff = rand.nextInt(5)+1;
		a = new int[numEff];
		for(; numEff > 0; numEff--) {
			effID = rand.nextInt(effects.size())+1;
			if(effID == 17 && a.length < 3) {
				numEff++;
				a = new int[numEff];
			}
			c = getComponent(effID);
			if(effectsOnItem.contains(c)) {
				numEff++;
				continue;
			}
			String trigName = c.getRandomTrigger(rand);
			if(artifact.stackTagCompound.hasKey(trigName)) {
				//make NBTTagLists to remove this condition;
				numEff++;
				continue;
			}
			effectsOnItem.add(c);
			if(effID == 9) {
				int bonus = a.length*5;
				if(numEff == a.length) {
					numEff = 1;
					bonus = 0;
					artifact.stackSize = 10;
				}
				artifact.stackTagCompound.setInteger("cashBonus", bonus);
			}
			if(effID == 7) {
				if(numEff == a.length) {
					if(trigName == "onDropped") {
						numEff = 1;
						artifact.stackSize = 10;
					}
				}
			}
			//System.out.println(c.getName());
			artifact.stackTagCompound.setInteger(trigName, effID);
			if(trigName == "onHeld") {
				artifact = c.attached(artifact, rand);
			}
			a[numEff-1] = effID;
			flags = c.getTextureBitflags();
			Amulet += flags % 2;
			flags >>= 1;
			Dagger += flags % 2;
			flags >>= 1;
			Figurine += flags % 2;
			flags >>= 1;
			Ring += flags % 2;
			flags >>= 1;
			Staff += flags % 2;
			flags >>= 1;
			Sword += flags % 2;
			flags >>= 1;
			Trinket += flags % 2;
			flags >>= 1;
			Wand += flags % 2;
			flags >>= 1;
			//Armor += flags % 2;
			flags >>= 1;
			Boots += flags % 2;
			flags >>= 1;
			Chestplate += flags % 2;
			flags >>= 1;
			Helm += flags % 2;
			flags >>= 1;
			Leggings += flags % 2;

			flags = c.getNegTextureBitflags();
			Amulet -= flags % 2;
			flags >>= 1;
			Dagger -= flags % 2;
			flags >>= 1;
			Figurine -= flags % 2;
			flags >>= 1;
			Ring -= flags % 2;
			flags >>= 1;
			Staff -= flags % 2;
			flags >>= 1;
			Sword -= flags % 2;
			flags >>= 1;
			Trinket -= flags % 2;
			flags >>= 1;
			Wand -= flags % 2;
			flags >>= 1;
			Armor -= flags % 2;
			flags >>= 1;
			Boots -= flags % 2;
			flags >>= 1;
			Chestplate -= flags % 2;
			flags >>= 1;
			Helm -= flags % 2;
			flags >>= 1;
			Leggings -= flags % 2;
			if(rand.nextInt(4) == 0) {
				numEff--;
				if(numEff > 0)
					a[numEff-1] = 0;
			}
		}
		Amulet = Math.max(Amulet, 0);
		Dagger = Math.max(Dagger, 0);
		Figurine = Math.max(Figurine, 0);
		Ring = Math.max(Ring, 0);
		Staff = Math.max(Staff, 0);
		Sword = Math.max(Sword, 0);
		Trinket = Math.max(Trinket, 0);
		Wand = Math.max(Wand, 0);
		Armor = Math.max(Armor*4, 0);
		Boots = Math.max(Boots, 0);
		Chestplate = Math.max(Chestplate, 0);
		Helm = Math.max(Helm, 0);
		Leggings = Math.max(Leggings, 0);
		//end loop
		int t = Amulet + Dagger + Figurine + Ring + Staff + Sword + Trinket + Wand;
		int r = 0;
		String iconType;
		artifact.stackTagCompound.setInteger("armorType", -1);
		if(t > 0) {
			r = rand.nextInt(t);
			if((r -= Amulet) < 0) {
				iconType = "Amulet";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberAmulets;
			}
			else if((r -= Dagger) < 0) {
				iconType = "Dagger";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberDaggers;
				if(!effectsOnItem.contains(2)) {
					artifact = baseDamage.attached(artifact, rand);
				}
			}
			else if((r -= Figurine) < 0) {
				iconType = "Figurine";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberFigurines;
			}
			else if((r -= Ring) < 0) {
				iconType = "Ring";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberRings;
			}
			else if((r -= Staff) < 0) {
				iconType = "Staff";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberStaffs;
			}
			else if((r -= Sword) < 0) {
				iconType = "Sword";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberSwords;
				if(!effectsOnItem.contains(2)) {
					artifact = baseDamage.attached(artifact, rand);
				}
			}
			else if((r -= Trinket) < 0) {
				iconType = "Trinket";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberTrinkets;
			}
			else if((r -= Wand) < 0) {
				iconType = "Wand";
				t = ((FactoryItemIcons)(ArtifactsAPI.itemicons)).numberWands;
			}
			else if((r -= Armor) < 0) {
				/*t = Boots+Chestplate+Helm+Leggings;
				if(t < 1)
					t = 1;
				r = rand.nextInt(t);
				if((r -= Boots) < 0) {
					iconType = "Boots";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 3);
				}
				else if((r -= Chestplate) < 0) {
					iconType = "Chestplate";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 1);
				}
				else if((r -= Helm) < 0) {
					iconType = "Helm";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 0);
				}
				else if((r -= Leggings) < 0) {
					iconType = "Leggings";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 2);
				}
				else {*/
					iconType = "Artifact";
					t = 1;
				//}
			}
			else {
				iconType = "Artifact";
				t = 1;
			}
		}
		else {
			iconType = "Artifact";
			t = 1;
		}
		int r2=-1,r3=-1,r4 = rand.nextInt(10);
		String matName = "[Material]";
		switch(r4) {
			case 0:
				matName = "Wood";
				r4 = 0;
				break;
			case 1:
			case 8:
				r4 = 1;
				matName = "Stone";
				break;
			case 2:
			case 7:
			case 9:
				r4 = 2;
				matName = "Iron";
				break;
			case 4:
				r4 = 4;
				matName = "Diamond";
				break;
			case 3:
			case 5:
			case 6:
			case 10:
				r4 = 3;
				matName = "Gold";
				break;
		}
		artifact.stackTagCompound.setString("iconName", iconType);
		artifact.stackTagCompound.setString("matName", matName);
		if(effectsOnItem.size() > 1) {
			r2 = rand.nextInt(effectsOnItem.size());
			c = (IArtifactComponent) effectsOnItem.get(r2);
			//System.out.println("Pre: " + c.getPreAdj(rand));
			nameChunk = c.getPreAdj(rand);
			artifact.stackTagCompound.setString("preadj", nameChunk);
			artiName = nameChunk + " ";
			artiName += matName + " " + iconType;
			do {
				r3 = rand.nextInt(effectsOnItem.size());
			} while(r2 == r3);
			c = (IArtifactComponent) effectsOnItem.get(r3);
			nameChunk = c.getPostAdj(rand);
			artifact.stackTagCompound.setString("postadj", nameChunk);
			artiName += " " + nameChunk;
		}
		else {
			//System.out.println("Singular");
			if(rand.nextBoolean()) {
				r2 = rand.nextInt(effectsOnItem.size());
				c = (IArtifactComponent) effectsOnItem.get(r2);
				nameChunk = c.getPreAdj(rand);
				artifact.stackTagCompound.setString("preadj", nameChunk);
				artifact.stackTagCompound.setString("postadj", "");
				artiName = nameChunk + " " + matName + " " + iconType;
			}
			else {
				r3 = rand.nextInt(effectsOnItem.size());
				c = (IArtifactComponent) effectsOnItem.get(r3);
				nameChunk = c.getPostAdj(rand);
				artifact.stackTagCompound.setString("preadj", "");
				artifact.stackTagCompound.setString("postadj", nameChunk);
				artiName = matName + " " + iconType + " " + nameChunk;
			}
		}
		r = rand.nextInt(t);
		artifact.stackTagCompound.setString("name", artiName);
		artifact.stackTagCompound.setString("icon", iconType+(r+1));
		int col = Color.HSBtoRGB((float)(rand.nextInt(360) / 360F), .8f, 1);
		artifact.stackTagCompound.setLong("overlay_color", col);
		artiName = "";
		artifact.stackTagCompound.setInteger("material", r4);
		
		artifact.stackTagCompound.setIntArray("allComponents", a);
		if(rand.nextInt(8) == 0) {
			artifact = enchantArtifact(artifact, effectsOnItem, (iconType == "Sword" || iconType == "Dagger"));
		}
		return artifact;
	}
	

	public NBTTagCompound createDefault()
	{
		NBTTagCompound nbt = new NBTTagCompound("tag");
		nbt.setInteger("material",0);
		nbt.setString("name", "Blank Artifact");
		nbt.setString("icon", "Artifact");
		int col = Color.HSBtoRGB((float)(rand.nextInt(360) / 360F), .8f, 1);
		nbt.setLong("overlay_color", col);
		nbt.setIntArray("allComponents", new int[] {0,0,0,0,0});
		return nbt;
	}
	
	private ItemStack enchantArtifact(ItemStack artifact, Vector effectsOnItem) {
		return enchantArtifact(artifact, effectsOnItem, false);
	}
	
	private ItemStack enchantArtifact(ItemStack artifact, Vector effectsOnItem, boolean isSword) {
		Item item = Item.pickaxeWood;
		int level = 6;
		do {
			level += 2;
		} while(level < 40 && rand.nextInt(8) != 0);
		if((isSword || effectsOnItem.contains(getComponent(2))) && rand.nextBoolean()) {
			switch(artifact.stackTagCompound.getInteger("material")) {
				case 0:
					item = Item.swordWood;
					break;
				case 1:
					item = Item.swordStone;
					break;
				case 2:
					item = Item.swordIron;
					break;
				case 3:
					item = Item.swordGold;
					break;
				case 4:
					item = Item.swordDiamond;
					break;
			}
		}
		else if(effectsOnItem.contains(getComponent(4)) || effectsOnItem.contains(getComponent(6))) {
			switch(artifact.stackTagCompound.getInteger("material")) {
				case 0:
					item = Item.pickaxeWood;
					break;
				case 1:
					item = Item.pickaxeStone;
					break;
				case 2:
					item = Item.pickaxeIron;
					break;
				case 3:
					item = Item.pickaxeGold;
					break;
				case 4:
					item = Item.pickaxeDiamond;
					break;
			}
		}
		else {
			int r2 = (int) Math.min(Math.ceil(level / 7D), 3);
			artifact.addEnchantment(Enchantment.unbreaking, r2);
			artifact.stackTagCompound.setString("name", "Unbreaking " + artifact.stackTagCompound.getString("name"));
			return artifact;
		}
		//System.out.println(level + " levels to play with.");
		ItemStack stack = new ItemStack(item);
		stack = EnchantmentHelper.addRandomEnchantment(rand, stack, level);
		if(stack.stackTagCompound != null) {
			artifact.stackTagCompound.setTag("ench", stack.stackTagCompound.getTag("ench").copy());
			NBTTagList tags = artifact.getEnchantmentTagList();
			int firstID = ((NBTTagCompound)tags.tagAt(0)).getShort("id");
			String enchName;
			switch(firstID) {
				case 0:
					enchName = "Protecting ";
					break;
				case 1:
					enchName = "Fireproof ";
					break;
				case 2:
					enchName = "Floating ";
					break;
				case 3:
					enchName = "Blast Protecting ";
					break;
				case 4:
					enchName = "Anti-Arrow ";
					break;
				case 5:
					enchName = "Breathing ";
					break;
				case 6:
					enchName = "Underwater ";
					break;
				case 7:
					enchName = "Thorny ";
					break;
				case 16:
					enchName = "Sharp ";
					break;
				case 17:
					enchName = "Smiting ";
					break;
				case 18:
					enchName = "Spider-Crushing ";
					break;
				case 19:
					enchName = "Forcefull ";
					break;
				case 20:
					enchName = "Firey";
					break;
				case 21:
					enchName = "Looting ";
					break;
				case 32:
					enchName = "Efficient ";
					break;
				case 33:
					enchName = "Gentle ";
					break;
				case 34:
					enchName = "Unbreaking ";
					break;
				case 35:
					enchName = "Fortuitous ";
					break;
				default:
					enchName = "";
			}
			artifact.stackTagCompound.setString("enchName", enchName);
			artifact.stackTagCompound.setString("name", enchName + artifact.stackTagCompound.getString("name"));
		}
		return artifact;
	}

	@Override
	public ItemStack applyEffectByID(ItemStack artifact, int id, String trigger) {
		if(artifact.stackTagCompound == null) {
			artifact.stackTagCompound = this.createDefault();
		}
		int[]a = artifact.stackTagCompound.getIntArray("allComponents");
		Vector effectsOnItem = new Vector();
		for(int i = 0; i < 5; ++i) {
			if(a[i] != 0) {
				effectsOnItem.add(a[i]);
			}
		}
		int numEff = 0;
		
		IArtifactComponent c = getComponent(id);
		if(!effectsOnItem.contains(c)) {
			if(trigger == null || trigger.equals(""))
				trigger = c.getRandomTrigger(rand);
			if(!artifact.stackTagCompound.hasKey(trigger)) {
				effectsOnItem.add(c);
				if(id == 9) {
					int bonus = a.length*5;
					if(numEff == a.length) {
						numEff = 1;
						bonus = 0;
						artifact.stackSize = 10;
					}
					artifact.stackTagCompound.setInteger("cashBonus", bonus);
				}
				if(id == 7) {
					if(numEff == a.length) {
						if(trigger == "onDropped") {
							numEff = 1;
							artifact.stackSize = 10;
						}
					}
				}
				artifact.stackTagCompound.setInteger(trigger, id);
				for(int i = 0; i < 5; ++i) {
					if(a[i] == 0) {
						a[i] = id;
						i = 99;
					}
				}
				artifact.stackTagCompound.setIntArray("allComponents", a);
			}
		}
		return artifact;
	}

	@Override
	public void setTreasureGeneration(String treasureString, int rarity) {
		ChestGenHooks.getInfo(treasureString).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, rarity));
	}
}
