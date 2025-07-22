package com.web.model.dto;

import com.web.enums.operation.SortDirection;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    @Valid
    private UserDTO userEntity;
    private String occupation;
    private String hobby;
    private String note;
    private String memberPackageEntityPackageEntityId;
    private Date memberPackageEntityStartDate;
    private String sortFieldName;
    private SortDirection sortDirection;
}
