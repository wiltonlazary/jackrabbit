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
package org.apache.jackrabbit.webdav.version.report;

import org.apache.log4j.Logger;
import org.apache.jackrabbit.webdav.*;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.version.*;
import org.jdom.Document;

/**
 * <code>VersionTreeReport</code> encapsulates the DAV:version-tree report.
 * It describes the requested properties of all the versions in the version
 * history of a version. The DAV:version-tree report must be supported by all
 * version resources and all version-controlled resources.
 */
public class VersionTreeReport implements Report, DeltaVConstants {

    private static Logger log = Logger.getLogger(VersionTreeReport.class);

    private ReportInfo info;
    private DeltaVResource resource;

    public ReportType getType() {
        return ReportType.VERSION_TREE;
    }

    /**
     * Set the <code>DeltaVResource</code> used to register this report.
     *
     * @param resource
     * @throws IllegalArgumentException if the given resource is neither
     * {@link VersionControlledResource} nor {@link VersionResource}.
     * @see Report#setResource(org.apache.jackrabbit.webdav.version.DeltaVResource)
     */
    public void setResource(DeltaVResource resource) throws IllegalArgumentException {
        if (resource instanceof VersionControlledResource || resource instanceof VersionResource) {
            this.resource = resource;
        } else {
            throw new IllegalArgumentException("DAV:version-tree report can only be created for version-controlled resources and version resources.");
        }
    }

    /**
     * Set the <code>ReportInfo</code> as specified by the REPORT request body,
     * that defines the details for this report.
     *
     * @param info
     * @throws IllegalArgumentException if the given <code>ReportInfo</code>
     * does not contain a DAV:version-tree element.
     * @see Report#setInfo(ReportInfo)
     */
    public void setInfo(ReportInfo info) throws IllegalArgumentException {
        if (info == null || !XML_VERSION_TREE.equals(info.getReportElement().getName())) {
            throw new IllegalArgumentException("DAV:version-tree element expected.");
        }
        this.info = info;
    }

    /**
     * Runs the DAV:version-tree report.
     *
     * @return Xml <code>Document</code> representing the report in the required
     * format.
     * @throws DavException if the resource or the info field are <code>null</code>
     * or if any other error occurs.
     * @see Report#toXml()
     */
    public Document toXml() throws DavException {
        if (info == null || resource == null) {
            throw new DavException(DavServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while running DAV:version-tree report");
        }

        MultiStatus ms = new MultiStatus();
        buildResponse(resource, info.getPropertyNameSet(), info.getDepth(), ms);
        return ms.toXml();
    }

    /**
     *
     * @param res
     * @param propNameSet
     * @param depth
     * @param ms
     * @throws DavException
     */
    private void buildResponse(DavResource res, DavPropertyNameSet propNameSet,
                               int depth, MultiStatus ms) throws DavException {
        VersionResource[] versions = getVersions(res);
        for (int i = 0; i < versions.length; i++) {
            if (propNameSet.isEmpty()) {
                ms.addResourceStatus(versions[i], DavServletResponse.SC_OK, 0);
            } else {
                ms.addResourceProperties(versions[i], propNameSet, 0);
            }
        }
        if (depth > 0) {
            DavResourceIterator it = res.getMembers();
            while (it.hasNext()) {
                buildResponse(it.nextResource(), propNameSet, depth-1, ms);
            }
        }
    }

    /**
     * Retrieve all versions from the version history associated with the given
     * resource. If the versions cannot be retrieved from the given resource
     * an exception is thrown.
     *
     * @param res
     * @return array of {@link VersionResource}s or an empty array if the versions
     * could not be retrieved.
     * @throws DavException if the version history could not be retrieved from
     * the given resource or if an error occurs while accessing the versions
     * from the version history resource.
     */
    private static VersionResource[] getVersions(DavResource res) throws DavException {
        VersionResource[] versions = new VersionResource[0];
        if (res instanceof VersionControlledResource) {
            versions = ((VersionControlledResource)res).getVersionHistory().getVersions();
        } else if (res instanceof VersionResource) {
            versions = ((VersionResource)res).getVersionHistory().getVersions();
        }
        return versions;
    }
}