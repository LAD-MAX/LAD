import { Request, Response, NextFunction } from 'express';
import * as videoService from '../services/video.service';
import prisma from '../models';

export const getAllVideos = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const page = Number(req.query.page) || 1;
    const limit = Number(req.query.limit) || 12;
    const category = req.query.category as string || '';
    const keyword = req.query.keyword as string || '';
    const result = await videoService.findMany(page, limit, category, keyword);
    res.json({ success: true, ...result });
  } catch (error) {
    next(error);
  }
};

export const getVideoById = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const id = Number(req.params.id);
        if (isNaN(id)) {
            return res.status(400).json({ success: false, message: '无效的视频ID' });
        }
        const video = await videoService.findById(id);
        if (!video) {
            return res.status(404).json({ success: false, message: '视频不存在' });
        }
        // 增加观看次数
        await prisma.video.update({
            where: { id },
            data: { viewCount: { increment: 1 } },
        });
        res.json({ success: true, data: video });
    } catch (error) {
        next(error);
    }
};