package com.quoteflow.util;

import com.chaoxing.office.app.entity.forms.dto.data.field.value.BaseFieldValue;
import com.chaoxing.office.app.entity.forms.dto.data.field.value.ContactFieldValue;
import com.chaoxing.office.app.entity.forms.dto.data.field.value.NumberFieldValue;
import com.chaoxing.office.app.entity.forms.dto.data.field.value.TextFieldValue;
import com.chaoxing.office.app.entity.forms.vo.data.ApiFormUser;
import com.chaoxing.office.app.entity.forms.vo.data.Field;
import com.quoteflow.dto.ContactDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Utility for extracting values from Chaoxing form fields.
 *
 * @author QuoteFlow
 */
public final class FormFieldValueUtils {

    private FormFieldValueUtils() {
    }

    /**
     * Gets first text-like value by alias.
     *
     * @param apiFormUser form data
     * @param alias field alias
     * @return first text-like value
     */
    public static String getFirstText(ApiFormUser apiFormUser, String alias) {
        Field field = findField(apiFormUser, alias);
        if (field == null || field.getValueList() == null || field.getValueList().isEmpty()) {
            return null;
        }
        BaseFieldValue value = field.getValueList().get(0);
        if (value instanceof TextFieldValue) {
            return ((TextFieldValue) value).getVal();
        }
        if (value instanceof NumberFieldValue) {
            NumberFieldValue numberFieldValue = (NumberFieldValue) value;
            if (numberFieldValue.getVal() != null) {
                return BigDecimal.valueOf(numberFieldValue.getVal()).toPlainString();
            }
            return numberFieldValue.getStringVal();
        }
        if (value instanceof ContactFieldValue) {
            return ((ContactFieldValue) value).getUname();
        }
        return null;
    }

    /**
     * Gets first number value by alias.
     *
     * @param apiFormUser form data
     * @param alias field alias
     * @return first number value
     */
    public static BigDecimal getFirstNumber(ApiFormUser apiFormUser, String alias) {
        Field field = findField(apiFormUser, alias);
        if (field == null || field.getValueList() == null || field.getValueList().isEmpty()) {
            return BigDecimal.ZERO;
        }
        BaseFieldValue value = field.getValueList().get(0);
        if (value instanceof NumberFieldValue) {
            NumberFieldValue numberFieldValue = (NumberFieldValue) value;
            if (numberFieldValue.getVal() != null) {
                return BigDecimal.valueOf(numberFieldValue.getVal());
            }
            if (numberFieldValue.getStringVal() != null) {
                return new BigDecimal(numberFieldValue.getStringVal());
            }
        }
        if (value instanceof TextFieldValue && ((TextFieldValue) value).getVal() != null) {
            return new BigDecimal(((TextFieldValue) value).getVal());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Gets first contact value by alias.
     *
     * @param apiFormUser form data
     * @param alias field alias
     * @return first contact
     */
    public static ContactDTO getFirstContact(ApiFormUser apiFormUser, String alias) {
        Field field = findField(apiFormUser, alias);
        if (field == null || field.getValueList() == null || field.getValueList().isEmpty()) {
            return null;
        }
        BaseFieldValue value = field.getValueList().get(0);
        if (!(value instanceof ContactFieldValue)) {
            return null;
        }
        ContactFieldValue contactFieldValue = (ContactFieldValue) value;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setUid(contactFieldValue.getPuid());
        contactDTO.setName(contactFieldValue.getUname());
        return contactDTO;
    }

    private static Field findField(ApiFormUser apiFormUser, String alias) {
        if (apiFormUser == null || apiFormUser.getFormData() == null) {
            return null;
        }
        List<Field> fieldList = apiFormUser.getFormData();
        for (Field field : fieldList) {
            if (Objects.equals(alias, field.getAlias())) {
                return field;
            }
        }
        return null;
    }
}
