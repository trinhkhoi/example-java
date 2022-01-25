package org.example.repository.solr;

import org.springframework.data.solr.repository.SolrRepository;
import org.example.entity.CustomerSolr;

/**
 * repository company solr
 * @author nguyentienthanh
 */
public interface CustomerSolrRepository extends SolrRepository<CustomerSolr, String> {

//    @Query("stock_code:*?0*")
    CustomerSolr findByName(String name);

}

