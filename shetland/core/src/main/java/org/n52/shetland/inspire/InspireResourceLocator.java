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
package org.n52.shetland.inspire;

import java.util.Collection;
import java.util.Set;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * Service internal representation of INSPIRE resource locator
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireResourceLocator {

    /* Element URL 1..1 */
    private String url;

    /* Element MediaType 0..* */
    private Set<MediaType> mediaTypes = Sets.newHashSet();

    public InspireResourceLocator(String url) {
        setURL(url);
    }

    /**
     * Get the URL
     *
     * @return the URL
     */
    public String getURL() {
        return url;
    }

    /**
     * Set the URL
     *
     * @param url
     *            the URL to set
     */
    private void setURL(String url) {
        this.url = url;
    }

    /**
     * Check if the URL is set
     *
     * @return <code>true</code>, if the URL is set
     */
    public boolean isSetUrl() {
        return !Strings.isNullOrEmpty(getURL());
    }

    /**
     * Get the {@link MediaType}s
     *
     * @return the media types
     */
    public Set<MediaType> getMediaTypes() {
        return mediaTypes;
    }

    /**
     * Set the {@link MediaType}s, clears the existing collection
     *
     * @param mediaTypes
     *            the media types to set
     * @return this
     *
     */
    public InspireResourceLocator setMediaTypes(Collection<MediaType> mediaTypes) {
        getMediaTypes().clear();
        if (CollectionHelper.isNotEmpty(mediaTypes)) {
            getMediaTypes().addAll(mediaTypes);
        }
        return this;
    }

    /**
     * Add a {@link MediaType}
     *
     * @param mediaType
     *            the media type to add
     * @return this
     */
    public InspireResourceLocator addMediaType(MediaType mediaType) {
        getMediaTypes().add(mediaType);
        return this;
    }

    /**
     * Check if media types are set
     *
     * @return <code>true</code>, if media types are set
     */
    public boolean isSetMediaTypes() {
        return CollectionHelper.isNotEmpty(getMediaTypes());
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n url=%s,%n mediaTypes=%s%n]", this.getClass().getSimpleName(), getURL(),
                CollectionHelper.collectionToString(getMediaTypes()));
    }

}
