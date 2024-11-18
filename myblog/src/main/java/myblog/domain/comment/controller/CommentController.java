package myblog.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import myblog.domain.comment.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class CommentController {

    private final CommentService commentService;

}
