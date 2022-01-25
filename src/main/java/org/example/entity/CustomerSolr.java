package org.example.entity;

import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;

@SolrDocument(collection = "search_all")
public class CustomerSolr {

    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Indexed(name = "name", type = "string")
    private String name;

    @Indexed(name = "mobile", type = "string")
    private String shortName;

    @Indexed(name = "user_name", type = "string")
    private String username;

}
