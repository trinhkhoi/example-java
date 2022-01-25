package org.example.repository.solr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrRepository;
import org.example.entity.CompanyKeywordSolr;

import java.util.List;

/**
 * repository company solr
 * @author nguyentiennghia copy from nguyentienthanh :)
 */
public interface CompanyKeywordSolrRepository extends SolrRepository<CompanyKeywordSolr, String> {

    @Query("(code:?3* OR name:('?0') OR name:('?1') OR short_name:('?0') OR short_name:('?1') OR stock_exchange:('?0')) AND search_type:(?2) AND -stock_exchange: OTC ")
    Page<CompanyKeywordSolr> findByCustomQuery(String searchTermUpperCase, String searchTermRemoveAccent, String searchType, String searchAZ, Pageable pageable);

    List<CompanyKeywordSolr> findByCodeIsIn(List<String> codes);
}

