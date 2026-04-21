package com.example.hrtool.service;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.enums.AbortState;
import com.example.hrtool.model.enums.ProcessStep;

import java.util.HashSet;
import java.util.Set;

public class ProcessStateResolver {


    public static Set<ProcessStep> resolve(EmployeeProfile e,
                                           EmployeeBlockStatusDto dto) {

        Set<ProcessStep> steps = new HashSet<>();

        // =====================================================
        // 1️⃣ Abort hat höchste Priorität
        // =====================================================
        if (e.getAbortState() == AbortState.DONE) {
            steps.add(ProcessStep.ABORTED);
            return steps;
        }

        // =====================================================
        // 2️⃣ Wenn IT komplett abgeschlossen → DONE
        // =====================================================
        if (e.isItCompleted()) {
            steps.add(ProcessStep.DONE);
            return steps;
        }

        // =====================================================
        // 3️⃣ Recruiting
        // =====================================================
        if (!dto.isPersonalDataFilled()
                || !dto.isCompanyDataFilled()) {

            steps.add(ProcessStep.RECRUITING);
            return steps; // Recruiting blockiert alles andere
        }

        // =====================================================
        // 4️⃣ Bereichsleiter bleibt aktiv,
        // solange einer seiner Blöcke fehlt
        // =====================================================
        boolean bereichsleiterNeedsWork =
                !dto.isQualificationDataFilled()
                        || !dto.isFinanceDataFilled()
                        || !dto.isInventoryDataFilled();

        if (bereichsleiterNeedsWork) {
            steps.add(ProcessStep.BEREICHSLEITER);
        }

        // =====================================================
        // 5️⃣ KL darf arbeiten wenn Finance fertig
        // =====================================================
        if (dto.isFinanceDataFilled()
                && !e.isKlCompleted()) {

            steps.add(ProcessStep.KAUFL);
        }

        // =====================================================
        // 6️⃣ HR darf arbeiten wenn Finance + Qualification fertig
        // =====================================================
        if (dto.isFinanceDataFilled()
                && dto.isQualificationDataFilled()
                && !e.isHrCompleted()) {

            steps.add(ProcessStep.HR);
        }

        // =====================================================
        // 7️⃣ IT nur wenn HR + KL abgeschlossen
        // =====================================================
        boolean itAllowed =
                e.isHrCompleted()
                        && e.isKlCompleted()
                        && dto.isPersonalDataFilled()
                        && dto.isInventoryDataFilled()
                        && dto.isSapNumberFilled();

        if (itAllowed && !e.isItCompleted()) {
            // IT darf NUR alleine aktiv sein
            steps.clear();
            steps.add(ProcessStep.IT);
            return steps;
        }

        return steps;
    }

}

