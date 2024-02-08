package com.example.anniee.customer;
import com.example.anniee.utilities.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")

public class Customer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private Integer idNumber;
    private Long address;




    //    Operational Audit
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String postedBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Character postedFlag ='N';
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime postedTime;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String verifiedBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Character verifiedFlag ='N';
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime verifiedTime;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Character deletedFlag ='N';
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime deletedTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String modifiedBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Character modifiedFlag ='N';
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modifiedTime;



    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String hrApprovedBy=null;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime hrApprovedOn = null;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Character hrApprovedFlag = 'N';
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status = StatusEnum.PENDING.toString();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String remarks = "-";
}


