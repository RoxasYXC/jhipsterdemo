package com.yxc.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.yxc.domain.Userinfo;
import com.yxc.repository.UserinfoRepository;
import com.yxc.repository.search.UserinfoSearchRepository;
import com.yxc.web.rest.errors.BadRequestAlertException;
import com.yxc.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Userinfo.
 */
@RestController
@RequestMapping("/api")
public class UserinfoResource {

    private final Logger log = LoggerFactory.getLogger(UserinfoResource.class);

    private static final String ENTITY_NAME = "userinfo";

    private final UserinfoRepository userinfoRepository;

    private final UserinfoSearchRepository userinfoSearchRepository;

    public UserinfoResource(UserinfoRepository userinfoRepository, UserinfoSearchRepository userinfoSearchRepository) {
        this.userinfoRepository = userinfoRepository;
        this.userinfoSearchRepository = userinfoSearchRepository;
    }

    /**
     * POST  /userinfos : Create a new userinfo.
     *
     * @param userinfo the userinfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userinfo, or with status 400 (Bad Request) if the userinfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/userinfos")
    @Timed
    public ResponseEntity<Userinfo> createUserinfo(@RequestBody Userinfo userinfo) throws URISyntaxException {
        log.debug("REST request to save Userinfo : {}", userinfo);
        if (userinfo.getId() != null) {
            throw new BadRequestAlertException("A new userinfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Userinfo result = userinfoRepository.save(userinfo);
        userinfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userinfos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userinfos : Updates an existing userinfo.
     *
     * @param userinfo the userinfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userinfo,
     * or with status 400 (Bad Request) if the userinfo is not valid,
     * or with status 500 (Internal Server Error) if the userinfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/userinfos")
    @Timed
    public ResponseEntity<Userinfo> updateUserinfo(@RequestBody Userinfo userinfo) throws URISyntaxException {
        log.debug("REST request to update Userinfo : {}", userinfo);
        if (userinfo.getId() == null) {
            return createUserinfo(userinfo);
        }
        Userinfo result = userinfoRepository.save(userinfo);
        userinfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userinfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userinfos : get all the userinfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userinfos in body
     */
    @GetMapping("/userinfos")
    @Timed
    public List<Userinfo> getAllUserinfos() {
        log.debug("REST request to get all Userinfos");
        Sort sort = new Sort(Sort.Direction.DESC, "id").and (new Sort(Sort.Direction.DESC, "name"));
        return userinfoRepository.findAll(sort);
        }

    /**
     * GET  /userinfos/:id : get the "id" userinfo.
     *
     * @param id the id of the userinfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userinfo, or with status 404 (Not Found)
     */
    @GetMapping("/userinfos/{id}")
    @Timed
    public ResponseEntity<Userinfo> getUserinfo(@PathVariable Long id) {
        log.debug("REST request to get Userinfo : {}", id);
        Userinfo userinfo = userinfoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userinfo));
    }

    /**
     * DELETE  /userinfos/:id : delete the "id" userinfo.
     *
     * @param id the id of the userinfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/userinfos/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserinfo(@PathVariable Long id) {
        log.debug("REST request to delete Userinfo : {}", id);
        userinfoRepository.delete(id);
        userinfoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/userinfos?query=:query : search for the userinfo corresponding
     * to the query.
     *
     * @param query the query of the userinfo search
     * @return the result of the search
     */
    @GetMapping("/_search/userinfos")
    @Timed
    public List<Userinfo> searchUserinfos(@RequestParam String query) {
        log.debug("REST request to search Userinfos for query {}", query);
        return StreamSupport
            .stream(userinfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
