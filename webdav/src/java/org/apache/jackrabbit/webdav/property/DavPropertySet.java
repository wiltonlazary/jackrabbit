/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.webdav.property;

import org.jdom.Namespace;
import org.apache.jackrabbit.webdav.DavConstants;

import java.util.*;

/**
 * The <code>DavPropertySet</code> class represents a set of WebDAV
 * property.
 */
public class DavPropertySet {

    /**
     * the set of property
     */
    private final HashMap map = new HashMap();

    /**
     * Adds a new property to this set.
     *
     * @param property The property to add
     *
     * @return The previously assigned property or <code>null</code>.
     */
    public DavProperty add(DavProperty property) {
        return (DavProperty) map.put(property.getName(), property);
    }

    /**
     *
     * @param pset Properties to add
     */
    public void addAll(DavPropertySet pset) {
	map.putAll(pset.map);
    }

    /**
     * Retrieves the property with the specified <code>name</code> and the
     * default WebDAV {@link org.apache.jackrabbit.webdav.DavConstants#NAMESPACE namespace}.
     *
     * @param name The name of the property to retrieve
     *
     * @return The desired property or <code>null</code>
     */
    public DavProperty get(String name) {
    	return get(DavPropertyName.create(name));
    }

    /**
     * Retrieves the property with the specified <code>name</code> and
     * <code>namespace</code>.
     *
     * @param name The name of the property to retrieve
     * @param namespace The namespace of the property to retrieve
     *
     * @return The desired property or <code>null</code>
     */
    public DavProperty get(String name, Namespace namespace) {
        return get(DavPropertyName.create(name, namespace));
    }

    /**
     * Retrieves the property with the specified <code>name</code>
     *
     * @param name The webdav property name of the property to retrieve
     *
     * @return The desired property or <code>null</code>
     */
    public DavProperty get(DavPropertyName name) {
    	return (DavProperty) map.get(name);
    }


    /**
     * Removes the indicated property from this set.
     *
     * @param name The webdav property name to remove
     *
     * @return The removed property or <code>null</code>
     */
    public DavProperty remove(DavPropertyName name) {
        return (DavProperty) map.remove(name);
    }

    /**
     * Removes the property with the specified <code>name</code> and the
     * default WebDAV {@link org.apache.jackrabbit.webdav.DavConstants#NAMESPACE namespace}.
     *
     * @param name The name of the property to remove
     *
     * @return The removed property or <code>null</code>
     */
    public DavProperty remove(String name) {
        return remove(DavPropertyName.create(name));
    }

    /**
     * Removes the property with the specified <code>name</code> and
     * <code>namespace</code> from this set.
     *
     * @param name The name of the property to remove
     * @param namespace The namespace of the property to remove
     *
     * @return The removed property or <code>null</code>
     */
    public DavProperty remove(String name, Namespace namespace) {
        return remove(DavPropertyName.create(name, namespace));
    }

    /**
     * Returns an iterator over all property in this set.
     *
     * @return An iterator over {@link DavProperty}.
     */
    public DavPropertyIterator iterator() {
        return new PropIter();
    }

    /**
     * Returns an iterator over all those property in this set, that have the
     * indicated <code>namespace</code>.
     *
     * @param namespace The namespace of the property in the iteration.
     *
     * @return An iterator over {@link DavProperty}.
     */
    public DavPropertyIterator iterator(Namespace namespace) {
        return new PropIter(namespace);
    }

    /**
     * Checks if this set contains the property with the specified name.
     *
     * @param name The name of the property
     *
     * @return <code>true</code> if this set contains the property;
     *         <code>false</code> otherwise.
     */
    public boolean contains(DavPropertyName name) {
        return map.containsKey(name);
    }

    /**
     * Checks if this set contains the property with the specified name and the
     * default WebDAV {@link org.apache.jackrabbit.webdav.DavConstants#NAMESPACE namespace}.
     *
     * @param name The name of the property
     *
     * @return <code>true</code> if this set contains the property;
     *         <code>false</code> otherwise.
     */
    public boolean contains(String name) {
        return contains(DavPropertyName.create(name, DavConstants.NAMESPACE));
    }

    /**
     * Return true if this property set is empty.
     *
     * @return true if the internal map contains no elements.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Return the names of all properties present in this set.
     *
     * @return array of {@link DavPropertyName property names} present in this set.
     */
    public DavPropertyName[] getPropertyNames() {
        Set keySet = map.keySet();
	return (DavPropertyName[]) keySet.toArray(new DavPropertyName[keySet.size()]);
    }

    //---------------------------------------------------------- Inner class ---
    /**
     * Implementation of a DavPropertyIterator that returns webdav property.
     * Additionally, it can only return property with the given namespace.
     */
    private class PropIter implements DavPropertyIterator {

        /** the namespace to match agains */
        private final Namespace namespace;

        /** the internal iterator */
        private final Iterator iterator;

        /** the next property to return */
        private DavProperty next;

        /**
         * Creates a new property iterator.
         */
        private PropIter() {
            this(null);
        }

        /**
         * Creates a new iterator with the given namespace
         * @param namespace The namespace to match against
         */
	private PropIter(Namespace namespace) {
            this.namespace = namespace;
            iterator = map.values().iterator();
            seek();
        }

        /**
         * @see DavPropertyIterator#nextProperty();
         */
        public DavProperty nextProperty() throws NoSuchElementException {
            if (next==null) {
                throw new NoSuchElementException();
            }
            DavProperty ret = next;
            seek();
            return ret;
        }

        /**
         * @see DavPropertyIterator#hasNext();
         */
        public boolean hasNext() {
            return next!=null;
        }

        /**
         * @see DavPropertyIterator#next();
         */
        public Object next() {
            return nextProperty();
        }

        /**
         * @see DavPropertyIterator#remove();
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Seeks for the next valid property
         */
        private void seek() {
            while (iterator.hasNext()) {
                next = (DavProperty) iterator.next();
                if (namespace == null || namespace.equals(next.getName().getNamespace())) {
                    return;
                }
            }
            next = null;
        }
    }
}

