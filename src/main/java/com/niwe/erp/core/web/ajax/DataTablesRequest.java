package com.niwe.erp.core.web.ajax;

import java.util.List;

public record DataTablesRequest(
	    int draw,
	    int start,
	    int length,
	    Search search,
	    List<Order> order,
	    List<Column> columns
	) {}

	record Search(String value, boolean regex) {}
	record Order(int column, String dir) {}
	record Column(String data, String name, boolean searchable, boolean orderable, Search search) {}
