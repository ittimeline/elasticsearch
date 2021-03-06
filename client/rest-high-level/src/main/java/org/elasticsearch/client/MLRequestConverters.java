/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.client.RequestConverters.EndpointBuilder;
import org.elasticsearch.client.ml.CloseJobRequest;
import org.elasticsearch.client.ml.DeleteCalendarJobRequest;
import org.elasticsearch.client.ml.DeleteCalendarRequest;
import org.elasticsearch.client.ml.DeleteDatafeedRequest;
import org.elasticsearch.client.ml.DeleteFilterRequest;
import org.elasticsearch.client.ml.DeleteForecastRequest;
import org.elasticsearch.client.ml.DeleteJobRequest;
import org.elasticsearch.client.ml.DeleteModelSnapshotRequest;
import org.elasticsearch.client.ml.FlushJobRequest;
import org.elasticsearch.client.ml.ForecastJobRequest;
import org.elasticsearch.client.ml.GetBucketsRequest;
import org.elasticsearch.client.ml.GetCalendarEventsRequest;
import org.elasticsearch.client.ml.GetCalendarsRequest;
import org.elasticsearch.client.ml.GetCategoriesRequest;
import org.elasticsearch.client.ml.GetDatafeedRequest;
import org.elasticsearch.client.ml.GetDatafeedStatsRequest;
import org.elasticsearch.client.ml.GetFiltersRequest;
import org.elasticsearch.client.ml.GetInfluencersRequest;
import org.elasticsearch.client.ml.GetJobRequest;
import org.elasticsearch.client.ml.GetJobStatsRequest;
import org.elasticsearch.client.ml.GetModelSnapshotsRequest;
import org.elasticsearch.client.ml.GetOverallBucketsRequest;
import org.elasticsearch.client.ml.GetRecordsRequest;
import org.elasticsearch.client.ml.OpenJobRequest;
import org.elasticsearch.client.ml.PostCalendarEventRequest;
import org.elasticsearch.client.ml.PostDataRequest;
import org.elasticsearch.client.ml.PreviewDatafeedRequest;
import org.elasticsearch.client.ml.PutCalendarJobRequest;
import org.elasticsearch.client.ml.PutCalendarRequest;
import org.elasticsearch.client.ml.PutDatafeedRequest;
import org.elasticsearch.client.ml.PutFilterRequest;
import org.elasticsearch.client.ml.PutJobRequest;
import org.elasticsearch.client.ml.RevertModelSnapshotRequest;
import org.elasticsearch.client.ml.StartDatafeedRequest;
import org.elasticsearch.client.ml.StopDatafeedRequest;
import org.elasticsearch.client.ml.UpdateDatafeedRequest;
import org.elasticsearch.client.ml.UpdateFilterRequest;
import org.elasticsearch.client.ml.UpdateJobRequest;
import org.elasticsearch.client.ml.UpdateModelSnapshotRequest;
import org.elasticsearch.client.ml.job.util.PageParams;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;

import java.io.IOException;

import static org.elasticsearch.client.RequestConverters.REQUEST_BODY_CONTENT_TYPE;
import static org.elasticsearch.client.RequestConverters.createContentType;
import static org.elasticsearch.client.RequestConverters.createEntity;

final class MLRequestConverters {

    private MLRequestConverters() {}

    static Request putJob(PutJobRequest putJobRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(putJobRequest.getJob().getId())
                .build();
        Request request = new Request(HttpPut.METHOD_NAME, endpoint);
        request.setEntity(createEntity(putJobRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getJob(GetJobRequest getJobRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(Strings.collectionToCommaDelimitedString(getJobRequest.getJobIds()))
            .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);

        RequestConverters.Params params = new RequestConverters.Params(request);
        if (getJobRequest.getAllowNoJobs() != null) {
            params.putParam("allow_no_jobs", Boolean.toString(getJobRequest.getAllowNoJobs()));
        }

        return request;
    }

    static Request getJobStats(GetJobStatsRequest getJobStatsRequest) {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(Strings.collectionToCommaDelimitedString(getJobStatsRequest.getJobIds()))
                .addPathPartAsIs("_stats")
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);

        RequestConverters.Params params = new RequestConverters.Params(request);
        if (getJobStatsRequest.getAllowNoJobs() != null) {
            params.putParam("allow_no_jobs", Boolean.toString(getJobStatsRequest.getAllowNoJobs()));
        }
        return request;
    }

    static Request openJob(OpenJobRequest openJobRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(openJobRequest.getJobId())
                .addPathPartAsIs("_open")
                .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(openJobRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request closeJob(CloseJobRequest closeJobRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(Strings.collectionToCommaDelimitedString(closeJobRequest.getJobIds()))
            .addPathPartAsIs("_close")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(closeJobRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request deleteJob(DeleteJobRequest deleteJobRequest) {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(deleteJobRequest.getJobId())
                .build();
        Request request = new Request(HttpDelete.METHOD_NAME, endpoint);

        RequestConverters.Params params = new RequestConverters.Params(request);
        if (deleteJobRequest.getForce() != null) {
            params.putParam("force", Boolean.toString(deleteJobRequest.getForce()));
        }
        if (deleteJobRequest.getWaitForCompletion() != null) {
            params.putParam("wait_for_completion", Boolean.toString(deleteJobRequest.getWaitForCompletion()));
        }

        return request;
    }

    static Request flushJob(FlushJobRequest flushJobRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(flushJobRequest.getJobId())
                .addPathPartAsIs("_flush")
                .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(flushJobRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request forecastJob(ForecastJobRequest forecastJobRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(forecastJobRequest.getJobId())
            .addPathPartAsIs("_forecast")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(forecastJobRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request updateJob(UpdateJobRequest updateJobRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(updateJobRequest.getJobUpdate().getJobId())
                .addPathPartAsIs("_update")
                .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(updateJobRequest.getJobUpdate(), REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request putDatafeed(PutDatafeedRequest putDatafeedRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("datafeeds")
                .addPathPart(putDatafeedRequest.getDatafeed().getId())
                .build();
        Request request = new Request(HttpPut.METHOD_NAME, endpoint);
        request.setEntity(createEntity(putDatafeedRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request updateDatafeed(UpdateDatafeedRequest updateDatafeedRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("datafeeds")
            .addPathPart(updateDatafeedRequest.getDatafeedUpdate().getId())
            .addPathPartAsIs("_update")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(updateDatafeedRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getDatafeed(GetDatafeedRequest getDatafeedRequest) {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("datafeeds")
                .addPathPart(Strings.collectionToCommaDelimitedString(getDatafeedRequest.getDatafeedIds()))
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);

        RequestConverters.Params params = new RequestConverters.Params(request);
        if (getDatafeedRequest.getAllowNoDatafeeds() != null) {
            params.putParam(GetDatafeedRequest.ALLOW_NO_DATAFEEDS.getPreferredName(),
                    Boolean.toString(getDatafeedRequest.getAllowNoDatafeeds()));
        }

        return request;
    }

    static Request deleteDatafeed(DeleteDatafeedRequest deleteDatafeedRequest) {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("datafeeds")
                .addPathPart(deleteDatafeedRequest.getDatafeedId())
                .build();
        Request request = new Request(HttpDelete.METHOD_NAME, endpoint);
        RequestConverters.Params params = new RequestConverters.Params(request);
        if (deleteDatafeedRequest.getForce() != null) {
            params.putParam("force", Boolean.toString(deleteDatafeedRequest.getForce()));
        }
        return request;
    }

    static Request startDatafeed(StartDatafeedRequest startDatafeedRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("datafeeds")
            .addPathPart(startDatafeedRequest.getDatafeedId())
            .addPathPartAsIs("_start")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(startDatafeedRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request stopDatafeed(StopDatafeedRequest stopDatafeedRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("datafeeds")
            .addPathPart(Strings.collectionToCommaDelimitedString(stopDatafeedRequest.getDatafeedIds()))
            .addPathPartAsIs("_stop")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(stopDatafeedRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getDatafeedStats(GetDatafeedStatsRequest getDatafeedStatsRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("datafeeds")
            .addPathPart(Strings.collectionToCommaDelimitedString(getDatafeedStatsRequest.getDatafeedIds()))
            .addPathPartAsIs("_stats")
            .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);

        RequestConverters.Params params = new RequestConverters.Params(request);
        if (getDatafeedStatsRequest.getAllowNoDatafeeds() != null) {
            params.putParam("allow_no_datafeeds", Boolean.toString(getDatafeedStatsRequest.getAllowNoDatafeeds()));
        }
        return request;
    }

    static Request previewDatafeed(PreviewDatafeedRequest previewDatafeedRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("datafeeds")
            .addPathPart(previewDatafeedRequest.getDatafeedId())
            .addPathPartAsIs("_preview")
            .build();
        return new Request(HttpGet.METHOD_NAME, endpoint);
    }

    static Request deleteForecast(DeleteForecastRequest deleteForecastRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(deleteForecastRequest.getJobId())
            .addPathPartAsIs("_forecast")
            .addPathPart(Strings.collectionToCommaDelimitedString(deleteForecastRequest.getForecastIds()))
            .build();
        Request request = new Request(HttpDelete.METHOD_NAME, endpoint);
        RequestConverters.Params params = new RequestConverters.Params(request);
        if (deleteForecastRequest.getAllowNoForecasts() != null) {
            params.putParam("allow_no_forecasts", Boolean.toString(deleteForecastRequest.getAllowNoForecasts()));
        }
        if (deleteForecastRequest.timeout() != null) {
            params.putParam("timeout", deleteForecastRequest.timeout().getStringRep());
        }
        return request;
    }

    static Request deleteModelSnapshot(DeleteModelSnapshotRequest deleteModelSnapshotRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(deleteModelSnapshotRequest.getJobId())
            .addPathPartAsIs("model_snapshots")
            .addPathPart(deleteModelSnapshotRequest.getSnapshotId())
            .build();
        return new Request(HttpDelete.METHOD_NAME, endpoint);
    }

    static Request getBuckets(GetBucketsRequest getBucketsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(getBucketsRequest.getJobId())
                .addPathPartAsIs("results")
                .addPathPartAsIs("buckets")
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getBucketsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getCategories(GetCategoriesRequest getCategoriesRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(getCategoriesRequest.getJobId())
            .addPathPartAsIs("results")
            .addPathPartAsIs("categories")
            .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getCategoriesRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getModelSnapshots(GetModelSnapshotsRequest getModelSnapshotsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(getModelSnapshotsRequest.getJobId())
            .addPathPartAsIs("model_snapshots")
            .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getModelSnapshotsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request updateModelSnapshot(UpdateModelSnapshotRequest updateModelSnapshotRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(updateModelSnapshotRequest.getJobId())
            .addPathPartAsIs("model_snapshots")
            .addPathPart(updateModelSnapshotRequest.getSnapshotId())
            .addPathPartAsIs("_update")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(updateModelSnapshotRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request revertModelSnapshot(RevertModelSnapshotRequest revertModelSnapshotsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(revertModelSnapshotsRequest.getJobId())
            .addPathPartAsIs("model_snapshots")
            .addPathPart(revertModelSnapshotsRequest.getSnapshotId())
            .addPathPart("_revert")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(revertModelSnapshotsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getOverallBuckets(GetOverallBucketsRequest getOverallBucketsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(Strings.collectionToCommaDelimitedString(getOverallBucketsRequest.getJobIds()))
                .addPathPartAsIs("results")
                .addPathPartAsIs("overall_buckets")
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getOverallBucketsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getRecords(GetRecordsRequest getRecordsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(getRecordsRequest.getJobId())
                .addPathPartAsIs("results")
                .addPathPartAsIs("records")
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getRecordsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request postData(PostDataRequest postDataRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("anomaly_detectors")
            .addPathPart(postDataRequest.getJobId())
            .addPathPartAsIs("_data")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);

        RequestConverters.Params params = new RequestConverters.Params(request);
        if (postDataRequest.getResetStart() != null) {
            params.putParam(PostDataRequest.RESET_START.getPreferredName(), postDataRequest.getResetStart());
        }
        if (postDataRequest.getResetEnd() != null) {
            params.putParam(PostDataRequest.RESET_END.getPreferredName(), postDataRequest.getResetEnd());
        }
        BytesReference content = postDataRequest.getContent();
        if (content != null) {
            BytesRef source = postDataRequest.getContent().toBytesRef();
            HttpEntity byteEntity = new ByteArrayEntity(source.bytes,
                source.offset,
                source.length,
                createContentType(postDataRequest.getXContentType()));
            request.setEntity(byteEntity);
        }
        return request;
    }

    static Request getInfluencers(GetInfluencersRequest getInfluencersRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("anomaly_detectors")
                .addPathPart(getInfluencersRequest.getJobId())
                .addPathPartAsIs("results")
                .addPathPartAsIs("influencers")
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getInfluencersRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request putCalendar(PutCalendarRequest putCalendarRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("calendars")
                .addPathPart(putCalendarRequest.getCalendar().getId())
                .build();
        Request request = new Request(HttpPut.METHOD_NAME, endpoint);
        request.setEntity(createEntity(putCalendarRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getCalendars(GetCalendarsRequest getCalendarsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("calendars")
                .addPathPart(getCalendarsRequest.getCalendarId())
                .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getCalendarsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request putCalendarJob(PutCalendarJobRequest putCalendarJobRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("calendars")
            .addPathPart(putCalendarJobRequest.getCalendarId())
            .addPathPartAsIs("jobs")
            .addPathPart(Strings.collectionToCommaDelimitedString(putCalendarJobRequest.getJobIds()))
            .build();
        return new Request(HttpPut.METHOD_NAME, endpoint);
    }

    static Request deleteCalendarJob(DeleteCalendarJobRequest deleteCalendarJobRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("calendars")
            .addPathPart(deleteCalendarJobRequest.getCalendarId())
            .addPathPartAsIs("jobs")
            .addPathPart(Strings.collectionToCommaDelimitedString(deleteCalendarJobRequest.getJobIds()))
            .build();
        return new Request(HttpDelete.METHOD_NAME, endpoint);
    }

    static Request deleteCalendar(DeleteCalendarRequest deleteCalendarRequest) {
        String endpoint = new EndpointBuilder()
                .addPathPartAsIs("_xpack")
                .addPathPartAsIs("ml")
                .addPathPartAsIs("calendars")
                .addPathPart(deleteCalendarRequest.getCalendarId())
                .build();
        Request request = new Request(HttpDelete.METHOD_NAME, endpoint);
        return request;
    }

    static Request getCalendarEvents(GetCalendarEventsRequest getCalendarEventsRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("calendars")
            .addPathPart(getCalendarEventsRequest.getCalendarId())
            .addPathPartAsIs("events")
            .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        request.setEntity(createEntity(getCalendarEventsRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request postCalendarEvents(PostCalendarEventRequest postCalendarEventRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("calendars")
            .addPathPart(postCalendarEventRequest.getCalendarId())
            .addPathPartAsIs("events")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(postCalendarEventRequest,
            REQUEST_BODY_CONTENT_TYPE,
            PostCalendarEventRequest.EXCLUDE_CALENDAR_ID_PARAMS));
        return request;
    }

    static Request putFilter(PutFilterRequest putFilterRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("filters")
            .addPathPart(putFilterRequest.getMlFilter().getId())
            .build();
        Request request = new Request(HttpPut.METHOD_NAME, endpoint);
        request.setEntity(createEntity(putFilterRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request getFilter(GetFiltersRequest getFiltersRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("filters")
            .addPathPart(getFiltersRequest.getFilterId())
            .build();
        Request request = new Request(HttpGet.METHOD_NAME, endpoint);
        RequestConverters.Params params = new RequestConverters.Params(request);
        if (getFiltersRequest.getSize() != null) {
            params.putParam(PageParams.SIZE.getPreferredName(), getFiltersRequest.getSize().toString());
        }
        if (getFiltersRequest.getFrom() != null) {
            params.putParam(PageParams.FROM.getPreferredName(), getFiltersRequest.getFrom().toString());
        }
        return request;
    }

    static Request updateFilter(UpdateFilterRequest updateFilterRequest) throws IOException {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack")
            .addPathPartAsIs("ml")
            .addPathPartAsIs("filters")
            .addPathPart(updateFilterRequest.getFilterId())
            .addPathPartAsIs("_update")
            .build();
        Request request = new Request(HttpPost.METHOD_NAME, endpoint);
        request.setEntity(createEntity(updateFilterRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request deleteFilter(DeleteFilterRequest deleteFilterRequest) {
        String endpoint = new EndpointBuilder()
            .addPathPartAsIs("_xpack", "ml", "filters")
            .addPathPart(deleteFilterRequest.getId())
            .build();
        Request request = new Request(HttpDelete.METHOD_NAME, endpoint);
        return request;
    }
}
