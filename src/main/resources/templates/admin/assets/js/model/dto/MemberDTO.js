import {MemberPackageDTO} from "/templates/admin/assets/js/model/dto/MemberPackageDTO.js";
import {UserDTO} from "/templates/admin/assets/js/model/dto/UserDTO.js";

export class MemberDTO {
    /**
     * Constructor for MemberDTO
     * @param {UserDTO} userEntity
     * @param {MemberPackageDTO} memberPackageEntity
     * @param memberPackageEntityPackageEntityId
     * @param memberPackageEntityStartDate
     * @param sortFieldName
     * @param sortDirection
     */
    constructor(userEntity, memberPackageEntity, memberPackageEntityPackageEntityId, memberPackageEntityStartDate, sortFieldName, sortDirection) {
        this.userEntity = userEntity;
        this.memberPackageEntity = memberPackageEntity;
        this.memberPackageEntityPackageEntityId = memberPackageEntityPackageEntityId;
        this.memberPackageEntityStartDate = memberPackageEntityStartDate;
        this.sortFieldName = sortFieldName;
        this.sortDirection = sortDirection;
    }
}