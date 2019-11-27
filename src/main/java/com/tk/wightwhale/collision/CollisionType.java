package com.tk.wightwhale.collision;

/**
 * Pre-defined behaviour collision types
 */
public enum CollisionType {
    BLOCK,
    BOUNCE_BACK,
    LOAD_AREA_TRIGGER, //loads an area based on the direction of the user walking
    LOAD_LEVEL_TRIGGER, //loads a specific level using code (level #)
    EVENT, //indicates a custom trigger using the code and statEffect integers
    IGNORE
}
