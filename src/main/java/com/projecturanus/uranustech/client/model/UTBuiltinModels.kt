package com.projecturanus.uranustech.client.model

import com.projecturanus.uranustech.MODID
import com.projecturanus.uranustech.api.material.Constants
import com.projecturanus.uranustech.api.material.form.Forms
import com.projecturanus.uranustech.api.material.generate.GenerateTypes
import com.projecturanus.uranustech.api.material.info.ToolInfo
import com.projecturanus.uranustech.api.render.iconset.Iconsets
import com.projecturanus.uranustech.api.tool.Tool
import com.projecturanus.uranustech.api.tool.Tools
import com.projecturanus.uranustech.api.worldgen.Rock
import com.projecturanus.uranustech.api.worldgen.Rocks
import com.projecturanus.uranustech.client.clientLogger
import com.projecturanus.uranustech.common.formRegistry
import com.projecturanus.uranustech.common.material.JsonMaterial
import com.projecturanus.uranustech.common.material.STONE_FORMS
import com.projecturanus.uranustech.common.materialRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis

/**
 * 目前方块物品模型跟放置到世界的模型不太一致。。。。
 */
val modelCache = ConcurrentHashMap<Identifier, JsonUnbakedModel>()

fun initModels() = runBlocking {
    /**
     * 注册了一个回调...
     * ModelIdentifier对象长这样
     * {
     *      "namespace":"uranustech",
     *      "path": "actinium_ore",
     *      "variant": "rock=andesite"
     * }
     */
    ModelLoadingRegistry.INSTANCE.registerVariantProvider {
        ModelVariantProvider { modelId, context ->
            if (modelId.namespace == MODID) {
                when {
                    modelId.path.endsWith("ore") -> loadOreModel(modelId, context)
                    else -> loadCustomModel(modelId, context)
                }
            } else null
        }
    }

    /**
     * 这也是回调
     * resourceId长这样:
     * {
     *      "namespace": "minecraft",
     *      "path": "block/item_frame_map"
     * }
     */
    ModelLoadingRegistry.INSTANCE.registerResourceProvider {
        ModelResourceProvider { resourceId, context ->
            loadCustomModel(resourceId, context)
        }
    }

    clientLogger.info("Caching model iconsets...")
    launch {
        // Cache form models
        withContext(Dispatchers.Default) {
            clientLogger.info("Generate form models took " + measureTimeMillis {
                Iconsets.values().forEach {
                    /**
                     * 生产物品的模型
                     */
                    formRegistry.asSequence().filter { form -> form.generateType == GenerateTypes.ITEM }.forEach { form ->
                        //uranustech:fluid/rotor
                        model(Identifier(MODID, "${it.getName()}/${form.asString()}")) {
                            /**
                             * "parent": "item/generated"
                             */
                            itemSetup()
                            /**
                             * it: 当前material的iconset
                             * form: 当前material的form，小写
                             * assets\uranustech\textures\item\materialicons\fluid\rotor.png
                             */
                            //layer0
                            layer("item/materialicons/${it.getName()}/${form.asString()}")
                            //layer1
                            layer("item/materialicons/${it.getName()}/${form.asString()}_overlay")
                            register()
                        }
                    }
                }

                // Block item

                materialRegistry.forEach {
                    Rocks.values().forEach { rock ->
                        STONE_FORMS.forEach { st ->
                            //uranustech:actinium_stone_item
                            //identifier -> actinium
                            //form: stone
                            model(Identifier(MODID, "${it.identifier.path}_${rock.name.toLowerCase()}_${st.name.toLowerCase()}_item")) {
                                parent = "$MODID:block/${rock.name.toLowerCase()}_${st.name.toLowerCase()}"
                                register()
                            }
                        }
                    }
                }
            } + "ms")
        }

        // Cache block models
        withContext(Dispatchers.Default) {
            clientLogger.info("Generate block models took " + measureTimeMillis {
                //uranustech:${iconset}/${form}
                //materialIcons
                Iconsets.values().forEach {
                    formRegistry.asSequence().filter { form -> form.generateType == GenerateTypes.BLOCK && form != Forms.ORE && form !in STONE_FORMS }.forEach { form ->
                        model(Identifier(MODID, "${it.getName()}/${form.asString()}")) {
                            /**
                             * "parent":"block/block"
                             */
                            blockSetup()
                            /**
                             * 设定base和overlay
                             */
                            texture("base", "block/materialicons/${it.getName()}/${form.asString()}")
                            texture("overlay", "block/materialicons/${it.getName()}/${form.asString()}_overlay")
                            /**
                             * element应该是常规操作
                             */
                            element {
                                from = Triple(0, 0, 0)
                                to = Triple(16, 16, 16)
                                faces {
                                    texture = "#base"
                                    tintIndex = 0
                                }
                            }
                            element {
                                from = Triple(0, 0, 0)
                                to = Triple(16, 16, 16)
                                faces {
                                    texture = "#overlay"
                                }
                            }
                            register()
                        }
                    }

                    //uranustech:cube/andesite/ore
                    //block/materialicons/andesite/ore
                    //block/materialicons/andesite/ore_overlay
                    //block/stones/andersite/stone
                    //block/stones/andersite/ore

                    // Ore
                    Rocks.values().forEach { rock ->
                        // Ore block
                        model(Identifier(MODID, "${it.getName()}/${rock.asString()}/ore")) {
                            blockSetup()
                            /**
                             * 去对应textureset下面找ore和ore_overlay
                             */
                            texture("base", "block/materialicons/${it.getName()}/ore")
                            texture("overlay", "block/materialicons/${it.getName()}/ore_overlay")
                            /**
                             * 如果是石头的类型就取mc的石头底色，
                             * 否则去uranustech:block/stones/$StoneType/stone下面找一个叫stone的贴图
                             */
                            texture("stone", if (rock == Rocks.STONE) Identifier("minecraft", "block/stone") else Identifier(MODID, "block/stones/${rock.asString()}/stone"))
                            /**
                             * 粒子效果也是Ore
                             */
                            texture("particle", "block/materialicons/${it.getName()}/ore")
                            element {
                                from = Triple(0, 0, 0)
                                to = Triple(16, 16, 16)
                                faces {
                                    texture = "#stone"
                                }
                            }
                            element {
                                from = Triple(0, 0, 0)
                                to = Triple(16, 16, 16)
                                faces {
                                    texture = "#overlay"
                                }
                            }
                            element {
                                from = Triple(0, 0, 0)
                                to = Triple(16, 16, 16)
                                faces {
                                    texture = "#base"
                                    tintIndex = 0
                                }
                            }
                            register()
                        }
                    }
                }

                // Rock block model
                materialRegistry.forEach {

                    Rocks.values().forEach { rock ->
                        /**
                         * uranustech:surfur_dioxide_andersite
                         */
                        model(Identifier(MODID, "${it.identifier.path}_stone")) {
                            parent = "block/cube_all"
                            when (rock) {
                                Rocks.STONE -> {
                                    texture("all", Identifier("minecraft", "block/stone"))
                                }
                                else -> {
                                    texture("all", "block/stones/${rock.name.toLowerCase()}/stone")
                                }
                            }
                            register()
                        }
                    }
                }
            } + "ms")
        }

        // Cache tool models
        withContext(Dispatchers.Default) {
            // Cache tool models
            clientLogger.info("Generate tool models took " +
                    measureTimeMillis {
                        Iconsets.values().forEach {
                            formRegistry.asSequence().filter { form -> form.generateType == GenerateTypes.TOOL }.map { form -> form as Tool }.forEach { tool ->
                                if (tool.hasHandleMaterial()) {
                                    Iconsets.values().forEach { handleSet ->
                                        when (tool) {
                                            Tools.FILE, Tools.SCREWDRIVER, Tools.SAW, Tools.CHISEL -> model(Identifier(MODID, "${it.getName()}/${handleSet.getName()}/${tool.asString()}")) {
                                                itemSetup()
                                                layer("item/materialicons/${it.getName()}/tool_head_${tool.asString()}")
                                                layer("item/materialicons/${it.getName()}/tool_head_${tool.asString()}_overlay")
                                                layer("item/iconsets/handle_${tool.asString()}")
                                                layer("item/iconsets/handle_${tool.asString()}_overlay")
                                                register()
                                            }
                                            else -> model(Identifier(MODID, "${it.getName()}/${handleSet.getName()}/${tool.asString()}")) {
                                                itemSetup()
                                                layer("item/materialicons/${handleSet.getName()}/stick")
                                                layer("item/materialicons/${handleSet.getName()}/stick_overlay")
                                                layer("item/materialicons/${it.getName()}/tool_head_${tool.asString()}")
                                                layer("item/materialicons/${it.getName()}/tool_head_${tool.asString()}_overlay")
                                                register()
                                            }
                                        }
                                    }
                                } else {
                                    model(Identifier(MODID, "${it.getName()}/${tool.asString()}")) {
                                        itemSetup()
                                        layer("item/iconsets/${tool.asString()}")
                                        layer("item/iconsets/${tool.asString()}_overlay")
                                        register()
                                    }
                                }
                            }
                        }
                    } + "ms")
        }
    }.join()
    launch {
        builderMap.forEach { (id, modelBuilder) ->
            withContext(Dispatchers.Default) {
                modelCache[id] = modelBuilder.build()
            }
        }
    }.join()
    clientLogger.info("Cached ${modelCache.size} models")
}

/**
 * 从缓存读出对应的方块/物品的模型
 */
private fun loadOreModel(modelId: ModelIdentifier, context: ModelProviderContext): UnbakedModel? {
    /**
     * 确定矿属于那种石头
     */
    val rock =
            when {
                modelId.variant.isNullOrEmpty() -> Rocks.STONE.asString()
                modelId.variant == "inventory" -> (Rocks.values().asSequence().find { modelId.path.removeSuffix("_ore").endsWith(it.asString()) }
                        ?: Rocks.STONE).asString()
                else -> modelId.variant.split(',').first { it.startsWith("rock=") }.removePrefix("rock=")
            }
    val material = materialRegistry[Identifier(MODID, modelId.path.removePrefix("item/").removeSuffix("_ore"))]
    return modelCache[Identifier(MODID, "${material.textureSet?.toLowerCase()}/${rock}/ore")]
}

/**
 * 从缓存读出对应的方块/物品的自定义模型
 */
private fun loadCustomModel(resourceId: Identifier, context: ModelProviderContext): UnbakedModel? {
    if (resourceId.namespace != MODID)
        return null
    // TODO Unifying iconset finding
    val form = formRegistry.asSequence().findLast { resourceId.path.endsWith(it.asString()) }
    if (form != null) {
        if (form in STONE_FORMS) {
            if (resourceId is ModelIdentifier && resourceId.variant == "inventory") {
                    return modelCache[Identifier(resourceId.namespace, resourceId.path.removePrefix("block/").removePrefix("item/") + "_item")]
            }
            return modelCache[Identifier(resourceId.namespace, resourceId.path.removePrefix("block/"))]
        } else if (form.generateType == GenerateTypes.TOOL) {
            val tool = form as Tool
            val material = materialRegistry[Identifier(MODID, resourceId.path.removePrefix("item/").removeSuffix("_${tool.asString()}"))]
            if (material is JsonMaterial) {
                val toolInfo = material[Constants.TOOL_INFO, ToolInfo::class.java]
                return if (tool.hasHandleMaterial())
                    modelCache[Identifier(MODID, "${material.textureSet.toLowerCase()}/${toolInfo?.handleMaterial?.textureSet?.toLowerCase() ?: "none"}/${tool.asString()}")]
                else modelCache[Identifier(MODID, "${material.textureSet.toLowerCase()}/${tool.asString()}")]
            }
        } else {
            val material = materialRegistry[Identifier(MODID, resourceId.path.removePrefix("item/").removeSuffix("_${form.asString()}"))]
            if (material is JsonMaterial)
                return modelCache[Identifier(MODID, "${material.textureSet.toLowerCase()}/${form.asString()}")]
        }
    }
    return null
}
