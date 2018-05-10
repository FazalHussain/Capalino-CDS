package com.MWBE.Connects.NY.Database.DatabaseBeen;

/**
 * Created by Fazal on 9/26/2016.
 */
public class ProcMaster {
    int ProcurementID;
    String ProcurementEPIN;
    String ProcurementSource;
    String ProcurementAgencyID;
    String ProcurementTypeIDP;
    String ProcurementTitle;
    String ProcurementShortDescription;
    String ProcurementLongDescription;
    String ProcurementProposalDeadline;
    String ProcurementPreConferenceDate;
    String ProcurementQuestionDeadline;
    String ProcurementAgencyURL;
    String ProcurementDocument1URL;
    String ProcurementDocument2URL;
    String ProcurementDocument3URL;
    String ProcurementDocument4URL;
    String ProcurementDocument5URL;
    String ProcurementAddedDate;
    String ProcurementContractValueID;
    String Status;
    String LASTEDITEDUSERNAME;
    String PDFPath;
    String RecordUpdatedDate;
    String Action;

    public ProcMaster(int procurementID, String procurementEPIN, String procurementSource, String procurementAgencyID,
                      String procurementTypeIDP, String procurementTitle, String procurementShortDescription,
                      String procurementLongDescription, String procurementProposalDeadline, String procurementPreConferenceDate,
                      String procurementQuestionDeadline, String procurementAgencyURL, String procurementDocument1URL,
                      String procurementDocument2URL, String procurementDocument3URL, String procurementDocument4URL,
                      String procurementDocument5URL, String procurementAddedDate, String procurementContractValueID,
                      String status, String LASTEDITEDUSERNAME, String PDFPath, String RecordUpdatedDate, String Action) {
        ProcurementID = procurementID;
        ProcurementEPIN = procurementEPIN;
        ProcurementSource = procurementSource;
        ProcurementAgencyID = procurementAgencyID;
        ProcurementTypeIDP = procurementTypeIDP;
        ProcurementTitle = procurementTitle;
        ProcurementShortDescription = procurementShortDescription;
        ProcurementLongDescription = procurementLongDescription;
        ProcurementProposalDeadline = procurementProposalDeadline;
        ProcurementPreConferenceDate = procurementPreConferenceDate;
        ProcurementQuestionDeadline = procurementQuestionDeadline;
        ProcurementAgencyURL = procurementAgencyURL;
        ProcurementDocument1URL = procurementDocument1URL;
        ProcurementDocument2URL = procurementDocument2URL;
        ProcurementDocument3URL = procurementDocument3URL;
        ProcurementDocument4URL = procurementDocument4URL;
        ProcurementDocument5URL = procurementDocument5URL;
        ProcurementAddedDate = procurementAddedDate;
        ProcurementContractValueID = procurementContractValueID;
        Status = status;
        this.LASTEDITEDUSERNAME = LASTEDITEDUSERNAME;
        this.PDFPath = PDFPath;
        this.RecordUpdatedDate = RecordUpdatedDate;
        this.Action = Action;
    }

    public int getProcurementID() {
        return ProcurementID;
    }

    public void setProcurementID(int procurementID) {
        ProcurementID = procurementID;
    }

    public String getProcurementEPIN() {
        return ProcurementEPIN;
    }

    public void setProcurementEPIN(String procurementEPIN) {
        ProcurementEPIN = procurementEPIN;
    }

    public String getProcurementSource() {
        return ProcurementSource;
    }

    public void setProcurementSource(String procurementSource) {
        ProcurementSource = procurementSource;
    }

    public String getProcurementAgencyID() {
        return ProcurementAgencyID;
    }

    public void setProcurementAgencyID(String procurementAgencyID) {
        ProcurementAgencyID = procurementAgencyID;
    }

    public String getProcurementTypeIDP() {
        return ProcurementTypeIDP;
    }

    public void setProcurementTypeIDP(String procurementTypeIDP) {
        ProcurementTypeIDP = procurementTypeIDP;
    }

    public String getProcurementTitle() {
        return ProcurementTitle;
    }

    public void setProcurementTitle(String procurementTitle) {
        ProcurementTitle = procurementTitle;
    }

    public String getProcurementShortDescription() {
        return ProcurementShortDescription;
    }

    public void setProcurementShortDescription(String procurementShortDescription) {
        ProcurementShortDescription = procurementShortDescription;
    }

    public String getProcurementLongDescription() {
        return ProcurementLongDescription;
    }

    public void setProcurementLongDescription(String procurementLongDescription) {
        ProcurementLongDescription = procurementLongDescription;
    }

    public String getProcurementProposalDeadline() {
        return ProcurementProposalDeadline;
    }

    public void setProcurementProposalDeadline(String procurementProposalDeadline) {
        ProcurementProposalDeadline = procurementProposalDeadline;
    }

    public String getProcurementPreConferenceDate() {
        return ProcurementPreConferenceDate;
    }

    public void setProcurementPreConferenceDate(String procurementPreConferenceDate) {
        ProcurementPreConferenceDate = procurementPreConferenceDate;
    }

    public String getProcurementQuestionDeadline() {
        return ProcurementQuestionDeadline;
    }

    public void setProcurementQuestionDeadline(String procurementQuestionDeadline) {
        ProcurementQuestionDeadline = procurementQuestionDeadline;
    }

    public String getProcurementAgencyURL() {
        return ProcurementAgencyURL;
    }

    public void setProcurementAgencyURL(String procurementAgencyURL) {
        ProcurementAgencyURL = procurementAgencyURL;
    }

    public String getProcurementDocument1URL() {
        return ProcurementDocument1URL;
    }

    public void setProcurementDocument1URL(String procurementDocument1URL) {
        ProcurementDocument1URL = procurementDocument1URL;
    }

    public String getProcurementDocument2URL() {
        return ProcurementDocument2URL;
    }

    public void setProcurementDocument2URL(String procurementDocument2URL) {
        ProcurementDocument2URL = procurementDocument2URL;
    }

    public String getProcurementDocument3URL() {
        return ProcurementDocument3URL;
    }

    public void setProcurementDocument3URL(String procurementDocument3URL) {
        ProcurementDocument3URL = procurementDocument3URL;
    }

    public String getProcurementDocument4URL() {
        return ProcurementDocument4URL;
    }

    public void setProcurementDocument4URL(String procurementDocument4URL) {
        ProcurementDocument4URL = procurementDocument4URL;
    }

    public String getProcurementDocument5URL() {
        return ProcurementDocument5URL;
    }

    public void setProcurementDocument5URL(String procurementDocument5URL) {
        ProcurementDocument5URL = procurementDocument5URL;
    }

    public String getProcurementAddedDate() {
        return ProcurementAddedDate;
    }

    public void setProcurementAddedDate(String procurementAddedDate) {
        ProcurementAddedDate = procurementAddedDate;
    }

    public String getProcurementContractValueID() {
        return ProcurementContractValueID;
    }

    public void setProcurementContractValueID(String procurementContractValueID) {
        ProcurementContractValueID = procurementContractValueID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLASTEDITEDUSERNAME() {
        return LASTEDITEDUSERNAME;
    }

    public void setLASTEDITEDUSERNAME(String LASTEDITEDUSERNAME) {
        this.LASTEDITEDUSERNAME = LASTEDITEDUSERNAME;
    }

    public String getPDFPath() {
        return PDFPath;
    }

    public void setPDFPath(String PDFPath) {
        this.PDFPath = PDFPath;
    }

    public String getRecordUpdatedDate() {
        return RecordUpdatedDate;
    }

    public String getAction() {
        return Action;
    }
}
