package app.dao;

import app.model.ForumPost;
import app.repository.ForumPostRepository;
import app.repository.UserRepository;
import app.vo.ForumPostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ForumPostDao {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userDao;

    public boolean checkCoolDown(Long userId) {
        List<ForumPost> results = forumPostRepository.findAllByUserId(userId);
        if(results.size() == 0) return true;
        ForumPost result = results.get(0);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return getTimeDiff(result.getDateCreated(), now) > 60;
    }

    public ForumPost save(Long userId, String content) {
        ForumPost forumPost = new ForumPost();
        forumPost.setUserId(userId);
        forumPost.setContent(content);
        forumPost.setDateCreated(new Timestamp(System.currentTimeMillis()));
        return forumPostRepository.save(forumPost);
    }

    public List<ForumPostVO> findAllToVO() {
        List<ForumPostVO> results = new ArrayList<>();
        for(ForumPost post : forumPostRepository.findAll()) {
            ForumPostVO vo = new ForumPostVO();
            vo.setUserName(userDao.findByUserId(post.getUserId()).getFirst_name());
            vo.setContent(post.getContent());
            vo.setDate(post.getDateCreated());
            results.add(vo);
        }
        return results;
    }

    private int getTimeDiff(Timestamp time1, Timestamp now) {
        return (int)(now.getTime() - time1.getTime()) / 1000;
    }

}
