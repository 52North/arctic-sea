/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * class for FES 2.0 AbstractAdHocQueryExpression
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 *
 */
public abstract class AbstractAdHocQueryExpression extends AbstractQueryExpression {

    private Set<AbstractProjectionClause> projectionClauses = new HashSet<>(0);
    private AbstractSelectionClause selectionClause;
    private AbstractSortingClause sortingClause;
    private Set<QName> typeNames = new HashSet<>(0);
    private Set<String> aliases = new HashSet<>(0);

    public AbstractAdHocQueryExpression(Collection<QName> typeNames) {
        setTypeNames(typeNames);
    }

    /**
     * @return the projectionClauses
     */
    public Set<AbstractProjectionClause> getProjectionClauses() {
        return Collections.unmodifiableSet(this.projectionClauses);
    }

    /**
     * @param projectionClause the projectionClause to add
     *
     * @return this
     */
    public AbstractAdHocQueryExpression addProjectionClause(AbstractProjectionClause projectionClause) {
        this.projectionClauses.add(projectionClause);
        return this;
    }

    /**
     * @param projectionClauses the projectionClauses to add
     *
     * @return this
     */
    public AbstractAdHocQueryExpression addProjectionClauses(Set<AbstractProjectionClause> projectionClauses) {
        this.projectionClauses.addAll(projectionClauses);
        return this;
    }

    /**
     * @param projectionClauses the projectionClauses to set
     *
     * @return this
     */
    public AbstractAdHocQueryExpression setProjectionClauses(Set<AbstractProjectionClause> projectionClauses) {
        this.projectionClauses = Optional.ofNullable(projectionClauses).orElseGet(HashSet::new);
        return this;
    }

    public boolean isSetProjectionClauses() {
        return this.projectionClauses != null && !this.projectionClauses.isEmpty();
    }

    /**
     * @return the selectionClause
     */
    public AbstractSelectionClause getSelectionClause() {
        return selectionClause;
    }

    /**
     * @param selectionClause the selectionClause to set
     *
     * @return this
     */
    public AbstractAdHocQueryExpression setSelectionClause(AbstractSelectionClause selectionClause) {
        this.selectionClause = selectionClause;
        return this;
    }

    public boolean isSetSelectionClause() {
        return this.selectionClause != null;
    }

    /**
     * @return the sortingClause
     */
    public AbstractSortingClause getSortingClause() {
        return sortingClause;
    }

    /**
     * @param sortingClause the sortingClause to set
     *
     * @return this
     */
    public AbstractAdHocQueryExpression setSortingClause(AbstractSortingClause sortingClause) {
        this.sortingClause = sortingClause;
        return this;
    }

    public boolean isSetSortingClause() {
        return this.sortingClause != null;
    }

    /**
     * @return the typeNames
     */
    public Set<QName> getTypeNames() {
        return Collections.unmodifiableSet(typeNames);
    }

    /**
     * @param typeNames the typeNames to set
     */
    private void setTypeNames(Collection<QName> typeNames) {
        this.typeNames = Optional.ofNullable(typeNames).map(HashSet::new).orElseGet(HashSet::new);
    }

    /**
     * @return the aliases
     */
    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    /**
     * @param aliases the aliases to set
     *
     * @return this
     */
    public AbstractAdHocQueryExpression setAliases(Collection<String> aliases) {
        this.aliases = Optional.ofNullable(aliases).map(HashSet::new).orElseGet(HashSet::new);
        return this;
    }

    public boolean isSetAliases() {
        return this.aliases != null && !this.aliases.isEmpty();
    }
}
