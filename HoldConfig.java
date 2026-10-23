package com.holdmyitem;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class HoldConfig {
    public static boolean enabled = true;
    public static boolean contextualAnimations = true;
    public static boolean hideWhileUsing = true;
    public static float armX = 0.0F;
    public static float armY = 0.0F;
    public static float armZ = 0.0F;

    public static void load(File file) {
        Configuration c = new Configuration(file);
        enabled = c.getBoolean("enabled", "general", true, "Render the arm while holding items");
        contextualAnimations = c.getBoolean("contextualAnimations", "general", true,
                "Extra motion while swimming, climbing a ladder or flying with elytra");
        hideWhileUsing = c.getBoolean("hideWhileUsing", "general", true,
                "Fall back to vanilla rendering while eating, drawing a bow or blocking");
        armX = c.getFloat("armX", "offsets", 0.0F, -2.0F, 2.0F, "Arm X offset (tune to taste)");
        armY = c.getFloat("armY", "offsets", 0.0F, -2.0F, 2.0F, "Arm Y offset");
        armZ = c.getFloat("armZ", "offsets", 0.0F, -2.0F, 2.0F, "Arm Z offset");
        if (c.hasChanged()) c.save();
    }
}
