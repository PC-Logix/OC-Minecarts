package mods.ocminecart.interaction;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEI implements IModPlugin {

    private static IJeiRuntime runtime;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }

    @Override
    public void register(IModRegistry registry) {
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public static IJeiRuntime getRuntime() {
        return runtime;
    }

    public static ItemStack getStackUnderMouse(){
        return runtime.getItemListOverlay().getStackUnderMouse();
    }
}
