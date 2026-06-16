import { Request, Response, NextFunction } from 'express';
export declare const getAllVideos: (req: Request, res: Response, next: NextFunction) => Promise<void>;
export declare const getVideoById: (req: Request, res: Response, next: NextFunction) => Promise<Response<any, Record<string, any>> | undefined>;
