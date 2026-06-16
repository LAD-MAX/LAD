import { Router } from 'express';
import { getComments, createComment, deleteComment } from '../controllers/comment.controller';
import { authMiddleware } from '../middlewares/auth.middleware';

const router = Router();

// 公开接口：获取评论
router.get('/article/:articleId', getComments);

// 需要登录的接口
router.post('/', authMiddleware, createComment);
router.delete('/:id', authMiddleware, deleteComment);

export default router;