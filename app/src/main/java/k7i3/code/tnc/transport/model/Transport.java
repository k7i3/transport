package k7i3.code.tnc.transport.model;

import java.util.Date;

/**
 * Created by k7i3 on 16.09.15.
 */
//        [
//        {
//        "id": 87361271912,
//        "transportTypeCode": "НЕФАЗ 5299-30-42",
//        "transportTypeDescription": "НЕФАЗ 5299-30-42",
//        "regNum": "С970ВМ102",
//        "description": "УПАТП-3",
//        "groupCode": "16",
//        "groupDescription": "ПП УПАТП-3",
//        "garageNum": "С970ВМ102",
//        "codeM": "НЕФАЗ 5299-30-42",
//        "deviceId": 87360650603,
//        "deviceCode": "6861",
//        "phone": "9273554837",
//        "phone2": "УПАТП-3",
//        "primaryGroupName": "УПАТП-3 (bat3_1)",
//        "transmitDataToTN": 1,
//        "lastTransfer": "Jul 7, 2015 11:09:46 AM",
//        "codevirt": "00006861",
//        "lastData": "Sep 30, 2015 12:13:22 PM",
//        "softVersion": ">REV 07.627.028"
//        },
//        {
//        "id": 87361276737,
//        "transportTypeCode": "НЕФАЗ 5299-30-42",
//        "transportTypeDescription": "НЕФАЗ 5299-30-42",
//        "regNum": "С971ВМ102",
//        "description": "УПАТП-3",
//        "groupCode": "16",
//        "groupDescription": "ПП УПАТП-3",
//        "garageNum": "С971ВМ102",
//        "codeM": "НЕФАЗ 5299-30-42",
//        "deviceId": 87360652815,
//        "deviceCode": "863591024820247",
//        "phone": "89273554948",
//        "phone2": "УПАТП-3",
//        "primaryGroupName": "УПАТП-3 (bat3_1)",
//        "transmitDataToTN": 1,
//        "lastTransfer": "Sep 30, 2015 4:57:59 AM",
//        "codevirt": "00006841",
//        "lastData": "Sep 30, 2015 12:29:48 PM",
//        "softVersion": ">REV 07.628.028",
//        "hardVersion": ""
//        }
//        ]

public class Transport {
    private long id;
    private String transportTypeCode;
    private String transportTypeDescription;
    private String regNum;
    private String description;
    private String groupCode;
    private String groupDescription;
    private String garageNum;
    private String codeM;
    private long deviceId;
    private String deviceCode;
    private String phone;
    private String phone2;
    private String primaryGroupName;
    private String transmitDataToTN;
    private Date lastTransfer;
    private String codevirt;
    private String crewDescription;
    private Date lastData;
    private String softVersion;
    private String hardVersion;
    private String ownerName;

    @Override
    public String toString() {
        return "Transport{" +
                "id=" + id +
                ", regNum='" + regNum + '\'' +
                ", deviceId=" + deviceId +
                ", deviceCode='" + deviceCode + '\'' +
                ", phone='" + phone + '\'' +
                ", lastData=" + lastData +
                ", lastTransfer=" + lastTransfer +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransportTypeCode() {
        return transportTypeCode;
    }

    public void setTransportTypeCode(String transportTypeCode) {
        this.transportTypeCode = transportTypeCode;
    }

    public String getTransportTypeDescription() {
        return transportTypeDescription;
    }

    public void setTransportTypeDescription(String transportTypeDescription) {
        this.transportTypeDescription = transportTypeDescription;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGarageNum() {
        return garageNum;
    }

    public void setGarageNum(String garageNum) {
        this.garageNum = garageNum;
    }

    public String getCodeM() {
        return codeM;
    }

    public void setCodeM(String codeM) {
        this.codeM = codeM;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPrimaryGroupName() {
        return primaryGroupName;
    }

    public void setPrimaryGroupName(String primaryGroupName) {
        this.primaryGroupName = primaryGroupName;
    }

    public String getTransmitDataToTN() {
        return transmitDataToTN;
    }

    public void setTransmitDataToTN(String transmitDataToTN) {
        this.transmitDataToTN = transmitDataToTN;
    }

    public Date getLastTransfer() {
        return lastTransfer;
    }

    public void setLastTransfer(Date lastTransfer) {
        this.lastTransfer = lastTransfer;
    }

    public String getCodevirt() {
        return codevirt;
    }

    public void setCodevirt(String codevirt) {
        this.codevirt = codevirt;
    }

    public String getCrewDescription() {
        return crewDescription;
    }

    public void setCrewDescription(String crewDescription) {
        this.crewDescription = crewDescription;
    }

    public Date getLastData() {
        return lastData;
    }

    public void setLastData(Date lastData) {
        this.lastData = lastData;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getHardVersion() {
        return hardVersion;
    }

    public void setHardVersion(String hardVersion) {
        this.hardVersion = hardVersion;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
