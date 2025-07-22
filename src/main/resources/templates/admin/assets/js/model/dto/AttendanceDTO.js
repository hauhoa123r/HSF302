import {MemberDTO} from "/templates/admin/assets/js/model/dto/MemberDTO.js";

export class AttendanceDTO {
    /**
     * AttendanceDTO constructor
     * @param id
     * @param {MemberDTO} MemberEntity
     * @param checkInTime
     * @param checkOutTime
     * @param method
     */
    constructor(id, MemberEntity, checkInTime, checkOutTime, method) {
        this.id = id;
        this.memberEntity = MemberEntity instanceof MemberDTO ? MemberEntity : new MemberDTO();
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.method = method;
    }
}