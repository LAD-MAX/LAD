import { Router } from 'express';
import {
    getAllNews,
    getNewsBySlug,
    getHotNews,
    getAllTags,
    searchNews,
} from '../controllers/news.controller';

const router = Router();

// 具体路径必须放在 /:slug 之前
router.get('/search', searchNews);
router.get('/hot', getHotNews);
router.get('/tags', getAllTags);

// 通用路径
router.get('/', getAllNews);
router.get('/:slug', getNewsBySlug);

export default router;