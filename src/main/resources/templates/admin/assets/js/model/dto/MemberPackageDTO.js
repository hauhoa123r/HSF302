export class MemberPackageDTO {
    constructor(memberEntityId, packageEntityId, promotionEntityId, startDate, method) {
        this.memberId = memberEntityId;
        this.packageId = packageEntityId;
        this.promotionEntityId = promotionEntityId;
        this.startDate = startDate;
        this.method = method;
    }
}