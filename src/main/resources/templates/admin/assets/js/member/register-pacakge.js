import {MemberPackageDTO} from "/templates/admin/assets/js/model/dto/MemberPackageDTO.js";
import {PromotionResponse, renderPromotionOption} from "/templates/admin/assets/js/model/response/PromotionResponse.js";
import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import {FormatUtils} from "/templates/shared/assets/js/utils/format-utils.js";
import toast from "/templates/shared/assets/js/utils/toast.js";

const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

document.addEventListener("DOMContentLoaded", async () => {
    setupInputEvents();
    setupFormEvents();
});

function setupInputEvents() {
    $("#member-package-select").addEventListener("change", async (event) => {
        const promotionSelect = $("#promotion-select");
        if (!promotionSelect) return;
        const selectedPackage = event.target.querySelector(`option[value="${event.target.value}"]`);
        $("#package-price").value = FormatUtils.formatPrice(parseInt(selectedPackage.getAttribute("data-price") || ""));
        const packageId = event.target.value;
        const data = await FetchingUtils.fetch(`/api/promotion/package/${packageId}`);
        if (data) {
            promotionSelect.innerHTML = "";
            const defaultOption = "<option value=''>-- Chọn khuyến mãi --</option>";
            if (data.length > 0) {
                promotionSelect.innerHTML = defaultOption + PromotionResponse.fromJsonArray(data).map(promotion => promotion.setRenderStrategy(renderPromotionOption).render()).join("");
            }
        }

        promotionSelect.addEventListener("change", (event) => {
            const selectedPromotion = event.target.querySelector(`option[value="${event.target.value}"]`);
            if (selectedPromotion) {
                const percentDiscount = parseInt(selectedPromotion.getAttribute("data-discount") || "0");
                $("#discount").value = percentDiscount;
                const packagePrice = parseInt(selectedPackage.getAttribute("data-price") || "0");
                $("#final-price").value = FormatUtils.formatPrice((100 - percentDiscount) * packagePrice / 100);
            } else {
                $("#discount").value = "0";
                $("#final-price").value = FormatUtils.formatPrice(parseInt($("#package-price").value.replace(/[^0-9]/g, "")));
            }
        });
        promotionSelect.dispatchEvent(new Event("change"));

        $("#start-date").addEventListener("change", (event) => {
            const startDate = new Date(event.target.value);
            const durationDays = parseInt(selectedPackage.getAttribute("data-duration-day") || "0");
            const endDate = new Date(startDate.getTime() + (durationDays * 24 * 60 * 60 * 1000));
            $("#end-date").value = endDate.toISOString().split("T")[0];
        });
    });
}

function setupFormEvents() {
    const registerPackageForm = $("#register-package-form");
    if (!registerPackageForm) return;
    registerPackageForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const formData = new FormData(registerPackageForm);
        const memberPackageDTO = FormDataUtils.getObjectFromFormData(new MemberPackageDTO(), formData);
        const data = await FetchingUtils.fetch("/api/admin/member/package", {
            method: "POST",
            body: JSON.stringify(memberPackageDTO),
            headers: {
                "Content-Type": "application/json"
            }
        }, "text");
        if (data !== null) {
            toast.success("Đăng ký gói thành công, vui lòng thanh toán!", {
                duration: 3000,
                icon: true,
                progress: true,
                onClose: () => {
                    window.location.href = "/admin/member";
                }
            });
        }
    });
}