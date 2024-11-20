
class ArticleEditReqDto {
    constructor(title, writer, content, password, password_valid) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.password = password;
        this.password_valid = password_valid;
    }
}

export default ArticleEditReqDto