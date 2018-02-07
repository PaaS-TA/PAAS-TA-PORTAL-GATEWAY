package org.openpaas.paasta.gateway.filter;

public interface FilterType {
	final String PRE = "pre";
	final String POST = "post";
	final String ROUTE = "route";
	final String ERROR = "error";
	
	final String DISABLED = "none";
}
