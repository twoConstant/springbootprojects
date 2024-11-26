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
public class CommentListAtArticleResDto {
    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResDto> childComments;

    public static CommentListAtArticleResDto toDto(
            Comment parentComment,
            List<Comment> childComments
            ){
        return CommentListAtArticleResDto
                .builder()
                .id(parentComment.getId())
                .writer(parentComment.getWriter())
//                .writer(parentComment.getUsers().getUserName())
                .content(parentComment.getContent())
                .createdAt(parentComment.getCreatedAt())
                .childComments(childComments.stream().map(CommentResDto::toDto).toList())
                .build();
    }

    @Builder
    @Setter
    @Getter
    private static class CommentResDto {
        private Long id;
        private String writer;
        private String content;
        private LocalDateTime createdAt;

        public static CommentResDto toDto(Comment comment) {
            return CommentResDto
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
