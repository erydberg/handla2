package se.rydberg.handla.lists;

import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(Article article) {
        article.setTitle(Sanitize.setCapitalFirstLetter(article.getTitle()));
        return articleRepository.save(article);
    }

    public void delete(Integer id) {
        articleRepository.deleteById(id);
    }

    public Article getArticleById(Integer id) {
        return articleRepository.getOne(id);
    }
}
