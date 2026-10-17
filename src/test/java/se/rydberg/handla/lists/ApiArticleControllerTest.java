package se.rydberg.handla.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.rydberg.handla.security.ApiTokenService;
import se.rydberg.handla.security.HandlaUser;
import se.rydberg.handla.security.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiArticleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ApiTokenService apiTokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShopListRepository shopListRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleRepository articleRepository;

    String token;

    @BeforeEach
    void setUp() {
        HandlaUser user = userRepository.findByUsername("apitest").orElseGet(() ->
                userRepository.save(HandlaUser.builder().username("apitest").password("{noop}x").enabled(true).build()));
        token = "Bearer " + apiTokenService.create(user, "test");
    }

    @Test
    void should_reject_missing_and_bad_token() throws Exception {
        ShopList list = shopListRepository.save(ShopList.builder().title("Handla").build());
        mockMvc.perform(post("/api/lists/" + list.getId() + "/articles")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"mjölk\"}"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/lists/" + list.getId() + "/articles")
                        .header("Authorization", "Bearer hk_wrong")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"mjölk\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_add_article_with_token() throws Exception {
        ShopList list = shopListRepository.save(ShopList.builder().title("Handla").build());
        mockMvc.perform(post("/api/lists/" + list.getId() + "/articles").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Köp mjölk.\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Mjölk"))
                .andExpect(jsonPath("$.list").value("Handla"))
                .andExpect(jsonPath("$.needsCategory").value(false));
        assertThat(shopListRepository.getShopListWithArticles(list.getId()).getArticles())
                .extracting(Article::getTitle).contains("Mjölk");
    }

    @Test
    void should_give_404_for_unknown_list_and_400_for_blank_text() throws Exception {
        mockMvc.perform(post("/api/lists/999999/articles").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"mjölk\"}"))
                .andExpect(status().isNotFound());
        ShopList list = shopListRepository.save(ShopList.builder().title("Handla").build());
        mockMvc.perform(post("/api/lists/" + list.getId() + "/articles").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"köp.\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_ask_for_category_and_remember_it() throws Exception {
        ShopList list = shopListRepository.save(ShopList.builder().title("Kategorilista").useCategory(true).build());
        Category vegetables = categoryRepository.save(Category.builder().title("Grönsaker").sortOrder(1).build());

        String body = mockMvc.perform(post("/api/lists/" + list.getId() + "/articles").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"köp tomatsås\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.needsCategory").value(true))
                .andExpect(jsonPath("$.categories[?(@.title=='Grönsaker')]").exists())
                .andReturn().getResponse().getContentAsString();
        Integer articleId = com.jayway.jsonpath.JsonPath.read(body, "$.id");

        mockMvc.perform(put("/api/articles/" + articleId + "/category").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"categoryId\":" + vegetables.getId() + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Grönsaker"))
                .andExpect(jsonPath("$.needsCategory").value(false));
        assertThat(articleRepository.findById(articleId).orElseThrow().getCategory().getId()).isEqualTo(vegetables.getId());

        // nästa gång hittas kategorin automatiskt, även med annan stor/liten bokstav
        mockMvc.perform(post("/api/lists/" + list.getId() + "/articles").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"tomatsås\"}"))
                .andExpect(jsonPath("$.needsCategory").value(false))
                .andExpect(jsonPath("$.category").value("Grönsaker"));
    }

    @Test
    void should_list_lists() throws Exception {
        shopListRepository.save(ShopList.builder().title("Listan").build());
        mockMvc.perform(get("/api/lists").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='Listan')].id").exists());
    }

    @Test
    void should_not_accept_token_on_web_pages() throws Exception {
        mockMvc.perform(get("/lists").header("Authorization", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
