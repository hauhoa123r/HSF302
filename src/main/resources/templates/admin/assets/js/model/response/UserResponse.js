export class UserResponse {
    constructor(id, fullName, email, phone, avatar) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar; // Optional, can be used for user profile picture
    }

    static fromJson(json) {
        if (!json) return null;
        return new UserResponse(
                json.id,
                json.fullName,
                json.email,
                json.phone,
                json.avatar || null // Default to null if avatar is not provided
        );
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map((json) => UserResponse.fromJson(json));
    }
}