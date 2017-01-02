/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.test.api.query;

/**
 * Test cases for order by queries on String properties.
 *
 * @tck.config testroot path to node that accepts child nodes of type
 *   <code>nodetype</code>
 * @tck.config nodetype name of a node type
 * @tck.config nodename1 name of a child node of type <code>nodetype</code>
 * @tck.config nodename2 name of a child node of type <code>nodetype</code>
 * @tck.config nodename3 name of a child node of type <code>nodetype</code>
 * @tck.config nodename4 name of a child node of type <code>nodetype</code>
 * @tck.config propertyname1 name of a single value String property.
 *
 * @sources OrderByStringTest.java
 */
public class OrderByStringTest extends AbstractOrderByTest {

    /**
     * Tests order by queries with String properties.
     */
    public void testStringOrder() throws Exception {
        populate(new String[]{"aaaa", "cccc", "bbbb", "dddd"});
        checkOrder(new String[]{nodeName1, nodeName3, nodeName2, nodeName4});
    }

}
