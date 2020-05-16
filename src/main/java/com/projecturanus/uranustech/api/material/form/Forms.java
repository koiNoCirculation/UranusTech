package com.projecturanus.uranustech.api.material.form;

import com.projecturanus.uranustech.api.material.generate.GenerateType;
import com.projecturanus.uranustech.api.material.generate.GenerateTypes;

public enum Forms implements Form {
    /**
     * 宝石
     */
    GEM,
    /**
     * 矿石
     */
    ORE(2, GenerateTypes.BLOCK),
    // Rock generation
    //石头，木头
    STONE(GenerateTypes.BLOCK),

    /**
     * 原来Chisel的材质
     */
    /**
     * 小块砖头
     */
    SMALL_BRICKS(GenerateTypes.BLOCK),
    /**
     * 小块瓷砖
     */
    SMALL_TILES(GenerateTypes.BLOCK),
    /**
     * 光滑砖头
     */
    SMOOTH(GenerateTypes.BLOCK),
    /**
     * 砖头
     */
    BRICKS(GenerateTypes.BLOCK),
    /**
     * 凿制砖头
     */
    BRICKS_CHISELED(GenerateTypes.BLOCK),
    /**
     * 有裂纹的砖头
     */
    BRICKS_CRACKED(GenerateTypes.BLOCK),
    /**
     * 生了苔的砖头
     */
    BRICKS_MOSSY(GenerateTypes.BLOCK),
    /**
     * 红石砖头
     */
    BRICKS_REDSTONE(GenerateTypes.BLOCK),
    /**
     * 坚固的砖头
     */
    BRICKS_REINFORCED(GenerateTypes.BLOCK),
    /**
     * 圆石
     */
    COBBLE(GenerateTypes.BLOCK),
    /**
     * 生了苔园石
     */
    COBBLE_MOSSY(GenerateTypes.BLOCK),
    /**
     * 中等大小的瓷砖
     */
    SQUARE_BRICKS(GenerateTypes.BLOCK),
    /**
     * 地砖(田字)
     */
    TILES(GenerateTypes.BLOCK),
    /**
     * 风车花纹(CW)
     */
    WINDMILL_TILES_A(GenerateTypes.BLOCK),
    /**
     * 风车花纹(CCW)
     */
    WINDMILL_TILES_B(GenerateTypes.BLOCK),
    /**
     * 木头
     */
    LOG(2, GenerateTypes.BLOCK),
    /**
     * 木板
     */
    PLANK(GenerateTypes.BLOCK),

    // Material form generation
    /**
     * 材料
     */
    /**
     * 管道
     */
    PIPE,
    /**
     * 精致线缆
     */
    WIRE_FINE(-8),
    /**
     * 箔
     */
    FOIL(-4),
    /**
     * 刻蚀用镜片
     */
    LENS(0.75),
    /**
     * 装甲
     */
    ARMOR,
    /**
     * 含杂粉末
     */
    DUST_IMPURE,
    /**
     * 弹簧
     */
    SPRING,
    /**
     * 纯净粉
     */
    DUST,
    /**
     * 热锭
     */
    INGOT_HOT,
    /**
     * 锭
     */
    INGOT,
    /**
     * 二重锭
     */
    INGOT_DOUBLE(2),
    /**
     * 三重锭
     */
    INGOT_TRIPLE(3),
    /**
     * 四重锭
     */
    INGOT_QUADRUPLE(4),
    /**
     * 五重锭
     */
    INGOT_QUINTUPLE(5),

    /**
     * 板
     */
    PLATE,
    /**
     * 宝石板
     */
    PLATE_GEM,
    /**
     * 小块板子
     */
    PLATE_TINY(-4),
    /**
     * 双层板
     */
    PLATE_DOUBLE(2),
    /**
     * 小块宝石板子
     */
    PLATE_GEM_TINY(-4),
    /**
     * 三层板
     */
    PLATE_TRIPLE(3),
    /**
     * 四层板
     */
    PLATE_QUADRUPLE(4),
    /**
     * 五层板
     */
    PLATE_QUINTUPLE(5),
    /**
     * 卷曲板
     */
    PLATE_CURVED,
    /**
     * 九层板
     */
    PLATE_DENSE(9),

    /**
     * 机器零件
     */
    /**
     * 滚珠(球)
     */
    ROUND(-9),
    /**
     * 环
     */
    RING(-4),
    /**
     * 螺栓
     */
    BOLT(-8),
    /**
     * 转子
     */
    ROTOR(4.25),
    /**
     * 车轮
     */
    CART_WHEELS,
    /**
     * 螺丝
     */
    SCREW(-9),
    /**
     * 小粒
     */
    NUGGET(-9),
    /**
     * 棒
     */
    STICK(-2),
    /**
     * 垃圾
     */
    SCRAP_GT(-9),
    /**
     * 熔融液体
     */
    MOLTEN(GenerateTypes.FLUID),
    /**
     * 气体　
     */
    GAS(GenerateTypes.FLUID),
    /**
     * 离子体
     */
    PLASMA(GenerateTypes.FLUID),
    /**
     * 其他
     */
    OTHER(GenerateTypes.OTHER);

    private double massMultiplier = 1;
    private String name = name().toLowerCase();
    private GenerateType generateType = GenerateTypes.ITEM;

    Forms() {
    }

    Forms(String name) {
        this.name = name;
    }

    Forms(double massMultiplier) {
        this.massMultiplier = massMultiplier;
    }

    Forms(GenerateType generateType) {
        this.generateType = generateType;
    }

    Forms(double massMultiplier, GenerateType generateType) {
        this.massMultiplier = massMultiplier;
        this.generateType = generateType;
    }

    @Override
    public GenerateType getGenerateType() {
        return generateType;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public double getAmountMultiplier() {
        return massMultiplier;
    }
}
