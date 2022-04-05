/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.n52.janmayen.http.MediaType;

public class ObjectWithXmlTest {

    @Test
    public void object_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(Integer.valueOf(1), null);
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(1), null);
        assertTrue(one.equals(other));
        assertTrue(other.equals(one));
    }

    @Test
    public void xml_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(null, "xml");
        ObjectWithXmlString<Integer> other = createTestObject(null, "xml");
        assertTrue(one.equals(other));
        assertTrue(other.equals(one));
    }

    @Test
    public void both_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(Integer.valueOf(1), "xml");
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(1), "xml");
        assertTrue(one.equals(other));
        assertTrue(other.equals(one));
    }

    @Test
    public void one_without_xml_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(Integer.valueOf(1), null);
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(1), "xml");
        assertTrue(one.equals(other));
        assertTrue(other.equals(one));
    }

    @Test
    public void one_without_object_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(null, "xml");
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(1), "xml");
        assertTrue(one.equals(other));
        assertTrue(other.equals(one));
    }

    @Test
    public void one_without_object_other_without_xml_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(null, "xml");
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(1), null);
        assertFalse(one.equals(other));
        assertFalse(other.equals(one));
    }

    @Test
    public void object_not_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(Integer.valueOf(1), null);
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(2), null);
        assertFalse(one.equals(other));
        assertFalse(other.equals(one));
    }

    @Test
    public void xml_not_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(null, "xml");
        ObjectWithXmlString<Integer> other = createTestObject(null, "xml2");
        assertFalse(one.equals(other));
        assertFalse(other.equals(one));
    }

    @Test
    public void both_not_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(Integer.valueOf(1), "xml");
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(2), "xml2");
        assertFalse(one.equals(other));
        assertFalse(other.equals(one));
    }

    @Test
    public void one_without_xml_not_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(Integer.valueOf(1), null);
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(2), "xml");
        assertFalse(one.equals(other));
        assertFalse(other.equals(one));
    }

    @Test
    public void one_without_object_not_equals() {
        ObjectWithXmlString<Integer> one = createTestObject(null, "xml");
        ObjectWithXmlString<Integer> other = createTestObject(Integer.valueOf(1), "xml2");
        assertFalse(one.equals(other));
        assertFalse(other.equals(one));
    }

    @Test
    public void set_clear_xml() {
        ObjectWithXmlString<Integer> o = createTestObject(Integer.valueOf(1), "xml");
        o.clearXml();
        assertTrue(o.get().isPresent());
        assertFalse(o.getXml().isPresent());
    }

    @Test
    public void set_clear_xml_null_object() {
        ObjectWithXmlString<Integer> o = createTestObject(null, "xml");
        assertThrows(NullPointerException.class, () -> {
            o.clearXml();
        });

    }

    private TestObject createTestObject(Integer object, String xml) {
        return new TestObject(object, xml);
    }

    class TestObject extends ObjectWithXmlString<Integer> {

        public TestObject(Integer object, String xml) {
            super(object, xml);
        }
    }
}
