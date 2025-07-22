/**
 * Admin JavaScript Functions
 * GymFitness Admin Panel
 */

$(document).ready(function () {
    // Initialize admin panel
    initAdminPanel();
});

/**
 * Initialize admin panel
 */
function initAdminPanel() {
    // Sidebar toggle
    initSidebarToggle();

    // Initialize tooltips
    initTooltips();

    // Initialize modals
    initModals();

    // Initialize data tables
    initDataTables();

    // Initialize form validation
    initFormValidation();

    // Initialize notifications
    initNotifications();

    // Initialize sidebar menu
    initSidebarMenu();

    // Initialize tab navigation
    initTabNavigation();

    // Initialize settings page if on settings page
    if (window.location.pathname.includes("settings.html")) {
        initSettingsPage();
    }
}

/**
 * Initialize sidebar toggle functionality
 */
function initSidebarToggle() {
    $("#sidebar-collapse").on("click", function () {
        $("#sidebar").toggleClass("active");
        // Update button text
        const span = $(this).find("span");
        if ($("#sidebar").hasClass("active")) {
            span.text("Mở rộng");
        } else {
            span.text("Thu gọn");
        }
    });
}

/**
 * Initialize Bootstrap tooltips
 */
function initTooltips() {
    $('[data-toggle="tooltip"]').tooltip();
}

/**
 * Initialize Bootstrap modals
 */
function initModals() {
    // Auto-hide modals after form submission
    $(".modal").on("hidden.bs.modal", function () {
        $(this).find("form").trigger("reset");
        $(this).find(".alert").remove();
    });
}

/**
 * Initialize data tables functionality
 */
function initDataTables() {
    // Select all checkbox functionality
    $("#selectAll").on("change", function () {
        const isChecked = $(this).prop("checked");
        $('tbody input[type="checkbox"]').prop("checked", isChecked);
        updateBulkActions();
    });

    // Individual checkbox functionality
    $('tbody input[type="checkbox"]').on("change", function () {
        updateSelectAllCheckbox();
        updateBulkActions();
    });
}

/**
 * Update select all checkbox state
 */
function updateSelectAllCheckbox() {
    const totalCheckboxes = $('tbody input[type="checkbox"]').length;
    const checkedCheckboxes = $('tbody input[type="checkbox"]:checked').length;

    if (checkedCheckboxes === 0) {
        $("#selectAll").prop("indeterminate", false).prop("checked", false);
    } else if (checkedCheckboxes === totalCheckboxes) {
        $("#selectAll").prop("indeterminate", false).prop("checked", true);
    } else {
        $("#selectAll").prop("indeterminate", true);
    }
}

/**
 * Update bulk actions visibility
 */
function updateBulkActions() {
    const checkedCount = $('tbody input[type="checkbox"]:checked').length;
    const bulkActionBtn = $(
        '.dropdown-toggle[aria-labelledby="bulkActionDropdown"]'
    );

    if (checkedCount > 0) {
        bulkActionBtn
            .removeClass("disabled")
            .text(`Thao tác hàng loạt (${checkedCount})`);
    } else {
        bulkActionBtn.addClass("disabled").text("Thao tác hàng loạt");
    }
}

/**
 * Initialize form validation
 */
function initFormValidation() {
    // Custom validation for required fields
    $("form").on("submit", function (e) {
        const requiredFields = $(this).find("[required]");
        let isValid = true;

        requiredFields.each(function () {
            if (!$(this).val()) {
                $(this).addClass("is-invalid");
                isValid = false;
            } else {
                $(this).removeClass("is-invalid");
            }
        });

        if (!isValid) {
            e.preventDefault();
            showNotification(
                "Vui lòng điền đầy đủ thông tin bắt buộc",
                "error"
            );
        }
    });

    // Remove validation styling on input
    $("input, select, textarea").on("input change", function () {
        if ($(this).val()) {
            $(this).removeClass("is-invalid");
        }
    });
}

/**
 * Initialize notifications
 */
function initNotifications() {
    // Auto-hide success notifications after 5 seconds
    setTimeout(function () {
        $(".alert-success").fadeOut();
    }, 5000);
}

/**
 * Show notification
 * @param {string} message - Notification message
 * @param {string} type - Notification type (success, error, warning, info)
 */
function showNotification(message, type = "info") {
    const alertClass = `alert-${type}`;
    const iconClass = getNotificationIcon(type);

    const notification = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            <i class="${iconClass}"></i> ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    `;

    // Insert notification at the top of the content area
    $("#content .container-fluid").prepend(notification);

    // Auto-hide after 5 seconds
    setTimeout(function () {
        $(".alert").fadeOut();
    }, 5000);
}

/**
 * Get notification icon class
 * @param {string} type - Notification type
 * @returns {string} Icon class
 */
function getNotificationIcon(type) {
    const icons = {
        success: "fas fa-check-circle",
        error: "fas fa-exclamation-circle",
        warning: "fas fa-exclamation-triangle",
        info: "fas fa-info-circle",
    };
    return icons[type] || icons.info;
}

/**
 * Confirm action with modal
 * @param {string} title - Modal title
 * @param {string} message - Modal message
 * @param {function} onConfirm - Callback function on confirm
 */
function confirmAction(title, message, onConfirm) {
    const modal = `
        <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmModalLabel">${title}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>${message}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        <button type="button" class="btn btn-danger" id="confirmBtn">Xác nhận</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    // Remove existing modal
    $("#confirmModal").remove();

    // Add new modal
    $("body").append(modal);

    // Show modal
    $("#confirmModal").modal("show");

    // Handle confirm button
    $("#confirmBtn").on("click", function () {
        $("#confirmModal").modal("hide");
        if (typeof onConfirm === "function") {
            onConfirm();
        }
    });
}

/**
 * Delete item with confirmation
 * @param {string} itemName - Name of item to delete
 * @param {function} deleteCallback - Callback function to perform deletion
 */
function deleteItem(itemName, deleteCallback) {
    confirmAction(
        "Xác nhận xóa",
        `Bạn có chắc chắn muốn xóa "${itemName}" không? Hành động này không thể hoàn tác.`,
        deleteCallback
    );
}

/**
 * Bulk delete items
 * @param {function} deleteCallback - Callback function to perform bulk deletion
 */
function bulkDeleteItems(deleteCallback) {
    const selectedItems = $('tbody input[type="checkbox"]:checked');
    const count = selectedItems.length;

    if (count === 0) {
        showNotification("Vui lòng chọn ít nhất một mục để xóa", "warning");
        return;
    }

    confirmAction(
        "Xác nhận xóa hàng loạt",
        `Bạn có chắc chắn muốn xóa ${count} mục đã chọn không? Hành động này không thể hoàn tác.`,
        deleteCallback
    );
}

/**
 * Export data to Excel
 * @param {string} tableId - ID of table to export
 * @param {string} filename - Export filename
 */
function exportToExcel(tableId, filename = "export") {
    const table = document.getElementById(tableId);
    if (!table) {
        showNotification("Không tìm thấy bảng dữ liệu", "error");
        return;
    }

    // Create workbook and worksheet
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.table_to_sheet(table);

    // Add worksheet to workbook
    XLSX.utils.book_append_sheet(wb, ws, "Sheet1");

    // Save file
    XLSX.writeFile(wb, `${filename}.xlsx`);

    showNotification("Xuất Excel thành công", "success");
}

/**
 * Export data to PDF
 * @param {string} tableId - ID of table to export
 * @param {string} filename - Export filename
 */
function exportToPDF(tableId, filename = "export") {
    const table = document.getElementById(tableId);
    if (!table) {
        showNotification("Không tìm thấy bảng dữ liệu", "error");
        return;
    }

    // Configure PDF options
    const opt = {
        margin: 1,
        filename: `${filename}.pdf`,
        image: { type: "jpeg", quality: 0.98 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: "in", format: "letter", orientation: "portrait" },
    };

    // Generate PDF
    html2pdf().set(opt).from(table).save();

    showNotification("Xuất PDF thành công", "success");
}

/**
 * Search and filter table data
 * @param {string} tableId - ID of table to search
 * @param {string} searchTerm - Search term
 */
function searchTable(tableId, searchTerm) {
    const table = $(`#${tableId} tbody tr`);

    table.each(function () {
        const text = $(this).text().toLowerCase();
        const match = text.includes(searchTerm.toLowerCase());
        $(this).toggle(match);
    });
}

/**
 * Filter table by column
 * @param {string} tableId - ID of table to filter
 * @param {number} columnIndex - Column index to filter
 * @param {string} filterValue - Filter value
 */
function filterTableByColumn(tableId, columnIndex, filterValue) {
    const table = $(`#${tableId} tbody tr`);

    table.each(function () {
        const cellText = $(this)
            .find(`td:eq(${columnIndex})`)
            .text()
            .toLowerCase();
        const match =
            filterValue === "" || cellText.includes(filterValue.toLowerCase());
        $(this).toggle(match);
    });
}

/**
 * Sort table by column
 * @param {string} tableId - ID of table to sort
 * @param {number} columnIndex - Column index to sort
 * @param {string} sortType - Sort type (asc, desc)
 */
function sortTable(tableId, columnIndex, sortType = "asc") {
    const table = $(`#${tableId} tbody`);
    const rows = table.find("tr").toArray();

    rows.sort(function (a, b) {
        const aText = $(a).find(`td:eq(${columnIndex})`).text();
        const bText = $(b).find(`td:eq(${columnIndex})`).text();

        if (sortType === "asc") {
            return aText.localeCompare(bText);
        } else {
            return bText.localeCompare(aText);
        }
    });

    table.empty().append(rows);
}

/**
 * Format currency
 * @param {number} amount - Amount to format
 * @param {string} currency - Currency code
 * @returns {string} Formatted currency
 */
function formatCurrency(amount, currency = "VND") {
    return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: currency,
    }).format(amount);
}

/**
 * Format date
 * @param {string} dateString - Date string
 * @param {string} format - Date format
 * @returns {string} Formatted date
 */
function formatDate(dateString, format = "dd/MM/yyyy") {
    const date = new Date(dateString);
    return date.toLocaleDateString("vi-VN");
}

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} Is valid email
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate phone number format
 * @param {string} phone - Phone number to validate
 * @returns {boolean} Is valid phone number
 */
function isValidPhone(phone) {
    const phoneRegex = /^[0-9]{10,11}$/;
    return phoneRegex.test(phone.replace(/\s/g, ""));
}

/**
 * Generate random ID
 * @param {string} prefix - ID prefix
 * @param {number} length - ID length
 * @returns {string} Random ID
 */
function generateId(prefix = "", length = 6) {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    let result = "";
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return prefix + result;
}

/**
 * Calculate date difference
 * @param {string} startDate - Start date
 * @param {string} endDate - End date
 * @returns {number} Days difference
 */
function getDaysDifference(startDate, endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end - start);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
}

/**
 * Check if date is expired
 * @param {string} date - Date to check
 * @returns {boolean} Is expired
 */
function isExpired(date) {
    const checkDate = new Date(date);
    const today = new Date();
    return checkDate < today;
}

/**
 * Get status badge HTML
 * @param {string} status - Status text
 * @param {string} type - Badge type
 * @returns {string} Badge HTML
 */
function getStatusBadge(status, type = "success") {
    const badgeTypes = {
        active: "success",
        inactive: "secondary",
        pending: "warning",
        expired: "danger",
        suspended: "danger",
    };

    const badgeType = badgeTypes[status.toLowerCase()] || type;
    return `<span class="badge badge-${badgeType}">${status}</span>`;
}

/**
 * Load data with AJAX
 * @param {string} url - API URL
 * @param {object} params - Request parameters
 * @param {function} callback - Success callback
 */
function loadData(url, params = {}, callback) {
    $.ajax({
        url: url,
        method: "GET",
        data: params,
        success: function (response) {
            if (typeof callback === "function") {
                callback(response);
            }
        },
        error: function (xhr, status, error) {
            showNotification("Lỗi khi tải dữ liệu: " + error, "error");
        },
    });
}

/**
 * Save data with AJAX
 * @param {string} url - API URL
 * @param {object} data - Data to save
 * @param {function} callback - Success callback
 */
function saveData(url, data, callback) {
    $.ajax({
        url: url,
        method: "POST",
        data: data,
        success: function (response) {
            showNotification("Lưu dữ liệu thành công", "success");
            if (typeof callback === "function") {
                callback(response);
            }
        },
        error: function (xhr, status, error) {
            showNotification("Lỗi khi lưu dữ liệu: " + error, "error");
        },
    });
}

/**
 * Delete data with AJAX
 * @param {string} url - API URL
 * @param {object} data - Data to delete
 * @param {function} callback - Success callback
 */
function deleteData(url, data, callback) {
    $.ajax({
        url: url,
        method: "DELETE",
        data: data,
        success: function (response) {
            showNotification("Xóa dữ liệu thành công", "success");
            if (typeof callback === "function") {
                callback(response);
            }
        },
        error: function (xhr, status, error) {
            showNotification("Lỗi khi xóa dữ liệu: " + error, "error");
        },
    });
}

/**
 * Initialize sidebar menu functionality
 */
function initSidebarMenu() {
    // Wrap menu text in spans for better text overflow handling
    $("#sidebar ul li a").each(function () {
        const $link = $(this);
        const $icon = $link.find("i:first");
        const text = $link
            .contents()
            .filter(function () {
                return this.nodeType === 3; // Text nodes only
            })
            .text()
            .trim();

        if (text && !$link.find("span").length) {
            // Remove text nodes and add span
            $link
                .contents()
                .filter(function () {
                    return this.nodeType === 3;
                })
                .remove();

            if ($icon.length) {
                $icon.after("<span>" + text + "</span>");
            } else {
                $link.prepend("<span>" + text + "</span>");
            }
        }
    });

    // Handle responsive menu
    handleResponsiveMenu();
}

/**
 * Handle responsive menu behavior
 */
function handleResponsiveMenu() {
    const $sidebar = $("#sidebar");
    const $content = $("#content");

    function updateLayout() {
        const width = $(window).width();

        if (width <= 576) {
            $sidebar.css({
                "min-width": "250px",
                "max-width": "250px",
            });
        } else if (width <= 768) {
            $sidebar.css({
                "min-width": "280px",
                "max-width": "280px",
            });
        } else if (width <= 992) {
            $sidebar.css({
                "min-width": "240px",
                "max-width": "240px",
            });
        } else if (width <= 1200) {
            $sidebar.css({
                "min-width": "260px",
                "max-width": "260px",
            });
        } else {
            $sidebar.css({
                "min-width": "280px",
                "max-width": "280px",
            });
        }
    }

    // Update on window resize
    $(window).on("resize", updateLayout);

    // Initial update
    updateLayout();
}

/**
 * Initialize tab navigation
 */
function initTabNavigation() {
    // Use Bootstrap 4 tab API
    $('a[data-toggle="tab"]').on("shown.bs.tab", function (e) {
        // Tab has been shown
        console.log("Tab switched to:", e.target);
    });

    // Ensure first tab is active on page load
    if ($(".nav-tabs .nav-link.active").length === 0) {
        $(".nav-tabs .nav-link:first").addClass("active");
        $(".tab-content .tab-pane:first").addClass("show active");
    }
}

/**
 * Initialize settings page functionality
 */
function initSettingsPage() {
    // Form submissions for settings page
    $("#generalSettingsForm").on("submit", function (e) {
        e.preventDefault();
        showNotification("Đã lưu cài đặt chung!", "success");
    });

    $("#gymInfoForm").on("submit", function (e) {
        e.preventDefault();
        showNotification("Đã lưu thông tin phòng gym!", "success");
    });

    $("#userSettingsForm").on("submit", function (e) {
        e.preventDefault();
        const newPassword = $("#newPassword").val();
        const confirmPassword = $("#confirmPassword").val();

        if (newPassword && newPassword !== confirmPassword) {
            showNotification("Mật khẩu xác nhận không khớp!", "error");
            return;
        }

        showNotification("Đã cập nhật thông tin người dùng!", "success");
    });

    $("#notificationSettingsForm").on("submit", function (e) {
        e.preventDefault();
        showNotification("Đã lưu cài đặt thông báo!", "success");
    });

    $("#securitySettingsForm").on("submit", function (e) {
        e.preventDefault();
        showNotification("Đã lưu cài đặt bảo mật!", "success");
    });

    $("#paymentSettingsForm").on("submit", function (e) {
        e.preventDefault();
        showNotification("Đã lưu cài đặt thanh toán!", "success");
    });

    // Backup and restore functionality
    $("#backupData").on("click", function () {
        showNotification("Đang tạo bản sao lưu...", "info");
    });

    $("#restoreData").on("click", function () {
        confirmAction(
            "Xác nhận khôi phục",
            "Bạn có chắc chắn muốn khôi phục dữ liệu? Hành động này sẽ ghi đè dữ liệu hiện tại.",
            function () {
                showNotification("Đang khôi phục dữ liệu...", "info");
            }
        );
    });
}

// Global utility functions
window.AdminUtils = {
    showNotification,
    confirmAction,
    deleteItem,
    bulkDeleteItems,
    exportToExcel,
    exportToPDF,
    searchTable,
    filterTableByColumn,
    sortTable,
    formatCurrency,
    formatDate,
    isValidEmail,
    isValidPhone,
    generateId,
    getDaysDifference,
    isExpired,
    getStatusBadge,
    loadData,
    saveData,
    deleteData,
};
