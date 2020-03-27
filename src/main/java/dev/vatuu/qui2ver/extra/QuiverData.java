package dev.vatuu.qui2ver.extra;

import dev.vatuu.qui2ver.Qui2ver;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.TrueCondition;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuiverData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        DataGenerator gen = e.getGenerator();
        ExistingFileHelper helper = e.getExistingFileHelper();

        if(e.includeClient()) {
            gen.addProvider(new ItemModels(gen, helper));
            gen.addProvider(new Language(gen));
        }

        if(e.includeServer()) {
            gen.addProvider(new Recipes(gen));
        }
    }

    public static class Recipes extends RecipeProvider implements IDataProvider, IConditionBuilder {

        public Recipes(DataGenerator gen) { super(gen); }

        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            ResourceLocation id = new ResourceLocation(Qui2ver.MODID, "craft_quiver");
            ConditionalRecipe.builder()
                .addCondition(TrueCondition.INSTANCE)
                .addRecipe(
                    ShapedRecipeBuilder.shapedRecipe(Qui2ver.quiverItem.get())
                        .patternLine("sl ")
                        .patternLine("l l")
                        .patternLine("il ")
                        .key('s', Tags.Items.STRING)
                        .key('l', Tags.Items.LEATHER)
                        .key('i', Tags.Items.INGOTS_IRON)
                        .setGroup("")
                        .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                        ::build
                    )
                .setAdvancement(id,
                    ConditionalAdvancement.builder()
                        .addCondition(TrueCondition.INSTANCE)
                        .addAdvancement(Advancement.Builder.builder()
                            .withParentId(new ResourceLocation("minecraft", "recipes/root"))
                            .withRewards(AdvancementRewards.Builder.recipe(id))
                            .withRequirementsStrategy(IRequirementsStrategy.OR)
                            .withCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                            .withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id)))
                    )
                .build(consumer, id);
        }
    }

    public static class ItemModels extends ItemModelProvider {

        public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
            super(gen, Qui2ver.MODID, helper);
        }

        protected void registerModels() {
            getBuilder("quiver")
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/quiver"));
        }

        public String getName() {
            return "Item Models";
        }
    }

    public static class Language extends LanguageProvider {

        public Language(DataGenerator gen) {
            super(gen, Qui2ver.MODID, "en_us");
        }

        protected void addTranslations() {
            add(Qui2ver.quiverItem.get(), "Quiver");
        }
    }
}
