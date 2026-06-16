import prisma from '../models';
import { Response, NextFunction } from 'express';
import * as commentService from '../services/comment.service';
import { AuthRequest } from '../middlewares/auth.middleware';

// 获取某篇文章的评论
export const getComments = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const articleId = Number(req.params.articleId);
        if (isNaN(articleId)) {
            return res.status(400).json({ success: false, message: '无效的文章ID' });
        }
        const comments = await commentService.findByArticleId(articleId);
        res.json({ success: true, data: comments });
    } catch (err) {
        next(err);
    }
};

// 发表评论
export const createComment = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const { content, articleId, parentId } = req.body;
        const userId = req.user!.userId;

        if (!content || !articleId) {
            return res.status(400).json({ success: false, message: '内容不能为空' });
        }

        // 检查文章是否存在
        const article = await prisma.article.findUnique({ where: { id: articleId } });
        if (!article) {
            return res.status(404).json({ success: false, message: '文章不存在' });
        }

        // 如果是回复，检查父评论是否存在且属于同一篇文章
        if (parentId) {
            const parentComment = await prisma.comment.findUnique({ where: { id: parentId } });
            if (!parentComment || parentComment.articleId !== articleId) {
                return res.status(400).json({ success: false, message: '父评论无效' });
            }
        }

        const comment = await commentService.create({
            content,
            userId,
            articleId,
            parentId: parentId || null,
        });

        res.status(201).json({ success: true, data: comment });
    } catch (err) {
        next(err);
    }
};

// 删除评论
export const deleteComment = async (req: AuthRequest, res: Response, next: NextFunction) => {
    try {
        const commentId = Number(req.params.id);
        const comment = await prisma.comment.findUnique({ where: { id: commentId } });
        if (!comment) {
            return res.status(404).json({ success: false, message: '评论不存在' });
        }

        // 仅允许作者或管理员删除
        if (comment.userId !== req.user!.userId && req.user!.role !== 'ADMIN') {
            return res.status(403).json({ success: false, message: '无权删除' });
        }

        await commentService.remove(commentId);
        res.json({ success: true, message: '删除成功' });
    } catch (err) {
        next(err);
    }
};