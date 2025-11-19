package com.niwe.erp.web.api.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.sale.domain.Sale;

public class ItemCreatedEvent extends ApplicationEvent {
    /**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The sale
	 */
	private final List<CoreItem> items;
    
    public ItemCreatedEvent(Object source, List<CoreItem> items) {
        super(source);
        this.items = items;
    }
    
    public List<CoreItem> getItems() { return items; }
}