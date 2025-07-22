export class UserResponse {
    constructor(id, fullName, email, phone) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    static fromJson(json) {
        if (!json) return null;
        return new UserResponse(
                json.id,
                json.fullName,
                json.email,
                json.phone
        );
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map((json) => UserResponse.fromJson(json));
    }
}