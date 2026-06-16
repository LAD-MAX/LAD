import { Request, Response, NextFunction } from 'express';
import * as characterService from '../services/character.service';


export const getAllCharacters = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const { rarity, type, tierRank } = req.query;
        const characters = await characterService.findMany({
            rarity: rarity as string,
            type: type as string,
            tierRank: tierRank as string,
        });
        res.json({ success: true, data: characters });
    } catch (error) {
        next(error);
    }
};

export const getCharacterById = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const id = Number(req.params.id);
        if (isNaN(id)) {
            return res.status(400).json({ success: false, message: '无效的角色ID' });
        }
        const character = await characterService.findById(id);
        if (!character) {
            return res.status(404).json({ success: false, message: '角色不存在' });
        }
        res.json({ success: true, data: character });
    } catch (error) {
        next(error);
    }
};