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
package org.n52.svalbard;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.siemens.ct.exi.api.sax.EXIResult;
import com.siemens.ct.exi.api.sax.EXISource;

public class XmlToExiConverter {

    private static final String EXI_EXTENSION = ".exi";

    private static final String XML_EXTENSION = ".xml";

    private static final String XML_EXTENSION_2 = ".xml_";

    protected void encode(String fileName) {
        try (InputStream exiIS = FileUtils.openInputStream(getFile(fileName, XML_EXTENSION));
                OutputStream exiOS = FileUtils.openOutputStream(getFile(fileName, EXI_EXTENSION))) {
            EXIResult exiResult = new EXIResult();
            exiResult.setOutputStream(exiOS);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(exiResult.getHandler());
            xmlReader.parse(new InputSource(exiIS));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected void decode(String fileName) {
        try (InputStream exiIS = FileUtils.openInputStream(getFile(fileName, EXI_EXTENSION));
                OutputStream os = FileUtils.openOutputStream(getFile(fileName, XML_EXTENSION_2))) {

            Reader reader = new InputStreamReader(exiIS,"ISO-8859-1");
            InputSource is = new InputSource(reader);
            is.setEncoding("ISO-8859-1");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            EXISource exiSource = new EXISource();
            XMLReader exiReader = exiSource.getXMLReader();
            SAXSource saxSource = new SAXSource(is);
//            SAXSource saxSource = new SAXSource(new InputSource(exiIS));
            exiSource.setXMLReader(exiReader);
            transformer.transform(saxSource, new StreamResult(os));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    protected File getFile(String fileName, String extension) {
        String folder = "xml";
        if (EXI_EXTENSION.equals(extension)) {
            folder = "exi";
        }
        return FileUtils.getFile("src", "test", "resources", folder, fileName + extension);
    }

    public static void main(String[] args) {
        XmlToExiConverter c = new XmlToExiConverter();
        System.out.println("Encode");
        c.encode("notebook");
//        c.encode("GetCapabilities");
//        c.encode("DescribeSensor");
//        c.encode("GetObservation");
//        c.encode("GetFeatureOfInterest");
//        c.encode("GetDataAvailability");
        System.out.println("Decode");
        c.decode("notebook");
//        c.decode("GetCapabilities");
//        c.decode("DescribeSensor");
//        c.decode("GetObservation");
//        c.decode("GetFeatureOfInterest");
//        c.decode("GetDataAvailability");
    }

}
