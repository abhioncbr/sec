package com.sec.filing.analysis.corpwatch.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Parameters {
	public final String cik;
	public final long limit;
    public final long index;
    public final String year;

    @JsonCreator
    public Parameters(@JsonProperty("cik") String cik, @JsonProperty("limit") long limit, @JsonProperty("index") long index, @JsonProperty("year") String year){
        this.limit = limit;
        this.index = index;
        this.year = year;
        this.cik = cik;
    }

	public String getCik() {
		return cik;
	}

	public long getLimit() {
		return limit;
	}

	public long getIndex() {
		return index;
	}

	public String getYear() {
		return year;
	}

	@Override
	public String toString() {
		return "Parameters [limit=" + limit + ", index=" + index + ", year="
				+ year + "]";
	}
}
