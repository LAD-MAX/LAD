import { Router } from 'express';
import newsRoutes from './news.routes';
import characterRoutes from './character.routes';
import videoRoutes from './video.routes';
import authRoutes from './auth.routes';   // 新增
import commentRoutes from './comment.routes';
import adminRoutes from './admin.routes';



const router = Router();

router.use('/news', newsRoutes);
router.use('/characters', characterRoutes);
router.use('/videos', videoRoutes);   // 这一行是关键
router.get('/test', (req, res) => res.json({ ok: true }));
router.use('/auth', authRoutes);           // 新增
router.use('/comments', commentRoutes);

router.use('/admin', adminRoutes);

router.get('/health', (req, res) => {
    res.json({ status: 'ok', database: 'MySQL connected' });
});

export default router;