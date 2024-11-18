package myblog.domain.comment.service;

import lombok.RequiredArgsConstructor;
import myblog.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceV1 implements CommentService{

    private final CommentRepository commentRepository;


}
