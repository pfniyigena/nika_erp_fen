package com.niwe.erp.web.api.event;

import org.springframework.context.ApplicationEvent;

import com.niwe.erp.sale.domain.Sale;

public class SaleCreatedEvent extends ApplicationEvent {
    /**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The sale
	 */
	private final Sale sale;
    
    public SaleCreatedEvent(Object source, Sale sale) {
        super(source);
        this.sale = sale;
    }
    
    public Sale getSale() { return sale; }
}