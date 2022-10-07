package ec.msplatform.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to manage Payload info
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class Payload {

    private String aud;

    private String sub;

    @JsonProperty("client_serial_number")
    @SerializedName("client_serial_number")
    private String clientSerialNumber;

    @JsonProperty("psd2_ssn")
    @SerializedName("psd2_ssn")
    private String psd2Ssn;

    @JsonProperty("psd2_role")
    @SerializedName("psd2_role")
    private String psd2Role;

}
