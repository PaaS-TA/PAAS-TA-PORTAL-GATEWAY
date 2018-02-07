package org.openpaas.paasta.gateway.filter;

public interface FilterOrder {
	public interface PreRouteOrder {		
		int RequestLoggingFilter = 99;
	}
	
	public interface RouteOrder {
	    int EurekaStaticResourceRouteFilter = 11;
	}
	
	public interface PostRouteOrder {
	    
	}
	
	public interface ErrorOrder {
		
	}
}
