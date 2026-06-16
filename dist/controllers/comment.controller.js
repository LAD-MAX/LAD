"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.deleteComment = exports.createComment = exports.getComments = void 0;
const models_1 = __importDefault(require("../models"));
const commentService = __importStar(require("../services/comment.service"));
// 获取某篇文章的评论
const getComments = async (req, res, next) => {
    try {
        const articleId = Number(req.params.articleId);
        if (isNaN(articleId)) {
            return res.status(400).json({ success: false, message: '无效的文章ID' });
        }
        const comments = await commentService.findByArticleId(articleId);
        res.json({ success: true, data: comments });
    }
    catch (err) {
        next(err);
    }
};
exports.getComments = getComments;
// 发表评论
const createComment = async (req, res, next) => {
    try {
        const { content, articleId, parentId } = req.body;
        const userId = req.user.userId;
        if (!content || !articleId) {
            return res.status(400).json({ success: false, message: '内容不能为空' });
        }
        // 检查文章是否存在
        const article = await models_1.default.article.findUnique({ where: { id: articleId } });
        if (!article) {
            return res.status(404).json({ success: false, message: '文章不存在' });
        }
        // 如果是回复，检查父评论是否存在且属于同一篇文章
        if (parentId) {
            const parentComment = await models_1.default.comment.findUnique({ where: { id: parentId } });
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
    }
    catch (err) {
        next(err);
    }
};
exports.createComment = createComment;
// 删除评论
const deleteComment = async (req, res, next) => {
    try {
        const commentId = Number(req.params.id);
        const comment = await models_1.default.comment.findUnique({ where: { id: commentId } });
        if (!comment) {
            return res.status(404).json({ success: false, message: '评论不存在' });
        }
        // 仅允许作者或管理员删除
        if (comment.userId !== req.user.userId && req.user.role !== 'ADMIN') {
            return res.status(403).json({ success: false, message: '无权删除' });
        }
        await commentService.remove(commentId);
        res.json({ success: true, message: '删除成功' });
    }
    catch (err) {
        next(err);
    }
};
exports.deleteComment = deleteComment;
//# sourceMappingURL=comment.controller.js.map