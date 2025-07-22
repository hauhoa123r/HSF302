export class PackageResponse {
    constructor(id, name, description, price, durationDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
    }

    static fromJson(json) {
        if (!json) return null;
        return new PackageResponse(
                json.id,
                json.name,
                json.description,
                json.price,
                json.durationDays
        );
    }
}