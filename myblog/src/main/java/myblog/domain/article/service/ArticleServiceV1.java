package myblog.domain.article.service;

import lombok.RequiredArgsConstructor;
import myblog.domain.article.dto.response.ArticleDetailResDto;
import myblog.domain.article.dto.response.ArticleSummaryResDto;
import myblog.domain.article.repository.ArticleRepository;
import myblog.domain.comment.dto.response.CommentAtArticleReqDto;
import myblog.domain.comment.entity.Comment;
import myblog.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceV1 implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ArticleSummaryResDto> getArticleSummary() {
        return articleRepository.findAll()
                .stream().map(ArticleSummaryResDto::toDto).toList();
    }

    @Override
    public ArticleDetailResDto getArticleDetail(Long id) {
        return ArticleDetailResDto.toDto(
                articleRepository.findById(id).orElseThrow()
        );
    }

    @Override
    //FIXME JPQL로 수정
    public List<CommentAtArticleReqDto> getArticleComments(Long id) {
        List<CommentAtArticleReqDto> response = new ArrayList<>();
        for (Comment parentComment : commentRepository.findByArticle_IdAndParentCommentIsNull(id)) {
            List<Comment> childComments = parentComment.getChildComments();
            response.add(CommentAtArticleReqDto.toDto(parentComment, childComments));
        }
        return response;
    }

}


