package com.yxc.repository.search;

import com.yxc.domain.Userinfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Userinfo entity.
 */
public interface UserinfoSearchRepository extends ElasticsearchRepository<Userinfo, Long> {
}
