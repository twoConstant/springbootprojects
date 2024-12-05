class ArticleDetailResDto {

    id = 0;
    title = "";
    content = "";
    writer = "";
    createdAt = "";
    viewCount = 0;
    starCount = 0;

    constructor(id, title, content, writer, createdAt, viewCount, starCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.starCount = starCount;
    }
    
}

export default ArticleDetailResDto;