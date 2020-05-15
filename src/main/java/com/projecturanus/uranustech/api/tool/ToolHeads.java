package com.projecturanus.uranustech.api.tool;

import com.projecturanus.uranustech.api.material.generate.GenerateType;
import com.projecturanus.uranustech.api.material.generate.GenerateTypes;

public enum ToolHeads implements ToolHead {
    /**
     * 箭头
     */
    TOOL_HEAD_ARROW,
    /**
     * 斧头
     */
    TOOL_HEAD_AXE,
    /**
     * 双斧头
     */
    TOOL_HEAD_AXE_DOUBLE,
    /**
     * 园锯片
     */
    TOOL_HEAD_BUZZ_SAW,
    /**
     * 链锯片
     */
    TOOL_HEAD_CHAINSAW,
    /**
     * 凿头
     */
    TOOL_HEAD_CHISEL,
    /**
     *
     */
    TOOL_HEAD_CONSTRUCTION_PICKAXE,
    /**
     *
     */
    TOOL_HEAD_DRILL,
    /**
     * 锉头
     */
    TOOL_HEAD_FILE,
    /**
     * 锤头
     */
    TOOL_HEAD_HAMMER,
    /**
     * 锄头　
     */
    TOOL_HEAD_HOE,
    /**
     * 镐头
     */
    TOOL_HEAD_PICKAXE,
    /**
     *
     */
    TOOL_HEAD_PLOW,
    /**
     * 锯头
     */
    TOOL_HEAD_SAW,
    /**
     * 螺丝刀头
     */
    TOOL_HEAD_SCREWDRIVER,
    TOOL_HEAD_SENSE,
    /**
     * 铲头
     */
    TOOL_HEAD_SHOVEL,
    TOOL_HEAD_SPADE,
    /**
     * 剑刃
     */
    TOOL_HEAD_SWORD,
    TOOL_HEAD_UNIVERSAL_SPADE,
    /**
     * 扳手头
     */
    TOOL_HEAD_WRENCH;
    private double massMultiplier = 1;
    private String name = name().toLowerCase();
    private Tool tool;

    ToolHeads() {
        this.tool = Tools.valueOf(name().replaceFirst("TOOL_HEAD_", ""));
        this.massMultiplier = this.tool.getAmountMultiplier();
    }

    ToolHeads(Tool tool) {
        this.tool = tool;
        this.massMultiplier = this.tool.getAmountMultiplier();
    }

    ToolHeads(String name) {
        this.name = name;
    }

    ToolHeads(Tool tool, double massMultiplier) {

        this.massMultiplier = massMultiplier;
    }

    @Override
    public GenerateType getGenerateType() {
        return GenerateTypes.ITEM;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public double getAmountMultiplier() {
        return massMultiplier;
    }

    @Override
    public Tool getTool() {
        return tool;
    }
}
