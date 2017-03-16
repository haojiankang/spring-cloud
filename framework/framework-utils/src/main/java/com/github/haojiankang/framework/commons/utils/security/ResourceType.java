package com.github.haojiankang.framework.commons.utils.security;

public enum ResourceType {
    /**
     * 根
     */
    Root(0),
    /**
     * 应用
     */
    Application(1),
    /**
     * 导航条
     */
    Navigation(2),
    /**
     * 菜单
     */
    Menu(3),
    /**
     * 按钮
     */
    Button(4),
    /**
     * 权限资源
     */
    Jurisdiction(5);
    private int index;
    private static String[] names;
    static {
        names = new String[] { "根", "应用", "导航条", "菜单", "按钮","权限资源" };
    }

    ResourceType(int index) {
        this.index = index;
    }

    public static ResourceType parser(int index) {
        return ResourceType.values()[index];
    }

    public String getName() {
        return names[index];
    }

    public int getIndex() {
        return index;
    }

}
