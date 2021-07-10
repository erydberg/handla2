package se.rydberg.handla.lists;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleService(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    public Article save(ArticleDTO articleDTO) {
        articleDTO.setTitle(Sanitize.setCapitalFirstLetter(articleDTO.getTitle()));
        Article articleEntity = toEntity(articleDTO);
        return articleRepository.save(articleEntity);
    }

    public void delete(Integer id) {
        articleRepository.deleteById(id);
    }

    public ArticleDTO getArticleById(Integer id) {
        Article articleEntity = articleRepository.getOne(id);
        return toDto(articleEntity);
    }

    private Article toEntity(ArticleDTO articleDTO) {
        if (articleDTO != null) {
            return modelMapper.map(articleDTO, Article.class);
        } else {
            return null;
        }
    }

    private ArticleDTO toDto(Article articleEntity) {
        if (articleEntity != null) {
            return modelMapper.map(articleEntity, ArticleDTO.class);
        } else {
            return null;
        }
    }
}
