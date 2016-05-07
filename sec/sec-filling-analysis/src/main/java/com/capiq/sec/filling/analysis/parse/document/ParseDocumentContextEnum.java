package com.capiq.sec.filling.analysis.parse.document;

public enum ParseDocumentContextEnum {
	IPARSE_STAT("parse_stat"),
	
	//document name
	FILE_NAME("file_name"),
	
	//is hdfs storage
	IS_HDFS_STORAGE("isHdfsStorage"),
	
	TOC_DEDUCED("toc_deduced"),
	MDA_EXTRACTED("mda_extracted"),
	
	//initial document representation
	MDA_DOCUMENT_PATH("mda_document_path"),
	RAW_DOCUMENT_PATH("raw_document_path"),
	TEMP_DEST_FILE_PATH("temp_dest_file_path"),
	TEMP_SOURCE_FILE_PATH("temp_source_file_path"),
	NON_BINARY_DOCUMENT_PATH("non_binary_document_path"),
	CLEAN_DOCUMENT_PATH("clean_document_path"),
	
	PAGE_DIVIDER_LENGTH("page_divider_length"),
	HYPHEN_PAGE_DIVIDER_LENGTH("hyphen_page_divider_length"),
    	
	// tag free - clean text - document representation 
	CLEAN_SEC_DOCUMENT_OBJECT("sec_document_object"),
    CLEAN_RAW_SEC_DOCUMENT_OBJECT("raw_sec_document_object"),
    
    //Document Marker
    ITEM7_MARKER("item7_marker"),
    ITEM8_MARKER("item8_marker"),
	JFLEX_MARKERS("jflex_markers"),
	
	//toc token list
	TOC_TOKEN_LIST("toc_token_list"),
	
	//parsed table of content data
	PARSED_TABLE_OF_CONTENT_DATA("parsed_table_of_content");
	
    private ParseDocumentContextEnum(final String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return this.value;
    }
}    