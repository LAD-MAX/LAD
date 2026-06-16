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
exports.searchNews = exports.getAllTags = exports.getHotNews = exports.getNewsBySlug = exports.getAllNews = void 0;
const newsService = __importStar(require("../services/news.service"));
const models_1 = __importDefault(require("../models"));
const getAllNews = async (req, res, next) => {
    try {
        const news = await newsService.findMany();
        res.json({ success: true, data: news });
    }
    catch (error) {
        next(error);
    }
};
exports.getAllNews = getAllNews;
const getNewsBySlug = async (req, res, next) => {
    try {
        const { slug } = req.params;
        const article = await newsService.findBySlug(slug);
        if (!article) {
            return res.status(404).json({ success: false, message: '文章不存在' });
        }
        // 浏览量加1
        await models_1.default.article.update({
            where: { id: article.id },
            data: { viewCount: { increment: 1 } },
        });
        res.json({ success: true, data: article });
    }
    catch (error) {
        next(error);
    }
};
exports.getNewsBySlug = getNewsBySlug;
const getHotNews = async (req, res, next) => {
    try {
        const hotNews = await newsService.findHot();
        res.json({ success: true, data: hotNews });
    }
    catch (error) {
        next(error);
    }
};
exports.getHotNews = getHotNews;
const getAllTags = async (req, res, next) => {
    try {
        const tags = await newsService.findAllTags();
        res.json({ success: true, data: tags });
    }
    catch (error) {
        next(error);
    }
};
exports.getAllTags = getAllTags;
const searchNews = async (req, res, next) => {
    try {
        const keyword = req.query.keyword;
        if (!keyword) {
            return res.json({ success: true, data: [] });
        }
        const articles = await models_1.default.article.findMany({
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
    }
    catch (error) {
        next(error);
    }
};
exports.searchNews = searchNews;
//# sourceMappingURL=news.controller.js.map