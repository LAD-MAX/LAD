import { Request, Response, NextFunction } from 'express';
import * as newsService from '../services/news.service';
import prisma from '../models';

export const getAllNews = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const page = Number(req.query.page) || 1;
        const limit = Number(req.query.limit) || 20;
        const news = await newsService.findMany(page, limit);
        res.json({ success: true, data: news });
    } catch (error) {
        next(error);
    }
};

export const getNewsBySlug = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const { slug } = req.params;
        const article = await newsService.findBySlug(slug);
        if (!article) {
            return res.status(404).json({ success: false, message: '文章不存在' });
        }
        // 浏览量加1
        await prisma.article.update({
            where: { id: article.id },
            data: { viewCount: { increment: 1 } },
        });
        res.json({ success: true, data: article });
    } catch (error) {
        next(error);
    }
};

export const getHotNews = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const hotNews = await newsService.findHot();
        res.json({ success: true, data: hotNews });
    } catch (error) {
        next(error);
    }
};

export const getAllTags = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const tags = await newsService.findAllTags();
        res.json({ success: true, data: tags });
    } catch (error) {
        next(error);
    }
};

export const searchNews = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const keyword = req.query.keyword as string;
        if (!keyword) {
            return res.json({ success: true, data: [] });
        }
        const articles = await prisma.article.findMany({
            where: {
                isPublished: true,
                title: { contains: keyword },
            },
            orderBy: { publishedAt: 'desc' },
            select: {
                id: true,
                title: true,
                slug: true,
                summary: true,
                coverImage: true,
                type: true,
                publishedAt: true,
            },
            take: 20,
        });
        res.json({ success: true, data: articles });
    } catch (error) {
        next(error);
    }
};