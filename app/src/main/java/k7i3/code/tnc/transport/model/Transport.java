package k7i3.code.tnc.transport.model;

import java.util.Date;

/**
 * Created by k7i3 on 16.09.15.
 */
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
}
