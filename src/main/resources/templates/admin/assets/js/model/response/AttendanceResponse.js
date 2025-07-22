import {MemberResponse} from "/templates/admin/assets/js/model/response/MemberResponse.js";
import {FormatUtils} from "/templates/shared/assets/js/utils/format-utils.js";

export class AttendanceResponse {
    /**
     * AttendanceResponse constructor
     * @param id
     * @param {MemberResponse} memberEntity
     * @param checkInTime
     * @param checkOutTime
     * @param method
     */
    constructor(id, memberEntity, checkInTime, checkOutTime, method) {
        this.id = id;
        this.memberEntity = memberEntity instanceof MemberResponse ? memberEntity : new MemberResponse(memberEntity);
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.method = method;
    }

    static fromJson(json) {
        const attendanceResponse = new AttendanceResponse();
        attendanceResponse.id = json.id;
        attendanceResponse.memberEntity = MemberResponse.fromJson(json.memberEntity);
        attendanceResponse.checkInTime = json.checkInTime;
        attendanceResponse.checkOutTime = json.checkOutTime;
        attendanceResponse.method = json.method;
        return attendanceResponse;
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map(json => AttendanceResponse.fromJson(json));
    }

    getDiffDate() {
        const start = new Date(this.checkInTime);
        if (!this.checkOutTime) {
            return "Chưa check out";
        }
        const end = new Date(this.checkOutTime);

        const diffMs = end - start;
        const diffMins = Math.floor(diffMs / (1000 * 60));
        const hours = Math.floor(diffMins / 60);
        const minutes = diffMins % 60;

        return `${hours} giờ ${minutes} phút`;
    }

    setRenderStrategy(renderStrategy) {
        this.renderStrategy = renderStrategy;
        return this;
    }

    render(...args) {
        if (this.renderStrategy) {
            return this.renderStrategy(this, ...args);
        }
        throw new Error("Render strategy is not set for AttendanceResponse");
    }
}

/**
 * Render function for AttendanceResponse in admin context
 * @param {AttendanceResponse} attendanceResponse
 * @return {string}
 */
export function renderAttendanceForAdmin(attendanceResponse) {
    return `
    <tr>
        <td>${attendanceResponse.memberEntity.id}</td>
        <td>${attendanceResponse.memberEntity.userEntity.fullName}</td>
        <td>${FormatUtils.formatTimeStamp(attendanceResponse.checkInTime)}</td>
        <td>${FormatUtils.formatTimeStamp(attendanceResponse.checkOutTime) && "Chưa check out"}</td>
        <td>${attendanceResponse.getDiffDate()}</td>
        <td>${attendanceResponse.method}</td>
    </tr>
                            `;
}