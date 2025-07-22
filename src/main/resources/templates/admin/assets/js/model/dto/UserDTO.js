export class UserDTO {
    constructor(fullName, email, phone, gender, idCard, address, avatar, emergencyContact, occupation, hobby, note) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.idCard = idCard;
        this.address = address;
        this.avatar = avatar;
        this.emergencyContact = emergencyContact;
        this.occupation = occupation;
        this.hobby = hobby;
        this.note = note;
    }
}