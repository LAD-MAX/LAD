import { Router } from 'express';
import { getAllVideos, getVideoById } from '../controllers/video.controller';

const router = Router();

router.get('/', getAllVideos);
router.get('/:id', getVideoById);

export default router;