package com.sliar.sliarblog.service.Impl;

import com.sliar.sliarblog.dao.CommentRepository;
import com.sliar.sliarblog.entity.Comment;
import com.sliar.sliarblog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    /**
     * 存放递归迭代出来的所有评论
     * */
    private List<Comment> tempReplys = new ArrayList<>();

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).orElse(null));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶级的评论根节点
     * */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        //迭代复制根评论的源数据，避免对原数据进行修改
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层级子代到为第一级子代中
        combineChildren(commentsView);
        return commentsView;
    }
    /**
     * 获取顶层对象的所有子评论
     * */
    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            //循环递归出当前父评论的所有子评论，并存放到集合中
            for (Comment reply1 : replys1) {
                getReplays(reply1);
            }
            //设置当前父评论的子评论
            comment.setReplyComments(tempReplys);
            //清空集合，继续找到父评论的子评论
            tempReplys = new ArrayList<>();
        }
    }

    /**
     * 存放所有迭代出的评论进入集合
     * */
    private void getReplays(Comment comment){
        //添加根节点
        tempReplys.add(comment);
        //如果有子节点
        if(comment.getReplyComments().size() > 0){
            //递归获得子节点
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                getReplays(reply);
            }
        }
    }
}
