import {PackageResponse} from "/templates/admin/assets/js/model/response/PackageResponse.js";

export class MemberPackageResponse {
    /**
     * Constructor for MemberPackageResponse
     * @param id
     * @param startDate
     * @param endDate
     * @param isActive
     * @param {PackageResponse} packageEntity
     */
    constructor(id, startDate, endDate, isActive, packageEntity) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.packageEntity = packageEntity;
    }

    static fromJson(json) {
        if (!json) return null;
        return new MemberPackageResponse(
                json.id,
                json.startDate,
                json.endDate,
                json.isActive,
                PackageResponse.fromJson(json.packageEntity)
        );
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map((json) => MemberPackageResponse.fromJson(json));
    }
}