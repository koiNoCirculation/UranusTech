package com.projecturanus.uranustech.api.material.info;

import com.projecturanus.uranustech.api.material.Constants;
import com.projecturanus.uranustech.api.material.Material;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class ToolInfo implements MaterialInfo, ToolMaterial {
    /**
     * TODO More Types
     * The Types Tools allowed, 0 = No Tools, 1 = Flint/Stone/Wood Tools, 2 = Early Tools, 3 = Advanced Tools
     */
    public int toolTypes = 0;
    /**
     * The Quality of the Material as Tool Material (ranges from 0 to 15)
     */
    public int toolQuality = 0;
    /**
     * The Durability of the Material in Tool Form
     */
    public long toolDurability = 0;
    /**
     * The Speed of the Material as mining Material
     */
    public float toolSpeed = 1.0F;

    /**
     * Handle material, can be null.
     */
    public Material handleMaterial;

    @Override
    public Identifier getIdentifier() {
        return Constants.TOOL_INFO;
    }

    @Override
    public int getDurability() {
        return (int) toolDurability;
    }

    @Override
    public float getMiningSpeed() {
        return toolSpeed;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return toolTypes;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
}