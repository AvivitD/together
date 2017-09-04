package com.weallone.raz.together.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shift collection - in calender fragment
 */
public class ShiftCollection {
    private List<Shift> shifts;

    /**
     * Construcotr
     */
    public ShiftCollection() {
        shifts = new ArrayList<>();
    }

    /**
     * toString() override
     * @return
     */
    @Override
    public String toString() {
        return "ShiftCollection{" +
                "shifts=" + shifts.toString() +
                '}';
    }

    /**
     * Adding a shift.
     * Checking if it is not exists first
     * @param shift - to add
     * @return the collection
     */
    public ShiftCollection add(Shift shift) {
        //add if not exists:
        Boolean exists = false;
        for(Shift s: shifts){
            if(s.getId().equals(shift.getId())){
                exists = true;
            }
        }
        if(!exists){
            shifts.add(shift);
        }
        return this;
    }

    /**
     * get method
     * @return
     */
    public List<Shift> getShifts() {
        return shifts;
    }
}