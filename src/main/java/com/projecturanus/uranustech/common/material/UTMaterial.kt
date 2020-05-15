package com.projecturanus.uranustech.common.material

import com.google.gson.annotations.SerializedName
import com.projecturanus.uranustech.MODID
import com.projecturanus.uranustech.api.material.Material
import com.projecturanus.uranustech.api.material.MaterialStack
import com.projecturanus.uranustech.api.material.SimpleMaterial
import com.projecturanus.uranustech.api.material.WildcardMaterial
import com.projecturanus.uranustech.api.material.compound.Compound
import com.projecturanus.uranustech.api.material.element.Element
import com.projecturanus.uranustech.api.material.form.Form
import com.projecturanus.uranustech.api.material.form.Forms.*
import com.projecturanus.uranustech.api.material.info.MaterialInfo
import com.projecturanus.uranustech.api.tool.ToolHeads.*
import com.projecturanus.uranustech.common.formRegistry
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

interface MaterialContainer {
    val stack: MaterialStack
}

class UTMaterial : SimpleMaterial() {

}

/**
 * 衡量物质的量
 */
data class JsonMaterialStack(val material: String, val amount: Long)
/**
 * 用于产生魔法电解机/离心机配方
 * @param stacks 生成的物质列表
 * @param dividedStacks　感觉没什么用
 * @param divider　配方接受多少输入
 */
data class MaterialComponent(val stacks: Array<JsonMaterialStack>, val dividedStacks: Array<JsonMaterialStack>, val divider: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaterialComponent) return false

        if (!stacks.contentEquals(other.stacks)) return false
        if (!dividedStacks.contentEquals(other.dividedStacks)) return false
        if (divider != other.divider) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stacks.contentHashCode()
        result = 31 * result + dividedStacks.contentHashCode()
        result = 31 * result + divider
        return result
    }
}

/**
 * 材料的json表示
 */
open class JsonMaterial(var name: String, val tags: List<String>,
                        @SerializedName("textureSet")
                        /**
                         * 内部纹理集
                         */
                        var textureSetInternal: String,
                        /**
                         * 燃烧时间
                         */
                        val burnTime: Int,
                        @SerializedName("description")
                        /**
                         * 内部描述
                         */
                        val descriptionInternal: Array<String>?,
                        /**
                         * 副产品
                         */
                        val byProducts: Array<String>?,
                        /**
                         *　工具手柄材料，比如钍锤子需要钨钢杆做手柄，则这里填 any_tungstensteel_rod 之类的
                         */
                        val handleMaterial: String,
                        /**
                         * 是否隐藏
                         */
                        val hidden: Boolean,
                        /**
                         * 固体（锭）颜色，24位
                         */
                        val colorSolid: Int,
                        /**
                         * 液体（熔融）颜色，24位
                         */
                        val colorLiquid: Int,
                        /**
                         * 气体颜色, 24位
                         */
                        val colorGas: Int,
                        /**
                         * 等离子颜色，24位
                         */
                        val colorPlasma: Int,
                        /**
                         * 全名(displayname, unlocalized)
                         */
                        val fullName: String,
                        /**
                         * tooltip的化学式子
                         */
                        val tooltipChemical: String,
                        /**
                         * 密度(g * cm ^ -3)
                         */
                        val gramPerCubicCentimeter: Double,
                        /**
                         * 增产系数
                         */
                        val oreMultiplier: Int,
                        /**
                         * 处理(processing)增产系数
                         */
                        val oreProgressingMultiplier: Int,
                        /**
                         * 作为工具的耐久
                         */
                        val toolDurability: Long,
                        /**
                         * 作为工具的速度
                         */
                        val toolSpeed: Float,
                        /**
                         * 工具类型
                         */
                        val toolTypes: Int,
                        /**
                         * 工具品质
                         */
                        val toolQuality: Int,
                        /**
                         *熔点
                         */
                        val meltingPoint: Int,
                        /**
                         * 沸点
                         */
                        val boilingPoint: Int,
                        /**
                         * 离子体温度
                         */
                        val plasmaPoint: Int,
                        /**
                         * 中子数目
                         */
                        val neutrons: Int = 0,
                        /**
                         * 质子数目
                         */
                        val protons: Int = 0,
                        /**
                         * 电子数目
                         */
                        val electrons: Int = 0,
                        /**
                         * 组成
                         */
                        val components: MaterialComponent?): Material {
    var validFormsCache: List<Form> = emptyList()
    var elementsCache: Set<Element> = emptySet()

    override fun getDescription() = descriptionInternal?.toList() ?: emptyList()

    override fun isHidden() = hidden

    override fun getTextureSet() = textureSetInternal

    override fun getComposition() = emptyMap<Compound, Int>()

    override fun getElements() = elementsCache

    override fun getColor() = colorSolid

    override fun addInfo(info: MaterialInfo?) {}

    lateinit var infos: Map<Identifier, MaterialInfo>

    override fun getInfos() = infos.values

    override fun <T : MaterialInfo?> getInfo(infoId: Identifier?) = infos[infoId] as T

    override fun getChemicalCompound() = tooltipChemical

    override fun getValidForms() = validFormsCache

    override fun getIdentifier() = Identifier(MODID, name)
    override fun toString(): String {
        return "JsonMaterial(name='$name', tags=$tags, textureSetInternal='$textureSetInternal', descriptionInternal=${descriptionInternal?.contentToString()}, handleMaterial='$handleMaterial', hidden=$hidden, colorSolid=$colorSolid, colorLiquid=$colorLiquid, colorGas=$colorGas, colorPlasma=$colorPlasma, fullName='$fullName', tooltipChemical='$tooltipChemical', gramPerCubicCentimeter=$gramPerCubicCentimeter, oreMultiplier=$oreMultiplier, oreProgressingMultiplier=$oreProgressingMultiplier, toolDurability=$toolDurability, toolSpeed=$toolSpeed, toolTypes=$toolTypes, toolQuality=$toolQuality, meltingPoint=$meltingPoint, boilingPoint=$boilingPoint, plasmaPoint=$plasmaPoint, neutrons=$neutrons, protons=$protons, electrons=$electrons, components=$components)"
    }
}

val INGOT_FORMS = listOf(INGOT, NUGGET, PLATE, DUST, STICK, INGOT_DOUBLE, INGOT_QUADRUPLE, INGOT_QUINTUPLE, INGOT_TRIPLE, PLATE, PLATE_TINY, PLATE_GEM_TINY, PLATE_QUADRUPLE, PLATE_QUINTUPLE, PLATE_TRIPLE, PLATE_DOUBLE, PLATE_CURVED, PLATE_DENSE)
val TOOL_FORMS = listOf(TOOL_HEAD_ARROW, TOOL_HEAD_AXE, TOOL_HEAD_AXE_DOUBLE, TOOL_HEAD_BUZZ_SAW, TOOL_HEAD_CHAINSAW, TOOL_HEAD_CHISEL, TOOL_HEAD_CONSTRUCTION_PICKAXE,
        TOOL_HEAD_DRILL, TOOL_HEAD_FILE, TOOL_HEAD_HAMMER, TOOL_HEAD_HOE, TOOL_HEAD_PICKAXE, TOOL_HEAD_PLOW, TOOL_HEAD_SAW, TOOL_HEAD_SCREWDRIVER,
        TOOL_HEAD_SENSE, TOOL_HEAD_SHOVEL, TOOL_HEAD_SPADE, TOOL_HEAD_SWORD, TOOL_HEAD_UNIVERSAL_SPADE, TOOL_HEAD_WRENCH)
val GEM_FORMS = listOf(GEM, PLATE_GEM, NUGGET, STICK, DUST, LENS, PLATE_GEM_TINY)
val PART_FORMS = listOf(ROUND, RING, BOLT, ROTOR, CART_WHEELS, SCREW)
val STONE_FORMS = listOf(STONE, SMALL_BRICKS, SMALL_TILES, SMOOTH, BRICKS, BRICKS_CHISELED, BRICKS_CRACKED, BRICKS_MOSSY, BRICKS_REDSTONE, BRICKS_REINFORCED, COBBLE, COBBLE_MOSSY, SQUARE_BRICKS, TILES, WINDMILL_TILES_A, WINDMILL_TILES_B)

/**
 * 矿物词典
 */
class UTWildcardMaterial(identifier: Identifier, vararg submaterials: Material): SimpleMaterial(), WildcardMaterial {
    val knownSubmaterials = mutableListOf<Material>()
    val materialTag: Tag<Material>

    override fun subMaterials() = knownSubmaterials

    override fun isSubtype(material: Material?) = knownSubmaterials.contains(material)

    init {
        knownSubmaterials.addAll(submaterials.flatMap { if (it is WildcardMaterial) { it.subMaterials() + it } else listOf(it) })
        setIdentifier(identifier)
        materialTag = Tag.Builder.create<Material>().add(*knownSubmaterials.toTypedArray()).build(identifier)
    }
}

class TagProcessor(val tags: List<String>) {
    fun getForms(): Set<Form> {
        val set = hashSetOf<Form>()
        tags.forEach {
            if (it.startsWith("ITEMGENERATOR."))
                if (formRegistry.asSequence().any { form -> form.asString() == it.removePrefix("ITEMGENERATOR.").removeSuffix("S").toLowerCase() })
                    set += formRegistry[Identifier(MODID, it.removePrefix("ITEMGENERATOR.").removeSuffix("S").toLowerCase())]
                else
                    when (it.removePrefix("ITEMGENERATOR.")) {
                        "INGOTS" -> set.addAll(INGOT_FORMS)
                        "INGOTS_HOT" -> set.addAll(INGOT_FORMS + INGOT_HOT)
                        "PARTS" -> set.addAll(PART_FORMS)
                        "DIRTY_DUST" -> set += DUST_IMPURE
                        "GEMS" -> set.addAll(GEM_FORMS)
                        "ORES" -> set += ORE
                        "MOLTEN" -> set += MOLTEN
                        "GAS" -> set += GAS
                    }
            else if (it == "PROPERTIES.HAS_TOOL_STATS")
                set.addAll(TOOL_FORMS)
            else if (it == "PROPERTIES.STONE")
                set.addAll(STONE_FORMS)
        }
        return set
    }
}

