/**
 * Toast.js - Thư viện hiển thị thông báo toast đơn giản có thanh progress và hỗ trợ gợi ý options cho tất cả hàm
 */

/**
 * @typedef {Object} ToastOptions
 * @property {"primary"|"success"|"warning"|"danger"|"info"} [type] - Loại toast
 * @property {string} [message] - Nội dung thông báo
 * @property {number} [duration] - Thời gian hiển thị (ms)
 * @property {"top-right"|"top-left"|"bottom-right"|"bottom-left"} [position] - Vị trí hiển thị
 * @property {boolean} [dismissible] - Có thể đóng được không
 * @property {boolean} [icon] - Hiển thị icon hay không
 * @property {boolean} [solid] - Kiểu solid hay không
 * @property {boolean} [rounded] - Bo góc hay không
 * @property {string|null} [link] - Liên kết (nếu có)
 * @property {string|null} [heading] - Tiêu đề (nếu có)
 * @property {string|null} [additionalText] - Văn bản bổ sung (nếu có)
 * @property {boolean} [progress] - Có hiện progress bar không
 * @property {function} [onClose] - Hàm callback khi toast đóng
 */
class Toast {
    constructor() {
        this.injectStyles();

        this.defaultOptions = {
            type: "primary",
            message: "Đây là thông báo!",
            duration: 5000,
            position: "top-right",
            dismissible: true,
            icon: false,
            solid: false,
            rounded: true,
            link: null,
            heading: null,
            additionalText: null,
            progress: false,
            onClose: null // mới thêm
        };

        this.icons = {
            primary: `<svg class="bi flex-shrink-0 me-2" width="24" height="24"><use xlink:href="#info-fill" /></svg>`,
            success: `<svg class="bi flex-shrink-0 me-2" width="24" height="24"><use xlink:href="#check-circle-fill" /></svg>`,
            warning: `<svg class="bi flex-shrink-0 me-2" width="24" height="24"><use xlink:href="#exclamation-triangle-fill" /></svg>`,
            danger: `<svg class="bi flex-shrink-0 me-2" width="24" height="24"><use xlink:href="#exclamation-triangle-fill" /></svg>`,
            info: `<svg class="bi flex-shrink-0 me-2" width="24" height="24"><use xlink:href="#info-fill" /></svg>`
        };

        this.initIcons();
        this.createContainer();
    }

    injectStyles() {
        const css = `
      @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
      }
      @keyframes slideInLeft {
        from { transform: translateX(-100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
      }
      .toast-container-top-right, .toast-container-top-left,
      .toast-container-bottom-right, .toast-container-bottom-left {
        position: fixed;
        z-index: 9999;
        max-width: 350px;
      }
      .toast-container-top-right { top: 20px; right: 20px; }
      .toast-container-top-left { top: 20px; left: 20px; }
      .toast-container-bottom-right { bottom: 20px; right: 20px; }
      .toast-container-bottom-left { bottom: 20px; left: 20px; }
      .alert {
        margin-bottom: 15px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        transition: all 0.3s ease-in-out;
        opacity: 0;
        position: relative;
        padding: 0.75rem 1.25rem;
        border-radius: 0.375rem;
        font-size: 1rem;
        line-height: 1.5;
      }
      .alert .bi { margin-right: 0.75rem; }
      .toast-container-top-right .alert.show { opacity: 1; animation: slideInRight 0.5s; }
      .toast-container-top-left .alert.show { opacity: 1; animation: slideInLeft 0.5s; }
      .toast-container-bottom-right .alert.show { opacity: 1; animation: slideInRight 0.5s; }
      .toast-container-bottom-left .alert.show { opacity: 1; animation: slideInLeft 0.5s; }
      .alert.fade { transition: opacity 0.3s linear, transform 0.5s; }
      .toast-container-top-right .alert.fade:not(.show),
      .toast-container-bottom-right .alert.fade:not(.show) {
        opacity: 0;
        transform: translateX(100%);
      }
      .toast-container-top-left .alert.fade:not(.show),
      .toast-container-bottom-left .alert.fade:not(.show) {
        opacity: 0;
        transform: translateX(-100%);
      }
      .alert-left { border-left: 5px solid; }
      .alert-right { border-right: 5px solid; }
      .alert-top { border-top: 5px solid; }
      .alert-bottom { border-bottom: 5px solid; }
      .alert-solid { color: white; }
      .alert-solid.alert-primary { background-color: #0d6efd; border-color: #0d6efd; }
      .alert-solid.alert-success { background-color: #198754; border-color: #198754; }
      .alert-solid.alert-warning { background-color: #ffc107; border-color: #ffc107; }
      .alert-solid.alert-danger { background-color: #dc3545; border-color: #dc3545; }
      .alert-solid.alert-info { background-color: #0dcaf0; border-color: #0dcaf0; }
      .alert-primary { color: #084298; background-color: #cfe2ff; border: 1px solid #b6d4fe; }
      .alert-success { color: #0f5132; background-color: #d1e7dd; border: 1px solid #badbcc; }
      .alert-warning { color: #664d03; background-color: #fff3cd; border: 1px solid #ffecb5; }
      .alert-danger { color: #842029; background-color: #f8d7da; border: 1px solid #f5c2c7; }
      .alert-info { color: #055160; background-color: #cff4fc; border: 1px solid #b6effb; }
      .rounded { border-radius: 0.375rem !important; }
      .rounded-0 { border-radius: 0 !important; }
      .d-flex { display: flex !important; }
      .align-items-center { align-items: center !important; }
      .alert-link { font-weight: 700; color: inherit; text-decoration: underline; }
      .alert-link:hover, .alert-link:focus { color: #0a58ca; }
      .btn-close {
        box-sizing: content-box;
        width: 1em;
        height: 1em;
        padding: 0.25em 0.25em;
        color: #000;
        background: transparent url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 16 16'%3e%3cpath stroke='black' stroke-linecap='round' stroke-width='2' d='M2 2l12 12M14 2L2 14'/%3e%3c/svg%3e") center/1em auto no-repeat;
        border: 0;
        border-radius: 0.375rem;
        opacity: 0.5;
        cursor: pointer;
        transition: opacity 0.15s linear;
        top: 0.5rem;
        right: 0.75rem;
      }
      .btn-close:hover { opacity: 0.75; }
      .alert.d-flex .btn-close {
          margin: auto;
          margin-right: 0;
        }
      .toast-progress {
        position: absolute;
        left: 0; bottom: 0;
        height: 4px;
        background: #0d6efd;
        width: 100%;
        transition: width linear;
        border-radius: 0 0 0.375rem 0.375rem;
        opacity: 0.7;
        pointer-events: none;
      }
      .alert-success .toast-progress { background: #198754; }
      .alert-danger .toast-progress { background: #dc3545; }
      .alert-warning .toast-progress { background: #ffc107; }
      .alert-info .toast-progress { background: #0dcaf0; }
    `;

        const style = document.createElement("style");
        style.id = "toast-js-styles";
        style.textContent = css;

        if (!document.getElementById("toast-js-styles")) {
            document.head.appendChild(style);
        }
    }

    initIcons() {
        if (!document.getElementById("toast-icons")) {
            const iconsSvg = document.createElement("div");
            iconsSvg.style.display = "none";
            iconsSvg.id = "toast-icons";
            iconsSvg.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
          <symbol id="check-circle-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z" />
          </symbol>
          <symbol id="info-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z" />
          </symbol>
          <symbol id="exclamation-triangle-fill" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z" />
          </symbol>
        </svg>
      `;
            document.body.appendChild(iconsSvg);
        }
    }

    createContainer() {
        const positions = [
            "top-right",
            "top-left",
            "bottom-right",
            "bottom-left"
        ];
        positions.forEach((position) => {
            if (!document.querySelector(`.toast-container-${position}`)) {
                const container = document.createElement("div");
                container.className = `toast-container-${position}`;
                document.body.appendChild(container);
            }
        });
    }

    /**
     * Hiển thị một toast.
     * @param {ToastOptions} options
     * @returns {HTMLElement}
     */
    show(options = {}) {
        const settings = {...this.defaultOptions, ...options};

        // Tạo toast element
        const toastElement = document.createElement("div");

        // Xác định vị trí container
        let positionClass = "top-right";
        if (settings.position === "top-left") positionClass = "top-left";
        if (settings.position === "bottom-right") positionClass = "bottom-right";
        if (settings.position === "bottom-left") positionClass = "bottom-left";

        // Các lớp CSS cho vị trí
        let positionStyleClass = "";
        if (settings.position.includes("top")) positionStyleClass = "alert-top";
        else if (settings.position.includes("bottom")) positionStyleClass = "alert-bottom";
        if (settings.position.includes("left")) positionStyleClass = "alert-left";
        else if (settings.position.includes("right")) positionStyleClass = "alert-right";

        const classes = ["alert", `alert-${settings.type}`, "alert-dismissible", "fade", "show"];
        if (!settings.rounded) classes.push("rounded-0");
        if (settings.solid) classes.push("alert-solid");
        if (positionStyleClass) classes.push(positionStyleClass);
        if (settings.icon) classes.push("d-flex", "align-items-center");

        toastElement.className = classes.join(" ");
        toastElement.setAttribute("role", "alert");

        // Tạo nội dung HTML cho toast
        let contentHTML = "";

        if (settings.heading) {
            contentHTML += `<h4 class="alert-heading">${settings.heading}</h4>`;
        }

        if (settings.icon) {
            contentHTML += this.icons[settings.type];
        }

        if (settings.link) {
            contentHTML += `${settings.message} <a href="${settings.link}" class="alert-link">nhấp vào đây</a>`;
        } else {
            contentHTML += `<span>${settings.message}</span>`;
        }

        if (settings.additionalText) {
            contentHTML += `<hr><p class="mb-0">${settings.additionalText}</p>`;
        }

        if (settings.dismissible) {
            contentHTML += `<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>`;
        }

        if (settings.progress && settings.duration > 0) {
            contentHTML += `<div class="toast-progress"></div>`;
        }

        toastElement.innerHTML = contentHTML;

        // Thêm toast vào container
        const container = document.querySelector(`.toast-container-${positionClass}`);
        container.appendChild(toastElement);

        // Thêm sự kiện cho nút đóng
        const closeButton = toastElement.querySelector(".btn-close");
        if (closeButton) {
            closeButton.addEventListener("click", () => {
                this.hide(toastElement, settings.onClose);
            });
        }

        // Animate progress bar nếu có
        if (settings.progress && settings.duration > 0) {
            const progressBar = toastElement.querySelector(".toast-progress");
            if (progressBar) {
                setTimeout(() => {
                    progressBar.style.transition = `width ${settings.duration}ms linear`;
                    progressBar.style.width = "0%";
                }, 10);
            }
        }

        // Tự động đóng sau thời gian cài đặt
        if (settings.duration > 0) {
            setTimeout(() => {
                this.hide(toastElement, settings.onClose);
            }, settings.duration);
        }

        return toastElement;
    }

    // Ẩn toast
    hide(toastElement, onClose) {
        if (toastElement && toastElement.parentNode) {
            toastElement.classList.remove("show");
            setTimeout(() => {
                if (toastElement.parentNode) {
                    toastElement.parentNode.removeChild(toastElement);
                    // Gọi callback nếu có
                    if (typeof onClose === "function") {
                        onClose();
                    }
                }
            }, 300); // Đợi hiệu ứng fade out hoàn thành
        }
    }

    primary(message, options = {}) {
        return this.show({...options, type: "primary", message});
    }

    success(message, options = {}) {
        return this.show({...options, type: "success", message});
    }

    warning(message, options = {}) {
        return this.show({...options, type: "warning", message});
    }

    danger(message, options = {}) {
        return this.show({...options, type: "danger", message});
    }

    info(message, options = {}) {
        return this.show({...options, type: "info", message});
    }
}

export default new Toast();