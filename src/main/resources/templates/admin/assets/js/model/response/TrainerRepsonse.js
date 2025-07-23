import {UserResponse} from "/templates/admin/assets/js/model/response/UserResponse.js";

export class TrainerResponse {
    /**
     * Constructor for TrainerResponse
     * @param id
     * @param {UserResponse} userEntity
     * @param specialization
     * @param status
     * @param memberCount
     */
    constructor(id, userEntity, specialization, status, memberCount) {
        this.id = id;
        this.userEntity = userEntity; // Instance of UserResponse
        this.specialization = specialization; // String
        this.status = status; // String
        this.memberCount = memberCount; // Number
    }

    static fromJson(json) {
        return new TrainerResponse(
                json.id,
                UserResponse.fromJson(json.userEntity),
                json.specialization,
                json.status,
                json.memberCount
        );
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map(json => TrainerResponse.fromJson(json));
    }

    getStatus() {
        if (this.status === "ACTIVE") {
            return `<span class="badge badge-success">Đang làm việc</span>`;
        }
        if (this.status === "INACTIVE") {
            return `<span class="badge badge-secondary">Ngừng làm việc</span>`;
        }
        if (this.status === "SUSPENDED") {
            return `<span class="badge badge-danger">Bị đình chỉ</span>`;
        }
    }

    setRenderStrategy(renderStrategy) {
        this.renderStrategy = renderStrategy;
        return this;
    }

    render(...args) {
        if (this.renderStrategy) {
            return this.renderStrategy(this, ...args);
        }
        throw new Error("Render strategy is not set for TrainerResponse");
    }
}

/**
 * Render function for TrainerResponse in admin context
 * @param {TrainerResponse} trainerResponse
 * @returns {string}
 */
export function renderTrainerForAdmin(trainerResponse) {
    return `
        <tr>
            <td>${trainerResponse.id}</td>
            <td><img class="rounded-circle" height="40" src="${trainerResponse.userEntity.avatar}"
                     width="40"></td>
            <td>${trainerResponse.userEntity.fullName}</td>
            <td>${trainerResponse.specialization}</td>
            <td>${trainerResponse.userEntity.phone}</td>
            <td>${trainerResponse.userEntity.email}</td>
            <td>${trainerResponse.getStatus()}</td>
            <td>15</td>
            <td>
                <div class="btn-group">
                    <button aria-expanded="false"
                            aria-haspopup="true"
                            class="btn btn-sm btn-outline-secondary dropdown-toggle"
                            data-toggle="dropdown"
                            type="button">
                        <i class="fas fa-ellipsis-v"></i>
                    </button>
                    <div class="dropdown-menu dropdown-menu-right">
                        <a class="dropdown-item" href="/admin/trainer/detail/${trainerResponse.id}"><i class="fas fa-eye"></i>
                            Xem chi tiết</a>
                        <a class="dropdown-item" href="/admin/trainer/edit/${trainerResponse.id}"><i
                                class="fas fa-edit"></i> Chỉnh sửa</a>
                        <a class="dropdown-item" href="/admin/trainer/schedule/${trainerResponse.id}"><i
                                class="fas fa-calendar-alt"></i> Xem lịch làm
                            việc</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item text-danger" href="/admin/trainer/delete/${trainerResponse.id}" onclick="return confirm('Bạn có đồng ý xóa huấn luyện viên với id là ${trainerResponse.id} không?')"><i
                                class="fas fa-trash-alt"></i> Xóa</a>
                    </div>
                </div>
            </td>
        </tr> 
    `;
}