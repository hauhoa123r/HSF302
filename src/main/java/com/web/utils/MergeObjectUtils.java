package com.web.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;

@Component
public class MergeObjectUtils {
    /**
     * Gộp các trường không null từ đối tượng nguồn (source) sang đối tượng đích (target).
     * - Nếu trường là kiểu đơn giản (số, chuỗi, ngày tháng, enum, ...), sẽ ghi đè giá trị từ source sang target nếu giá trị ở source khác null.
     * - Nếu trường là một object (lồng nhau), hàm sẽ tự động gọi lại chính nó để tiếp tục gộp các trường bên trong.
     * - Nếu trường ở source là null thì target giữ nguyên giá trị cũ.
     * Lưu ý:
     * - Hàm này sẽ cập nhật trực tiếp giá trị của target, không tạo mới đối tượng.
     * - Dùng được cho các entity/phức tạp, kể cả trường lồng nhau.
     *
     * @param source Đối tượng nguồn - các trường có giá trị không null sẽ được lấy từ đây
     * @param target Đối tượng đích - sẽ được cập nhật lại các trường theo source
     */
    public void mergeNonNullFields(Object source, Object target) {
        if (source == null || target == null) return;

        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        BeanWrapper tgtWrapper = new BeanWrapperImpl(target);

        PropertyDescriptor[] propertyDescriptors = srcWrapper.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            if ("class".equals(propertyName)) continue;

            Object srcValue = srcWrapper.getPropertyValue(propertyName);
            Object tgtValue = tgtWrapper.getPropertyValue(propertyName);

            if (srcValue != null && BeanUtils.isSimpleValueType(propertyDescriptor.getPropertyType())) {
                tgtWrapper.setPropertyValue(propertyName, srcValue);
            } else if (srcValue != null && tgtValue != null) {
                // Đệ quy cho trường lồng nhau
                mergeNonNullFields(srcValue, tgtValue);
            }
        }
    }
}