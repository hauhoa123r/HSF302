export class PromotionResponse {
    constructor(id, name, discountPercent) {
        this.id = id;
        this.name = name;
        this.discountPercent = discountPercent;
    }

    static fromJson(json) {
        if (!json) return null;
        return new PromotionResponse(
                json.id,
                json.name,
                json.discountPercent !== undefined ? json.discountPercent : 0
        );
    }

    static fromJsonArray(jsonArray) {
        return jsonArray.map((json) => PromotionResponse.fromJson(json));
    }

    setRenderStrategy(renderStrategy) {
        this.renderStrategy = renderStrategy;
        return this;
    }

    render(...args) {
        if (this.renderStrategy) {
            return this.renderStrategy(this, ...args);
        }
        throw new Error("Render strategy is not set for PromotionResponse");
    }
}

/**
 * Render the response list for select options
 * @param {PromotionResponse} promotion
 * @returns {string}
 */
export function renderPromotionOption(promotion) {
    return `
    <option data-discount="${promotion.discountPercent}" value="${promotion.id}">${promotion.name} - ${promotion.discountPercent}%</option>
        `;
}