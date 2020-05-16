package com.projecturanus.uranustech.client.model

import com.google.gson.*
import com.projecturanus.uranustech.MODID
import com.projecturanus.uranustech.logger
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.io.Serializable
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

val builderMap = ConcurrentHashMap<Identifier, ModelBuilder>()

/**
 * Kotlin alternative for ModelTransformation in Minecraft.
 * @see net.minecraft.client.render.model.json.ModelTransformation
 */

/**
 * https://minecraft-zh.gamepedia.com/模型
 * https://minecraft.gamepedia.com/Model
 * {
 *      "parent":"modid:someblock", //承其他模型文件，格式为命名空间ID。如果同时设置了"parent"和"elements"，此模型的"elements"标签将覆盖前一个模型下的"elements"标签。
 *      "display": {
 *              "thirdperson_righthand":  { //键名可以是thirdperson_righthand、thirdperson_lefthand、firstperson_righthand、firstperson_lefthand、gui、head、ground或fixed。物品模型于不同位置下的显示设置。分别设置旋转，平移和缩放。fixed指的是当被摆放在物品展示框上时，其他的显示位置如同其名。值得注意的是平移优先于旋转。
 *                   "rotation": [ -90, 0, 0 ],
 *                   "translation": [ 0, 1, -3 ],
 *                   "scale": [ 0.55, 0.55, 0.55 ]
 *z               }
 *      },
 *      "textures": { //定义物品颗粒的材质以及模型所使用的材质变量。键值可以为材质文件的路径，格式为命名空间ID，也可以为另一个材质变量。
 *          "layer${i}": "block/torch",  //仅用于指定在物品栏内使用的物品的图标。允许有超过一层（比如刷怪蛋），但是每个物品可能的层数都是硬编码的。必须结合"builtin/generated"能用。
 *          "particle": "modid:particles/some", //物品对应的颗粒的材质。用于确定食物产生的碎屑颗粒，以及确定屏障颗粒（但它的破坏方块可以总是永远是items/barrier.png），否则将使用"layer0"。
 *          "材质变量名": "some" //定义一个材质变量并分配材质。
 *      },
 *      "gui_light": "front", //仅在gui中使用。可以是front或side。设为side，模型会被渲染为长方体形状。设为front，模型会以平面材质填满一整格。
 *      "elements": [ //列出模型的所有元素,这些元素只能为长方体形态。如果同时设置了"parent"和"elements"，此模型的"elements"标签将覆盖父类模型"parent"下的"elements"标签。
 *          {
 *              "from": [x, y, z], //根据[x, y, z]格式，指定一个长方体的起始点。数值必须为-16到32之间。
 *              "to": [x, y, z], //根据[x, y, z]格式，指定一个长方体的结束点。数值必须为-16到32之间。
 *              "rotation": {
 *                  "origin": [x,y,z], //根据坐标[x, y, z]设置旋转中心。
 *                  "axis": "x", //or "y", "z" 旋转的一个且只能为一个轴，可以为"x"，"y"或"z"
 *                  "angle": -45, //[45,45] steplength == 22.5 旋转的角度。可以为45到-45度，以22.5度为增量。
 *                  "rescale": true //or false 是否缩放为整个长方体大小。可以为"true"或"false"。默认为"false"。
 *              },
 *              "faces": { //包含单个"element"的所有面。若有一个面没有定义，则不渲染此面。
 *                  "down": {
 *                      "uv": {
 *                      },
 *                      "texture": "modid:texture/xxx", //指定所使用的材质变量，变量前加上#。材质变量于上文的材质变量名定义。
 *                      "cullface": "down", //指定的面可以为：down, up, north, south, west, 或 east。该面面的亮度计算等同于方块的指定面, and if unset, defaults to the side.
 *                      "rotation": 0, // 根据特定的角度旋转材质。可以为0，90，180或270。默认为0。旋转不会影响到uv所指定的材质部分，而是此部分的旋转角度。
 *                      "tintindex": "true" //确定是否使用硬编码对该材质进行重新着色。默认情况下不着色，任何数字都会决定着色器使用颜色（除了刷怪蛋，设置为0会使它使用第一种颜色，设置为1会使它使用第二种颜色）。请注意，只有特定的物品或物品的特定层（例如药水）可以着色，其他物品设置此项无效。
 *                  }
 *                  "up": {...},
 *                  "north": {...},
 *                  "west": {...},
 *                  "east": {...},
 *                  "south": {...},
 *              }
 *          }
 *      ]
 *      "overrides": [
 *          {
 *              "predicate": { //指定情况。
 *                  "情况标签名": "something" // 一个情况标签。参见物品标签查阅所有可用的标签。
 *              },
 *              "model": "modid:path" //当符合情况时使用的模型文件的路径，格式为命名空间ID。
 *          }
 *      ]
 * }
 */

/**
 * 变换向量
 */
data class Transformation(val rotation: Triple<Float, Float, Float>, val translation: Triple<Float, Float, Float>, val scale: Triple<Float, Float, Float>)

/**
 * 四元组
 */
data class Quadtuple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
): Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}

fun <T> Quadtuple<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)

/**
 * 对应"faces"字段
 */
data class ModelFace(var uv: Quadtuple<Int, Int, Int, Int> = Quadtuple(0, 0, 16, 16),
                     var texture: String = "all",
                     var tintIndex: Int = -1,
                     var cullFace: String = "",
                     var rotation: Int = -1,
                     var shade: Boolean = true)

/**
 * 对应"elements"字段
 */
data class ModelElement(var from: Triple<Int, Int, Int> = Triple(0, 0, 0),
                        var to: Triple<Int, Int, Int> = Triple(16, 16, 16),
                        var faces: MutableMap<String, ModelFace> = mutableMapOf())

class ModelBuilder(var identifier: Identifier) {
    companion object {
        val gson = GsonBuilder().registerTypeAdapter(ModelBuilder::class.java, ModelSerializer()).create()
    }
    //language=none
    var parent: String = "builtin/generated"
    var json: String = ""
    //language=JSON
    val builtinJson: String
        get() = gson.toJson(this)
    var layerIndex = 0
    val textures = mutableMapOf<String, Identifier>()
    val elements = mutableListOf<ModelElement>()

    fun register() {
        if (builderMap.containsKey(identifier))
            logger.warn("Duplicate key found for model $identifier, skipping registration")
        else
            builderMap[identifier] = this
    }

    fun build(): JsonUnbakedModel =
            if (json.isNotEmpty()) JsonUnbakedModel.deserialize(json)
            else JsonUnbakedModel.deserialize(builtinJson)

    /**
     * 指定parent
     */
    fun itemSetup() {
        parent = "item/generated"
    }

    fun blockSetup() {
        parent = "block/block"
    }

    fun cubeSetup() {
        parent = "block/cube"
    }

    /**
     * 生成texture字段
     */
    fun texture(key: String, path: String) {
        texture(key, Identifier(MODID, path))
    }

    fun texture(key: String, path: Identifier) {
        textures += key to path //textures是个map
        //textures.put(key, path)
    }

    /**
     * 生成layer{i}字段
     */
    fun layer(path: String) {
        layer(Identifier(MODID, path))
    }

    fun layer(path: Identifier) {
        layer(layerIndex++, path)
    }

    fun layer(index: Int, path: Identifier) {
        textures += "layer$index" to path
        //textures.put("layer$i", path)
    }
}

fun model(identifier: Identifier, init: ModelBuilder.() -> Unit): ModelBuilder {
    val obj = ModelBuilder(identifier)
    obj.init()
    return obj
}

/**
 * 添加element字段
 */
fun ModelBuilder.element(init: ModelElement.() -> Unit): ModelElement {
    val obj = ModelElement()
    obj.init()
    elements += obj
    return obj
}

fun ModelElement.face(key: String, init: ModelFace.() -> Unit): ModelFace {
    val obj = ModelFace()
    obj.init()
    if (obj.cullFace.isEmpty())
        obj.cullFace = key
    faces[key] = obj
    return obj
}

/**
 * 添加face字段
 */
fun ModelElement.faces(init: ModelFace.() -> Unit) =
    Direction.values().map { ModelFace().apply(init).apply { cullFace = it.getName(); faces[it.getName()] = this } }.toList()

class ModelSerializer: JsonSerializer<ModelBuilder> {
    override fun serialize(src: ModelBuilder, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        src.run {
            obj.addProperty("parent", parent)
            obj.add("textures", JsonObject().also { textureObj -> textures.forEach { key, identifier -> textureObj.addProperty(key, identifier.toString()) } })
            if (elements.isNotEmpty())
                obj.add("elements", JsonArray().also { elementArray ->
                    elements.map { element ->
                        JsonObject().also { elementObj ->
                            elementObj.add("from", JsonArray().also { arr -> element.from.toList().forEach(arr::add) })
                            elementObj.add("to", JsonArray().also { arr -> element.to.toList().forEach(arr::add) })
                            elementObj.add("faces", JsonObject().also { facesObj -> element.faces.map { (key, face) ->
                                facesObj.add(key,
                                    JsonObject().also { faceObj ->
                                        face.apply {
                                            faceObj.add("uv", JsonArray().also { arr -> uv.toList().forEach(arr::add) })
                                            faceObj.addProperty("texture", texture)
                                            if (tintIndex != -1)
                                                faceObj.addProperty("tintindex", tintIndex)
                                            faceObj.addProperty("cullface", cullFace)
                                            if (rotation != -1)
                                                faceObj.addProperty("rotation", rotation)
                                        }
                                    }
                                )
                            } })
                        }
                    }.forEach(elementArray::add)
                })
        }
        return obj
    }
}
