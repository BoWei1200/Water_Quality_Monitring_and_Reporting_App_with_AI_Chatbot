package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import static java.lang.Math.*;

public class IoTWaterQualityManagement {
    private Double DO, BOD, COD, NH3N, SS, pH, WQI;

    public IoTWaterQualityManagement(Double DO, Double BOD, Double COD, Double NH3N, Double SS, Double pH) {
        this.DO = DO;
        this.BOD = BOD;
        this.COD = COD;
        this.NH3N = NH3N;
        this.SS = SS;
        this.pH = pH;
    }

    public void calculateWQI(){
        WQI = (0.22 * getSIDO()) + (0.19 * getSIBOD()) + (0.16 * getSICOD()) + (0.15 * getSIAN()) + (0.16 * getSISS()) + (0.12 * getSIpH());
    }

    public Double getSIDO(){
        return (getDO() >= 92.0) ? 100.0 : (getDO() > 9.0) ? (-0.395 + 0.030 * (pow(getDO(), 2)) - 0.00020 * (Math.pow(getDO(), 3))) : 0.0;
    }

    public Double getSIBOD(){
        return (getBOD() > 5) ? (108 * exp(-0.055 * getBOD())) - (0.1 * getBOD()) : 100.4 - (4.23 * getBOD());
    }

    public Double getSICOD(){
        return (getCOD() > 20) ? 103 * exp(-0.0157*getCOD()) - (0.04 * getCOD()) : -1.33 * getCOD() + 99.1;
    }

    public Double getSIAN(){
        return (getNH3N() >= 4) ? 0.0 :
                (getNH3N() > 0.3) ? (94 * exp(-0.573 * getNH3N())) - (5 * abs(getNH3N() - 2)) : 100.5 - (105 * getNH3N());
    }

    public Double getSISS(){
        return (getSS() >= 1000) ? 0.0 :
                (getSS() >= 101) ? (71 * exp(-0.0061 * getSS())) + (0.015 * getSS()) : (97.5 * exp(-0.00676 * getSS())) + (0.05 * getSS());
    }
    
    public Double getSIpH(){
        return (getpH() >= 8.75) ? 536 - (77.0 * getpH()) + (2.76 * pow(getpH(), 2)) :
                (getpH() >= 7.0) ? -181 + (82.4 * getpH()) - (6.05 * pow(getpH(), 2)) :
                (getpH() >= 5.5) ? -242 + (95.5 *getpH()) - (6.67 * pow(getpH(), 2)) : 17.02 - (17.2 * getpH()) + (5.02 * pow(getpH(), 2));
    }

    public Double getWQI(){
        return WQI;
    }

    public Double getDO() {
        return DO;
    }

    public Double getBOD() {
        return BOD;
    }

    public Double getCOD() {
        return COD;
    }

    public Double getNH3N() {
        return NH3N;
    }

    public Double getSS() {
        return SS;
    }

    public Double getpH() {
        return pH;
    }
}
