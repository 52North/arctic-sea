package org.n52.shetland.ogc.sos;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

    private TestObject createTestObject(Integer object, String xml) {
        return new TestObject(object, xml);
    }

    class TestObject extends ObjectWithXmlString<Integer> {

        public TestObject(Integer object, String xml) {
            super(object, xml);
        }

    }
}
