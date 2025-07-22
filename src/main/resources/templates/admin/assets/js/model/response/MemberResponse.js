import {MemberPackageResponse} from "/templates/admin/assets/js/model/response/MemberPackageResponse.js";
import {UserResponse} from "/templates/admin/assets/js/model/response/UserResponse.js";

export class MemberResponse {
    /**
     * @param {string} id
     * @param {UserResponse} userEntity
     * @param {MemberPackageResponse} memberPackageEntity
     * @param {boolean} isCheckedIn
     * @param {boolean} isCheckedOut
     * @param {boolean} isActive
     */
    constructor(id, userEntity, memberPackageEntity, isCheckedIn, isCheckedOut, isActive) {
        this.id = id;
        this.userEntity = userEntity;
        this.memberPackageEntity = memberPackageEntity;
        this.isCheckedIn = isCheckedIn !== undefined ? isCheckedIn : false;
        this.isCheckedOut = isCheckedOut !== undefined ? isCheckedOut : false;
        this.isActive = isActive;
    }

    static fromJson(json) {
        if (!json) return null;
        return new MemberResponse(
                json.id,
                UserResponse.fromJson(json.userEntity),
                MemberPackageResponse.fromJson(json.memberPackageEntity),
                json.isCheckedIn !== undefined ? json.isCheckedIn : false,
                json.isCheckedOut !== undefined ? json.isCheckedOut : false,
                json.isActive !== undefined ? json.isActive : true
        );
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map((json) => MemberResponse.fromJson(json));
    }

    setRenderStrategy(renderStrategy) {
        this.renderStrategy = renderStrategy;
        return this;
    }

    getStatus() {
        return this.isActive
                ? `<span class="badge badge-success">Đang hoạt động</span>`
                : `<span class="badge badge-danger">Không hoạt động</span>`;
    }

    getButton() {
        let html = "";

        // Nút xem chi tiết
        html += `<a class="dropdown-item view-detail" href="/admin/member/detail/${this.id}">
                <i class="fas fa-eye"></i> Xem chi tiết
            </a>`;

        // Nút chỉnh sửa
        html += `<a class="dropdown-item edit-member" href="/admin/member/update/${this.id}">
                <i class="fas fa-edit"></i> Chỉnh sửa
            </a>`;

        // Nút gửi thông báo
        html += `<a class="dropdown-item send-notification" href="/admin/member/notify/${this.userEntity.id}">
                <i class="fas fa-envelope"></i> Gửi thông báo
            </a>`;

        // Nút hủy gói tập (chỉ khi gói tập đang hoạt động)
        const canCancelPackage = this.memberPackageEntity && this.memberPackageEntity.isActive;
        html += `<a class="dropdown-item text-warning ${canCancelPackage ? "" : "disabled"}"
                ${canCancelPackage ? `onclick="return confirm('Bạn có chắc chắn muốn hủy gói tập cho thành viên với id là ${this.id} không?')"` : ""}
                href="/admin/member/package/cancel/${this.memberPackageEntity?.id}">
                <i class="fas fa-ban"></i> Hủy gói tập
            </a>`;

        // Nút đăng ký gói tập mới
        html += `<a class="dropdown-item text-success" href="/admin/member/package/register/${this.id}">
                <i class="fas fa-plus-circle"></i> Đăng ký gói tập mới
            </a>`;

        // Nút checkin (ẩn nếu đã check-in)
        if (!this.isCheckedIn) {
            html += `<a onclick="return confirm('Bạn có muốn check in cho thành viên với id là ${this.id} không?')" class="dropdown-item text-primary" href="/admin/member/checkin/${this.id}">
                    <i class="fas fa-sign-in-alt"></i> Check-in
                </a>`;
        }

        // Nút checkout (ẩn nếu chưa check-in hoặc đã check-out)
        if (this.isCheckedIn && !this.isCheckedOut) {
            html += `<a onclick="return confirm('Bạn có muốn check out cho thành viên với id là ${this.id} không?')" class="dropdown-item text-danger" href="/admin/member/checkout/${this.id}">
                    <i class="fas fa-sign-out-alt"></i> Check-out
                </a>`;
        }

        html += `<div class="dropdown-divider"></div>`;

        // Nút xóa thành viên
        html += `<a class="dropdown-item text-danger delete-member"
                href="/admin/member/delete/${this.id}"
                onclick="return confirm('Bạn có chắc chắn muốn xóa thành viên với id là ${this.id} không?')">
                <i class="fas fa-trash-alt"></i> Xóa
            </a>`;

        return html;
    }

    formatDate(dateString) {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toLocaleDateString("vi-VN", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit"
        });
    }

    render(...args) {
        if (this.renderStrategy) {
            return this.renderStrategy(this, ...args);
        }
        throw new Error("Render strategy is not set for MemberResponse");
    }
}

/**
 * Render the response list for admin member management.
 * @param {MemberResponse} memberResponse
 */
export function renderResponseListForAdmin(memberResponse) {
    return `
    <tr>
        <td>${memberResponse.id}</td>
        <td>${memberResponse.userEntity.fullName}</td>
        <td>${memberResponse.userEntity.email}</td>
        <td>${memberResponse.userEntity.phone}</td>
        <td>${
            memberResponse.memberPackageEntity?.packageEntity?.name || ""
    }</td>
        <td>${memberResponse.formatDate(
            memberResponse.memberPackageEntity?.startDate || ""
    )}</td>
        <td>${memberResponse.formatDate(
            memberResponse.memberPackageEntity?.endDate || ""
    )}</td>
        <td>${memberResponse.getStatus()}</td>
        <td>
            <div class="btn-group">
                <button aria-expanded="false" aria-haspopup="true"
                        class="btn btn-sm btn-outline-secondary dropdown-toggle"
                        data-toggle="dropdown" type="button">
                    <i class="fas fa-ellipsis-v"></i>
                </button>
                <div class="dropdown-menu dropdown-menu-right">${memberResponse.getButton()}</div>
            </div>
        </td> 
    </tr>
`;
}
