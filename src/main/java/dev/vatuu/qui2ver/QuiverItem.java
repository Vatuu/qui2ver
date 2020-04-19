package dev.vatuu.qui2ver;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class QuiverItem extends Item {

    public QuiverItem(Item.Properties settings) {
        super(settings);
        DispenserBlock.registerDispenseBehavior(this, new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                return canEquip(source, stack) ? stack : super.dispenseStack(source, stack);
            }
        });
    }

    private static boolean canEquip(IBlockSource src, ItemStack stack) {
        BlockPos blockpos = src.getBlockPos().offset(src.getBlockState().get(DispenserBlock.FACING));
        List<ServerPlayerEntity> list = src.getWorld().getEntitiesWithinAABB(ServerPlayerEntity.class, new AxisAlignedBB(blockpos), EntityPredicates.NOT_SPECTATING);
        if (list.isEmpty()) {
            return false;
        } else {
            ServerPlayerEntity p = list.get(0);
            if (p.inventory.getStackInSlot(41).isEmpty()) {
                ItemStack equip = stack.split(1);
                p.inventory.setInventorySlotContents(41, equip);
                src.getWorld().playSound(null, p.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1, 1);
                return true;
            }
            return false;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity p, Hand hand) {
        ItemStack stack = p.getHeldItem(hand);
        ItemStack slot = p.inventory.getStackInSlot(41);
        if (slot.isEmpty()) {
            ItemStack equip = stack.copy();
            equip.setCount(1);
            p.inventory.setInventorySlotContents(41, equip);
            world.playSound(null, p.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1, 1);
            if(!p.isCreative())
                stack.shrink(1);
            return ActionResult.func_226248_a_(stack);
        } else {
            return ActionResult.func_226251_d_(stack);
        }
    }
}
