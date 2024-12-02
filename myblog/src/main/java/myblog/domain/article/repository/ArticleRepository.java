package myblog.domain.article.repository;

import myblog.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a JOIN FETCH a.users")
    List<Article> findAllWithUsers();

}
