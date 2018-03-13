package com.yxc.web.rest;

import com.yxc.TestApp;

import com.yxc.domain.Userinfo;
import com.yxc.repository.UserinfoRepository;
import com.yxc.repository.search.UserinfoSearchRepository;
import com.yxc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.yxc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserinfoResource REST controller.
 *
 * @see UserinfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
public class UserinfoResourceIntTest {

    private static final String DEFAULT_LOGINID = "AAAAAAAAAA";
    private static final String UPDATED_LOGINID = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UserinfoRepository userinfoRepository;

    @Autowired
    private UserinfoSearchRepository userinfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserinfoMockMvc;

    private Userinfo userinfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserinfoResource userinfoResource = new UserinfoResource(userinfoRepository, userinfoSearchRepository);
        this.restUserinfoMockMvc = MockMvcBuilders.standaloneSetup(userinfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Userinfo createEntity(EntityManager em) {
        Userinfo userinfo = new Userinfo()
            .loginid(DEFAULT_LOGINID)
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME);
        return userinfo;
    }

    @Before
    public void initTest() {
        userinfoSearchRepository.deleteAll();
        userinfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserinfo() throws Exception {
        int databaseSizeBeforeCreate = userinfoRepository.findAll().size();

        // Create the Userinfo
        restUserinfoMockMvc.perform(post("/api/userinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userinfo)))
            .andExpect(status().isCreated());

        // Validate the Userinfo in the database
        List<Userinfo> userinfoList = userinfoRepository.findAll();
        assertThat(userinfoList).hasSize(databaseSizeBeforeCreate + 1);
        Userinfo testUserinfo = userinfoList.get(userinfoList.size() - 1);
        assertThat(testUserinfo.getLoginid()).isEqualTo(DEFAULT_LOGINID);
        assertThat(testUserinfo.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserinfo.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Userinfo in Elasticsearch
        Userinfo userinfoEs = userinfoSearchRepository.findOne(testUserinfo.getId());
        assertThat(userinfoEs).isEqualToIgnoringGivenFields(testUserinfo);
    }

    @Test
    @Transactional
    public void createUserinfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userinfoRepository.findAll().size();

        // Create the Userinfo with an existing ID
        userinfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserinfoMockMvc.perform(post("/api/userinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userinfo)))
            .andExpect(status().isBadRequest());

        // Validate the Userinfo in the database
        List<Userinfo> userinfoList = userinfoRepository.findAll();
        assertThat(userinfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserinfos() throws Exception {
        // Initialize the database
        userinfoRepository.saveAndFlush(userinfo);

        // Get all the userinfoList
        restUserinfoMockMvc.perform(get("/api/userinfos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userinfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginid").value(hasItem(DEFAULT_LOGINID.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUserinfo() throws Exception {
        // Initialize the database
        userinfoRepository.saveAndFlush(userinfo);

        // Get the userinfo
        restUserinfoMockMvc.perform(get("/api/userinfos/{id}", userinfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userinfo.getId().intValue()))
            .andExpect(jsonPath("$.loginid").value(DEFAULT_LOGINID.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserinfo() throws Exception {
        // Get the userinfo
        restUserinfoMockMvc.perform(get("/api/userinfos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserinfo() throws Exception {
        // Initialize the database
        userinfoRepository.saveAndFlush(userinfo);
        userinfoSearchRepository.save(userinfo);
        int databaseSizeBeforeUpdate = userinfoRepository.findAll().size();

        // Update the userinfo
        Userinfo updatedUserinfo = userinfoRepository.findOne(userinfo.getId());
        // Disconnect from session so that the updates on updatedUserinfo are not directly saved in db
        em.detach(updatedUserinfo);
        updatedUserinfo
            .loginid(UPDATED_LOGINID)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME);

        restUserinfoMockMvc.perform(put("/api/userinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserinfo)))
            .andExpect(status().isOk());

        // Validate the Userinfo in the database
        List<Userinfo> userinfoList = userinfoRepository.findAll();
        assertThat(userinfoList).hasSize(databaseSizeBeforeUpdate);
        Userinfo testUserinfo = userinfoList.get(userinfoList.size() - 1);
        assertThat(testUserinfo.getLoginid()).isEqualTo(UPDATED_LOGINID);
        assertThat(testUserinfo.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserinfo.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Userinfo in Elasticsearch
        Userinfo userinfoEs = userinfoSearchRepository.findOne(testUserinfo.getId());
        assertThat(userinfoEs).isEqualToIgnoringGivenFields(testUserinfo);
    }

    @Test
    @Transactional
    public void updateNonExistingUserinfo() throws Exception {
        int databaseSizeBeforeUpdate = userinfoRepository.findAll().size();

        // Create the Userinfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserinfoMockMvc.perform(put("/api/userinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userinfo)))
            .andExpect(status().isCreated());

        // Validate the Userinfo in the database
        List<Userinfo> userinfoList = userinfoRepository.findAll();
        assertThat(userinfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserinfo() throws Exception {
        // Initialize the database
        userinfoRepository.saveAndFlush(userinfo);
        userinfoSearchRepository.save(userinfo);
        int databaseSizeBeforeDelete = userinfoRepository.findAll().size();

        // Get the userinfo
        restUserinfoMockMvc.perform(delete("/api/userinfos/{id}", userinfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userinfoExistsInEs = userinfoSearchRepository.exists(userinfo.getId());
        assertThat(userinfoExistsInEs).isFalse();

        // Validate the database is empty
        List<Userinfo> userinfoList = userinfoRepository.findAll();
        assertThat(userinfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserinfo() throws Exception {
        // Initialize the database
        userinfoRepository.saveAndFlush(userinfo);
        userinfoSearchRepository.save(userinfo);

        // Search the userinfo
        restUserinfoMockMvc.perform(get("/api/_search/userinfos?query=id:" + userinfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userinfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginid").value(hasItem(DEFAULT_LOGINID.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Userinfo.class);
        Userinfo userinfo1 = new Userinfo();
        userinfo1.setId(1L);
        Userinfo userinfo2 = new Userinfo();
        userinfo2.setId(userinfo1.getId());
        assertThat(userinfo1).isEqualTo(userinfo2);
        userinfo2.setId(2L);
        assertThat(userinfo1).isNotEqualTo(userinfo2);
        userinfo1.setId(null);
        assertThat(userinfo1).isNotEqualTo(userinfo2);
    }
}
