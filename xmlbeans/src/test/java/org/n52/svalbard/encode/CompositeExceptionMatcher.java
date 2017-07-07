/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
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
package org.n52.svalbard.encode;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 * J&uuml;rrens</a>
 */
public class CompositeExceptionMatcher extends BaseMatcher<CompositeOwsException>{

    private int expectedSize;

    private List<Matcher<?>> exceptionMatcher;

    boolean classNotMatched, sizeNotMatched;
    int subMatcherNotMatched = -1;

    public CompositeExceptionMatcher() {
    }

    @Override
    public boolean matches(Object o) {
        if (o == null) {
            return false;
        }
        if (!o.getClass().isAssignableFrom(CompositeOwsException.class)) {
            classNotMatched = true;
            return false;
        }
        CompositeOwsException e = (CompositeOwsException) o;
        if (e.getExceptions().size() != expectedSize) {
            sizeNotMatched = true;
            return false;
        }
        for (int i = 0; i < expectedSize; i++) {
            if (!exceptionMatcher.get(i).matches(e.getExceptions().get(i))) {
                subMatcherNotMatched = i;
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description d) {
        if (classNotMatched) {
            d.appendText("type ").appendText(this.getClass().getName());
        } else if (sizeNotMatched) {
            d.appendText(" exceptions ").appendValue(expectedSize);
        } else if (subMatcherNotMatched != -1) {
            exceptionMatcher.get(subMatcherNotMatched).describeTo(d);
        }
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (classNotMatched) {
            description.appendText(item.getClass().getName());
        } else if (sizeNotMatched) {
            description.appendValue(((CompositeOwsException)item).getExceptions().size());
        } else if (subMatcherNotMatched != -1) {
            exceptionMatcher
                    .get(subMatcherNotMatched)
                    .describeMismatch(
                            ((CompositeOwsException)item)
                                    .getExceptions()
                                    .get(subMatcherNotMatched),
                            description);
        }
    }



    public CompositeExceptionMatcher with(Class<? extends OwsExceptionReport> exceptionClass) {
        if (exceptionClass != null) {
            if (exceptionMatcher == null) {
                exceptionMatcher = Lists.newArrayList();
            }
            exceptionMatcher.add(CoreMatchers.instanceOf(exceptionClass));
            expectedSize++;
        }
        return this;
    }

}
