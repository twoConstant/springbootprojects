
class CommentCreReqDto {

    content = "";
    writer = "";

    constructor(content, writer) {
        this.content = content;
        this.writer = writer;
    }
}

export default CommentCreReqDto;