package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import com.ubidots.Value;

public class UserIoTValues {
    private Value[] valuesDO, valuesBOD, valuesCOD, valuesNH3N, valuesSS, valuespH;

    public UserIoTValues(Value[] valuesDO, Value[] valuesBOD, Value[] valuesCOD, Value[] valuesNH3N, Value[] valuesSS, Value[] valuespH) {
        this.valuesDO = valuesDO;
        this.valuesBOD = valuesBOD;
        this.valuesCOD = valuesCOD;
        this.valuesNH3N = valuesNH3N;
        this.valuesSS = valuesSS;
        this.valuespH = valuespH;
    }

    public Value[] getValuesDO() {
        return valuesDO;
    }

    public Value[] getValuesBOD() {
        return valuesBOD;
    }

    public Value[] getValuesCOD() {
        return valuesCOD;
    }

    public Value[] getValuesNH3N() {
        return valuesNH3N;
    }

    public Value[] getValuesSS() {
        return valuesSS;
    }

    public Value[] getValuespH() {
        return valuespH;
    }
}
