import {FormatUtils} from "/templates/shared/assets/js/utils/format-utils.js";

export class PackageResponse {
    constructor(id, name, description, packageType, price, durationDays, memberCount, status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
        this.packageType = packageType; // e.g., 'MONTHLY', 'YEARLY'
        this.memberCount = memberCount; // Number of members allowed in the package
        this.status = status; // e.g., 'ACTIVE', 'INACTIVE'
    }

    static fromJson(json) {
        if (!json) return null;
        return new PackageResponse(
                json.id,
                json.name,
                json.description,
                json.packageType,
                json.price,
                json.durationDays,
                json.memberCount,
                json.status
        );
    }

    static fromJsonArray(jsonArray) {
        if (!jsonArray) return [];
        return jsonArray.map(PackageResponse.fromJson);
    }

    setRenderStrategy(renderStrategy) {
        this.renderStrategy = renderStrategy;
        return this;
    }

    getStatus() {
        if (this.status === "ACTIVE") {
            return "<span class=\"badge badge-success\">Đang hoạt động</span>";
        } else if (this.status === "INACTIVE") {
            return "<span class=\"badge badge-secondary\">Không hoạt động</span>";
        }
        return "<span class=\"badge badge-danger\">Không xác định</span>";
    }

    render(...args) {
        if (this.renderStrategy) {
            return this.renderStrategy(this, ...args);
        }
        throw new Error("Render strategy is not set");
    }
}

/**
 * Render a package for admin view
 * @param {PackageResponse} packageResponse
 * @returns {string}
 */
export function renderPackageForAdmin(packageResponse) {
    return `
    <tr>
        <td>${packageResponse.id}</td>
        <td>${packageResponse.name}</td>
        <td>${packageResponse.packageType}</td>
        <td>${packageResponse.durationDays} ngày</td>
        <td>${FormatUtils.formatPrice(packageResponse.price)}</td>
        <td>${packageResponse.memberCount}</td>
        <td>${packageResponse.getStatus()}</td>
        <td>
            <div class="btn-group">
                <button aria-expanded="false" aria-haspopup="true"
                        class="btn btn-sm btn-outline-secondary dropdown-toggle"
                        data-toggle="dropdown" type="button">
                    <i class="fas fa-ellipsis-v"></i>
                </button>
                <div class="dropdown-menu dropdown-menu-right">
                    <a class="dropdown-item" href="/admin/package/detail/${packageResponse.id}"><i class="fas fa-eye"></i>
                        Xem chi tiết</a>
                    <a class="dropdown-item" href="/admin/package/edit/${packageResponse.id}"><i class="fas fa-edit"></i>
                        Chỉnh sửa</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item text-danger" href="/admin/package/delete/${packageResponse.id}" onclick="return confirm('Bạn có đồng ý xóa gói tập với id là ${packageResponse.id} không?')"><i
                            class="fas fa-trash-alt"></i> Xóa</a>
                </div>
            </div>
        </td>
    </tr>
    `;
}
