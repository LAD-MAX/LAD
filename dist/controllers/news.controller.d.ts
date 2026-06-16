import { Request, Response, NextFunction } from 'express';
export declare const getAllNews: (req: Request, res: Response, next: NextFunction) => Promise<void>;
export declare const getNewsBySlug: (req: Request, res: Response, next: NextFunction) => Promise<Response<any, Record<string, any>> | undefined>;
export declare const getHotNews: (req: Request, res: Response, next: NextFunction) => Promise<void>;
export declare const getAllTags: (req: Request, res: Response, next: NextFunction) => Promise<void>;
export declare const searchNews: (req: Request, res: Response, next: NextFunction) => Promise<Response<any, Record<string, any>> | undefined>;
