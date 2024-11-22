class CommentDetailResDto {
    
    id = 0;
    writer = "";
    content = "";
    createdAt = "";


    constructor(id, writer, content, createdAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdAt = new Date(createdAt);   // Date는 ISO Json날짜 데이터를 Date 객체로 전환해준다.
    }
}

export default CommentDetailResDto;

/***
 * 		private Long id;
		private String writer;
		private String content;
		private LocalDateTime createdAt;
 */