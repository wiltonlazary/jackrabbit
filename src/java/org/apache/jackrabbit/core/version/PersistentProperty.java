/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.jackrabbit.core.version;

import org.apache.jackrabbit.core.InternalValue;
import org.apache.jackrabbit.core.QName;
import org.apache.jackrabbit.core.state.PropertyState;

/**
 * This Class represents a property that was frozen during a
 * {@link javax.jcr.Node#checkin()}
 */
public class PersistentProperty {

    /**
     * the underlaying persistent state
     */
    private final PropertyState state;

    /**
     * flag if this is a multivalued propertery
     */
    private final boolean isMultiple;

    /**
     * Creates a new persistent property
     *
     * @param state
     */
    public PersistentProperty(PropertyState state, boolean isMultiple) {
        this.state = state;
        this.isMultiple = isMultiple;
    }

    /**
     * returns the name of the property
     *
     * @return
     */
    public QName getName() {
        return state.getName();
    }

    /**
     * returns the values of the property
     *
     * @return
     */
    public InternalValue[] getValues() {
        return state.getValues();
    }

    /**
     * returns the type of the property
     *
     * @return
     */
    public int getType() {
        return state.getType();
    }

    /**
     * returns <code>true</code> if this is a multivalue propererty
     * @return
     */
    public boolean isMultiple() {
        return isMultiple;
    }
}
