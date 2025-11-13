package com.niwe.erp.core.view;
 

import java.math.BigDecimal;
import java.util.UUID;

public record CoreItemListView(
        UUID id,
        String itemName,
        String itemCode,
        BigDecimal unitPrice,
        UUID taxId,
        UUID natureId,
        UUID classificationId
) {}
