package se.rydberg.handla.listor;

import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void delete(Integer id) {
        articleRepository.deleteById(id);
    }

}
