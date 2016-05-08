package com.sec.filing.analysis.corpwatch.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Meta {
    public final long success;
    public final long status;
    public final String status_string;
    public final String total_results;
    public final long results_complete;
    public final String api_version;
    public final Parameters parameters;

    @JsonCreator
    public Meta(@JsonProperty("success") long success, @JsonProperty("status") long status, @JsonProperty("status_string") String status_string, @JsonProperty("total_results") String total_results, @JsonProperty("results_complete") long results_complete, @JsonProperty("api_version") String api_version, @JsonProperty("parameters") Parameters parameters){
        this.success = success;
        this.status = status;
        this.status_string = status_string;
        this.total_results = total_results;
        this.results_complete = results_complete;
        this.api_version = api_version;
        this.parameters = parameters;
    }

	public long getSuccess() {
		return success;
	}

	public long getStatus() {
		return status;
	}

	public String getStatus_string() {
		return status_string;
	}

	public String getTotal_results() {
		return total_results;
	}

	public long getResults_complete() {
		return results_complete;
	}

	public String getApi_version() {
		return api_version;
	}

	public Parameters getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "Meta [success=" + success + ", status=" + status
				+ ", status_string=" + status_string + ", total_results="
				+ total_results + ", results_complete=" + results_complete
				+ ", api_version=" + api_version + ", parameters=" + parameters
				+ "]";
	}
}
