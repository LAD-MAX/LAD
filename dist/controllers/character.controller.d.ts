import { Request, Response, NextFunction } from 'express';
export declare const getAllCharacters: (req: Request, res: Response, next: NextFunction) => Promise<void>;
export declare const getCharacterById: (req: Request, res: Response, next: NextFunction) => Promise<Response<any, Record<string, any>> | undefined>;
