export class NotificationDTO {
    constructor(id, title, content, type, sendAt, isRead, userEntityId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.sendAt = sendAt;
        this.isRead = isRead !== undefined ? isRead : false;
        this.userEntityId = userEntityId;
    }
}