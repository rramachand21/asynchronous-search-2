/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 *
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */
/*
 *   Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.opensearch.search.asynchronous.response;

import org.opensearch.action.ActionResponse;
import org.opensearch.common.ParseField;
import org.opensearch.common.io.stream.StreamInput;
import org.opensearch.common.io.stream.StreamOutput;
import org.opensearch.common.xcontent.ConstructingObjectParser;
import org.opensearch.common.xcontent.ObjectParser;
import org.opensearch.common.xcontent.StatusToXContentObject;
import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.rest.RestStatus;

import java.io.IOException;
import java.util.Objects;

import static org.opensearch.common.xcontent.ConstructingObjectParser.constructorArg;
import static org.opensearch.rest.RestStatus.NOT_FOUND;
import static org.opensearch.rest.RestStatus.OK;

/**
 * A response that indicates that a request has been acknowledged
 */
public class AcknowledgedResponse extends ActionResponse implements StatusToXContentObject {

    private static final ParseField ACKNOWLEDGED = new ParseField("acknowledged");

    protected boolean acknowledged;

    public AcknowledgedResponse(StreamInput in) throws IOException {
        super(in);
        acknowledged = in.readBoolean();
    }

    public AcknowledgedResponse(StreamInput in, boolean readAcknowledged) throws IOException {
        super(in);
        if (readAcknowledged) {
            acknowledged = in.readBoolean();
        }
    }

    public AcknowledgedResponse(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    /**
     * Returns whether the response is acknowledged or not
     *
     * @return true if the response is acknowledged, false otherwise
     */
    public final boolean isAcknowledged() {
        return acknowledged;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeBoolean(acknowledged);
    }

    @Override
    public final XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(ACKNOWLEDGED.getPreferredName(), isAcknowledged());
        addCustomFields(builder, params);
        builder.endObject();
        return builder;
    }

    protected void addCustomFields(XContentBuilder builder, Params params) throws IOException {

    }

    /**
     * A generic parser that simply parses the acknowledged flag
     */
    private static final ConstructingObjectParser<Boolean, Void> ACKNOWLEDGED_FLAG_PARSER = new ConstructingObjectParser<>(
            "acknowledged_flag", true, args -> (Boolean) args[0]);

    static {
        ACKNOWLEDGED_FLAG_PARSER.declareField(constructorArg(), (parser, context) -> parser.booleanValue(), ACKNOWLEDGED,
                ObjectParser.ValueType.BOOLEAN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcknowledgedResponse that = (AcknowledgedResponse) o;
        return isAcknowledged() == that.isAcknowledged();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAcknowledged());
    }

    @Override
    public RestStatus status() {
        return acknowledged ? OK : NOT_FOUND;
    }
}
