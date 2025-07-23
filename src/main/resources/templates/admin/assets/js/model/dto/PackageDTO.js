export class PackageDTO {
    constructor(id, name, status, sortFieldName, sortDirection, packageCode, packageType, durationDays, price, description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.sortFieldName = sortFieldName;
        this.sortDirection = sortDirection;
        this.packageCode = packageCode;
        this.packageType = packageType;
        this.durationDays = durationDays;
        this.price = price;
        this.description = description;
    }
}