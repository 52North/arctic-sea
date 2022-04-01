/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class to represent a characater class.
 *
 * @author Christian Autermann
 */
public final class CharacterClass {

    private final Set<Integer> characters;

    /**
     * Creates a new {@code CharacterClass}
     *
     * @param characters the characters
     */
    private CharacterClass(Set<Integer> characters) {
        this.characters = Objects.requireNonNull(characters);
    }

    /**
     * Get the characters represented by this class.
     *
     * @return the characters
     */
    public Set<Integer> chars() {
        return Collections.unmodifiableSet(characters);
    }

    /**
     * Check if {@code c} is included in this class.
     *
     * @param c the character
     *
     * @return if it is included
     */
    public boolean includes(int c) {
        return characters.contains(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(characters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() &&
                               Objects.equals(chars(), ((CharacterClass) obj).chars());
    }

    /**
     * Creates a new character class including the supplied character classes.
     *
     * @param characters the character classes
     *
     * @return the character class
     */
    public static CharacterClass forClasses(CharacterClass... characters) {
        return forClasses(Arrays.asList(characters));
    }

    /**
     * Creates a new character class including the supplied character classes.
     *
     * @param characters the character classes
     *
     * @return the character class
     */
    public static CharacterClass forClasses(Collection<CharacterClass> characters) {
        return new CharacterClass(characters.stream().map(CharacterClass::chars)
                .flatMap(Set::stream).collect(Collectors.toSet()));
    }

    /**
     * Creates a new character class including the range of characters between {@code min} and {@code max}.
     *
     * @param min the (inclusive) minimum
     * @param max the (inclusive) maximum
     *
     * @return the character class
     */
    public static CharacterClass forRange(int min, int max) {
        return new CharacterClass(IntStream.rangeClosed(min, max)
                .mapToObj(Integer::valueOf).collect(Collectors.toSet()));
    }

    /**
     * Creates a new character class including the supplied characters.
     *
     * @param characters the characters
     *
     * @return the character class
     */
    public static CharacterClass forChars(Character... characters) {
        return new CharacterClass(Arrays.stream(characters)
                .map(c -> (int) c).collect(Collectors.toSet()));
    }

    /**
     * Creates a new character class including the supplied characters.
     *
     * @param characters the characters
     *
     * @return the character class
     */
    public static CharacterClass forChars(Integer... characters) {
        return new CharacterClass(Arrays.stream(characters).collect(Collectors.toSet()));
    }

    /**
     * Creates a new character class including the supplied characters.
     *
     * @param characters the characters
     *
     * @return the character class
     */
    public static CharacterClass forChars(Set<Integer> characters) {
        return new CharacterClass(characters);
    }

}
