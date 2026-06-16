import prisma from '../models';

import { Router } from 'express';
import { authMiddleware, adminMiddleware } from '../middlewares/auth.middleware';

const router = Router();
router.use(authMiddleware, adminMiddleware);

// 文章 CRUD
router.post('/articles', async (req: any, res) => {
    const article = await prisma.article.create({ data: { ...req.body, authorId: req.user.userId } });
    res.json({ success: true, data: article });
});
router.put('/articles/:id', async (req: any, res) => {
    const article = await prisma.article.update({ where: { id: Number(req.params.id) }, data: req.body });
    res.json({ success: true, data: article });
});
router.delete('/articles/:id', async (req: any, res) => {
    await prisma.article.delete({ where: { id: Number(req.params.id) } });
    res.json({ success: true, message: '已删除' });
});

export default router;