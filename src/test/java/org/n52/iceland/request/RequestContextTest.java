package org.n52.iceland.request;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestContextTest {
		
	@Test
	public void ip4mappedip6RemoteAddress() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();	
		mockRequest.setRemoteAddr("::ffff:217.173.34.182");
		RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
		Assert.assertEquals("217.173.34.182", fromRequest.getIPAddress().get().toString());
		
		mockRequest.setRemoteAddr("0:0:0:0:0:ffff:217.173.34.182");
		fromRequest = RequestContext.fromRequest(mockRequest);
		Assert.assertEquals("217.173.34.182", fromRequest.getIPAddress().get().toString());
	}
	
	@Test
	public void ip4CompactIpv6Address() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();	
		mockRequest.setRemoteAddr("::101.45.75.219");
		RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
		Assert.assertEquals("101.45.75.219", fromRequest.getIPAddress().get().toString());
		
		mockRequest.setRemoteAddr("0:0:0:0:0:0:101.45.75.219");
		fromRequest = RequestContext.fromRequest(mockRequest);
		
		Assert.assertEquals("101.45.75.219", fromRequest.getIPAddress().get().toString());
	}
	
	@Test
	public void ipv6LocalhostAddress() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();	
		mockRequest.setRemoteAddr("::1");
		RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
		Assert.assertEquals("127.0.0.1", fromRequest.getIPAddress().get().toString());
	}

}
