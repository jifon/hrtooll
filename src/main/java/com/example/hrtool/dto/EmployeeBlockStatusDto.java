package com.example.hrtool.dto;

import com.example.hrtool.model.enums.AbortState;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class EmployeeBlockStatusDto {
    private Long id;
    private String fullName;
    private boolean personalDataFilled;
    private boolean companyDataFilled;
    private boolean qualificationDataFilled;
    private boolean financeDataFilled;
    private boolean inventoryDataFilled;
    private boolean inventoryNrFilled;
    private boolean accessDataFilled;
    private boolean accessFinalCompleted;
    private boolean sapNumberFilled;
    private Set<String> currentStatuses = new HashSet<>();
    private boolean hrCompleted;
    private boolean klCompleted;
    private boolean itCompleted;

    private boolean assignedToHr;

    private boolean assignedToKaufl;

    private boolean assignedToIt;

    private boolean hrAccessFilled;
    private boolean klAccessFilled;
    private boolean itAccessFilled;

    private AbortState abortState;

    private boolean canStartAbort;

    private boolean canConfirmAbort;

    private Set<String> abortRequiredRoles;
    private Set<String> abortConfirmedRoles;

    public boolean isHrAccessFilled() {
        return hrAccessFilled;
    }

    public void setHrAccessFilled(boolean hrAccessFilled) {
        this.hrAccessFilled = hrAccessFilled;
    }

    public boolean isKlAccessFilled() {
        return klAccessFilled;
    }

    public void setKlAccessFilled(boolean klAccessFilled) {
        this.klAccessFilled = klAccessFilled;
    }

    public boolean isItAccessFilled() {
        return itAccessFilled;
    }

    public void setItAccessFilled(boolean itAccessFilled) {
        this.itAccessFilled = itAccessFilled;
    }

    public boolean isInventoryNrFilled() {
        return inventoryNrFilled;
    }

    public void setInventoryNrFilled(boolean inventoryNrFilled) {
        this.inventoryNrFilled = inventoryNrFilled;
    }






}
