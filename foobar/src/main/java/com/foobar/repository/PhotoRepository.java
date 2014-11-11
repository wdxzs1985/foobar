package com.foobar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import com.foobar.document.PhotoDocument;

@Repository
public interface PhotoRepository extends SolrCrudRepository<PhotoDocument, Integer> {

    public Page<PhotoDocument> findByUserId(Integer userId, Pageable pageable);

}
