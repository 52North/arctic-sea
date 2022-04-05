/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
import java.util.List;
import java.util.Set;

import org.n52.shetland.inspire.dls.InspireCapabilities.InspireExtendedCapabilitiesResourceLocator;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Service internal representation of INSPIRE citation
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireCitation implements InspireExtendedCapabilitiesResourceLocator {

    /* Title 1..1 */
    private String title;

    /* Choice: DateOfPublication, DateOfCreation, DateOfLastRevision 1..1 */
    private InspireDateOf dateOf;

    /* Url 0..* */
    private Set<String> urls = Sets.newHashSet();

    /* ResourceLocator 0..* */
    private List<InspireResourceLocator> resourceLocator = Lists.newArrayList();

    /**
     * constructor
     *
     * @param title
     *            the title
     * @param dateOf
     *            the {@link InspireDateOf}
     */
    public InspireCitation(String title, InspireDateOf dateOf) {
        super();
        setTitle(title);
        setDateOf(dateOf);
    }

    /**
     * Get the title
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title
     *
     * @param title
     *            the title to set
     */
    private void setTitle(String title) {
        this.title = title;
    }

    /**
     * Check if the title is set
     *
     * @return <code>true</code>, if the title is set
     */
    public boolean isSetTitle() {
        return !Strings.isNullOrEmpty(getTitle());
    }

    /**
     * Get the {@link InspireDateOf}
     *
     * @return the dateOf
     */
    public InspireDateOf getDateOf() {
        return dateOf;
    }

    /**
     * Set the {@link InspireDateOf}
     *
     * @param dateOf
     *            the dateOf to set
     */
    private void setDateOf(InspireDateOf dateOf) {
        this.dateOf = dateOf;
    }

    /**
     * Check if the {@link InspireDateOf} is set
     *
     * @return <code>true</code>, if the {@link InspireDateOf} is set
     */
    public boolean isSetDateOf() {
        return getDateOf() != null;
    }

    /**
     * Get the URLs
     *
     * @return the URLs
     */
    public Set<String> getUrls() {
        return urls;
    }

    /**
     * Set the URLs, clears the existing collection
     *
     * @param urls
     *            the URLs to set
     * @return this
     */
    public InspireCitation setUrls(Collection<String> urls) {
        if (CollectionHelper.isNotEmpty(urls)) {
            getUrls().clear();
            getUrls().addAll(urls);
        }
        return this;
    }

    /**
     * Add the URL
     *
     * @param url
     *            the URL to add
     * @return this
     */
    public InspireCitation addUrl(String url) {
        getUrls().add(url);
        return this;
    }

    /**
     * Check if the URLs are set
     *
     * @return <code>true</code>, if URLs are set
     */
    public boolean isSetUrls() {
        return CollectionHelper.isNotEmpty(getUrls());
    }

    @Override
    public List<InspireResourceLocator> getResourceLocator() {
        return resourceLocator;
    }

    @Override
    public InspireExtendedCapabilitiesResourceLocator setResourceLocator(
            Collection<InspireResourceLocator> resourceLocator) {
        getResourceLocator().clear();
        if (CollectionHelper.isNotEmpty(resourceLocator)) {
            getResourceLocator().addAll(resourceLocator);
        }
        return this;
    }

    @Override
    public InspireExtendedCapabilitiesResourceLocator addResourceLocator(InspireResourceLocator resourceLocator) {
        getResourceLocator().add(resourceLocator);
        return this;
    }

    @Override
    public boolean isSetResourceLocators() {
        return CollectionHelper.isNotEmpty(getResourceLocator());
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n title=%s,%n dateOf=%s,%n urls=%s,%n resourceLocator=%s%n]", this.getClass()
                .getSimpleName(), getTitle(), getDateOf(), CollectionHelper.collectionToString(getUrls()),
                CollectionHelper.collectionToString(getResourceLocator()));
    }
}
