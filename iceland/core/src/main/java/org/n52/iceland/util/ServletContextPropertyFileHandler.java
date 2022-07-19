/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.util;

import java.nio.file.Paths;

import javax.servlet.ServletContext;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public class ServletContextPropertyFileHandler extends PropertyFileHandlerImpl {

    public ServletContextPropertyFileHandler(ServletContext ctx, String name) {
        super(ctx.getRealPath(name) != null ? ctx.getRealPath(name)
                : Paths.get(ctx.getRealPath("/"), name).toString());
    }

}
