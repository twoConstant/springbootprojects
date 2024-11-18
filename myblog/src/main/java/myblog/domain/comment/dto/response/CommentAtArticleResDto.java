package myblog.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myblog.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
public class CommentAtArticleResDto {
    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createAt;
    private List<CommentDetailResDto> childComments;

    public static CommentAtArticleResDto toDto(
            Comment parentComment,
            List<Comment> childComments
            ){
        return CommentAtArticleResDto
                .builder()
                .id(parentComment.getId())
                .writer(parentComment.getWriter())
//                .writer(parentComment.getUsers().getUserName())
                .content(parentComment.getContent())
                .createAt(parentComment.getCreatedAt())
                .childComments(childComments.stream().map(CommentDetailResDto::toDto).toList())
                .build();
    }

    @Builder
    @Setter
    @Getter
    private static class CommentDetailResDto {
        private Long id;
        private String writer;
        private String content;
        private LocalDateTime createdAt;

        public static CommentDetailResDto toDto(Comment comment) {
            return CommentDetailResDto
                    .builder()
                    .id(comment.getId())
                    .writer(comment.getWriter())
//                    .writer(comment.getUsers().getUserName())   // 씁...
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }
}
