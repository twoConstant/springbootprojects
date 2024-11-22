import CommentDetailResDto from "./CommentDetailResDto";

class CommentAtArticleResDto {

    id = 0;
    writer = "";
    content = "";
    createdAt = "";
    childComments = [];

    constructor(id, writer, content, createdAt, childComments= []) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
        this.childComments = childComments.map(
            (childComment) => new CommentDetailResDto (
                childComment.id,
                childComment.writer,
                childComment.content,
                childComment.createdAt
            )
        )
    }
}

export default CommentAtArticleResDto;

/***
 *  private Long id;
    private String writer;
    private String content;
    private LocalDateTime createAt;
    private List<CommentDetailResDto> childComments;
 */