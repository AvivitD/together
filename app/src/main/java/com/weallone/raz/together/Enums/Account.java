package com.weallone.raz.together.Enums;

/**
 * Represents the entity of user.
 */
public enum Account {
    CHILD,PSYCHOLOGIST,MANAGER,DEVELOPER;

    /**
     * Enum to String method
     * @return string representation
     */
    @Override
    public String toString(){
        switch(this){
            case CHILD: return "Child";
            case PSYCHOLOGIST: return "Psychologist";
            case MANAGER: return "Manager";
            case DEVELOPER: return "Developer";
            default: return "";
        }
    }
}
