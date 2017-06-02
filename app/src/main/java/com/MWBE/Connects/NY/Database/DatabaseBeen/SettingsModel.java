package com.MWBE.Connects.NY.Database.DatabaseBeen;

/**
 * Created by Fazal on 6/23/2016.
 */
public class SettingsModel {
    String GeographicCoverage;
    String ContractValue;
    String Certification;
    String Capabilities;

    public SettingsModel() {
    }

    public SettingsModel(String geographicCoverage, String contractValue, String certification, String capabilities) {
        GeographicCoverage = geographicCoverage;
        ContractValue = contractValue;
        Certification = certification;
        Capabilities = capabilities;
    }

    public String getGeographicCoverage() {
        return GeographicCoverage;
    }

    public void setGeographicCoverage(String geographicCoverage) {
        GeographicCoverage = geographicCoverage;
    }

    public String getContractValue() {
        return ContractValue;
    }

    public void setContractValue(String contractValue) {
        ContractValue = contractValue;
    }

    public String getCertification() {
        return Certification;
    }

    public void setCertification(String certification) {
        Certification = certification;
    }

    public String getCapabilities() {
        return Capabilities;
    }

    public void setCapabilities(String capabilities) {
        Capabilities = capabilities;
    }
}
