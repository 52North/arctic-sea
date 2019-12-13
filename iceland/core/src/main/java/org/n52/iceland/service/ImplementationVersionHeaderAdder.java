package org.n52.iceland.service;

import javax.servlet.http.HttpServletResponse;

public interface ImplementationVersionHeaderAdder {

    void addVersion(HttpServletResponse response);
}
